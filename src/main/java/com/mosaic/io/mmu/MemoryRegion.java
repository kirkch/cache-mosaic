package com.mosaic.io.mmu;

import com.mosaic.io.Bytes;
import com.mosaic.io.NioBytes;

import java.nio.ByteBuffer;

import static com.mosaic.io.IOConstants.INT_SIZE_BYTES;
import static com.mosaic.io.IOConstants.NULL;

// todo NB this class is not complete; work shelved due to other commitments until further notice

/**
 * Manages a region of memory up to 2 gigabytes in size. Compaction occurs gradually as a side effect of other calls,
 * spreading the load and cost out over time. When the system is running idle extra time may be devoted to defragmentation.
 */
public class MemoryRegion {

    private static final int DATAFORMATVERSION_INDEX          = 0*INT_SIZE_BYTES;
    private static final int ENDOFDATAREGION_INDEX            = 1*INT_SIZE_BYTES;
    private static final int EARLIESTGAPINDATAREGION_INDEX    = 2*INT_SIZE_BYTES;
    private static final int ENDOFPOINTERREGION_INDEX         = 3*INT_SIZE_BYTES;
    private static final int EARLIESTGAPINPOINTERREGION_INDEX = 4*INT_SIZE_BYTES;
    private static final int NUMBEROFOBJECTSINDATAREGIN_INDEX = 5*INT_SIZE_BYTES;
    private static final int UNALLOCATEDBYTESCOUNT_INDEX      = 6*INT_SIZE_BYTES;

    private static final int START_OF_DATAREGION_INDEX        = 20*INT_SIZE_BYTES;



    private static final int OBJECTOVERHEAD_BYTES    = 2*INT_SIZE_BYTES;
    private static final int POINTER_VERSION_BITMASK = 3 << 30;  // sets the top 2 bits to true

    public static MemoryRegion offheap( int sizeBytes ) {
        return new MemoryRegion( new NioBytes(ByteBuffer.allocateDirect(sizeBytes)) );
    }

    public static MemoryRegion memoryMappedFil( int sizeBytes, String file ) {
        return null;
    }

// Byte Buffer Layout
// ==================
// The byte buffer is split into three parts. The first part is meta information about the structure of the file, the
// second is the data stored in the memory region itself and lastly virtual pointers. The pointers returned by MemoryRegion
// only point at data indirectly, this level of indirection allows for the data to be moved about to reduce fragmentation
// where as the 'virtual' pointers once allocated have to remain fixed inplace.

// Meta Region
// -----------
// First 80 bytes (20 ints) reserved for meta information
// 0: version
// 1: end of data region
// 2: earliest gap in the data region
// 3: end of ptr region
// 4: earliest gap in ptr region
// 5: number of objects allocated
// 6: number of unallocated bytes within the data region that is before the end of the data region (ala gaps)
// 7-19 reserved

// Data Region
// -----------
// Starts from byte 80 and grows towards the end of the file. The region is segmented into objects of different sizes.
//
// Objects once created may be moved to reclaim fragmented space. New objects will be allocated into the earliest
// gap in the data region, however if it does not fit then it will grow the data region and sliding compaction
// will move it later.
//
// The format for each object is as follows:
//
// 0-3  (int)  reverse pointer to the objects virtual pointer (optimises compaction)
// 4-7  (int)  size of the objects data region
// rest        the objects data region
//
// When an object is released, the reverse pointer is set to zero and its data region is blanked out with zeros but the
// size remains.

// Virtual Pointer Region
// ----------------------
// The virtual pointer region is stored at the end of the byte buffer. It is indexed in reverse to the rest of the file,
// starting from the end (index 0) and it grows towards the front of the file.
//
// Virtual pointers are indexed from zero so that the memory region can be "easily" resized. In the case of a file, the
// file can be resized and then only the virtual pointer region needs to be moved to the new end of the file. All
// reverse pointers from the data region and pointers stored externally to this class remain unchanged.
//
// Pointers, once created are never moved. However pointers may be freed and reused. New pointers will be allocated
// from the gaps in the pointer region before extending the region.
//
// Pointers within a memory region are 32bits long. The Virtual Pointer region holds virtual pointer to actual data location
// pointers which are also 32 bits long. A virtual pointer reserves two bits (bits indexes 30 and 31) as an allocation
// sequence so that reuse of a pointer address can be detected (so long as the location is not reused four times in a row
// between checks). This means that a Memory Region can store up to 2 gigabits of information spread over a maximum of
// 1 billion objects. Pretty much until the two data regions start to overlap.


    private Bytes buf;



    private MemoryRegion( Bytes buf ) {
        this.buf = buf;

        if ( dataFormatVersion() <= 0 ) {
            initMemory();
        }
    }

    int dataFormatVersion()                        { return buf.readInt( DATAFORMATVERSION_INDEX );          }
    int endOfDataRegionIndex()                     { return buf.readInt( ENDOFDATAREGION_INDEX );            }
    int earliestGapInDataRegionIndex()             { return buf.readInt( EARLIESTGAPINDATAREGION_INDEX );    }
    int endOfPointerRegionIndex()                  { return buf.readInt( ENDOFPOINTERREGION_INDEX );         }
    int earliestGapInPointerRegionIndex()          { return buf.readInt( EARLIESTGAPINPOINTERREGION_INDEX ); }
    int numberOfObjectsInDataRegion()              { return buf.readInt( NUMBEROFOBJECTSINDATAREGIN_INDEX ); }
    int unallocatedDataRegionByteCount()           { return buf.readInt( UNALLOCATEDBYTESCOUNT_INDEX );      }
    int sizeOfMemoryRegion()                       { return buf.capacity(); }

    void setDataFormatVersion(int v)               { buf.writeInt( DATAFORMATVERSION_INDEX, v );          }
    void setEndOfDataRegionIndex(int v)            { buf.writeInt( ENDOFDATAREGION_INDEX, v );            }
    void setEarliestGapInDataRegionIndex(int v)    { buf.writeInt( EARLIESTGAPINDATAREGION_INDEX, v );    }
    void setEndOfPointerRegionIndex(int v)         { buf.writeInt( ENDOFPOINTERREGION_INDEX, v );         }
    void setEarliestGapInPointerRegionIndex(int v) { buf.writeInt( EARLIESTGAPINPOINTERREGION_INDEX, v ); }
    void setNumberOfObjectsInDataRegion(int v)     { buf.writeInt( NUMBEROFOBJECTSINDATAREGIN_INDEX, v ); }
    void setUnallocatedDataRegionByteCount(int v)  { buf.writeInt( UNALLOCATEDBYTESCOUNT_INDEX, v );      }


    private void initMemory() {
        setDataFormatVersion( 1 );

        setEndOfDataRegionIndex( START_OF_DATAREGION_INDEX );
        setEarliestGapInDataRegionIndex( START_OF_DATAREGION_INDEX );
        setEndOfPointerRegionIndex( 0 );
        setEarliestGapInPointerRegionIndex( 0 );
        setNumberOfObjectsInDataRegion( 0 );
        setUnallocatedDataRegionByteCount( 0 );
    }

    private void dumpHeader() {
        System.out.println( "dataFormatVersion               = " + dataFormatVersion() );
        System.out.println( "endOfDataRegionIndex            = " + endOfDataRegionIndex() );
        System.out.println( "earliestGapInDataRegionIndex    = " + earliestGapInDataRegionIndex() );
        System.out.println( "endOfPointerRegionIndex         = " + endOfPointerRegionIndex() );
        System.out.println( "earliestGapInPointerRegionIndex = " + earliestGapInPointerRegionIndex() );
        System.out.println( "numberOfObjectsInDataRegion     = " + numberOfObjectsInDataRegion() );
        System.out.println( "unallocatedDataRegionByteCount  = " + unallocatedDataRegionByteCount() );
    }

    /**
     *
     *
     * @return null if the allocation failed
     */
    public AllocatedBytes allocate( int numDataBytes ) {
//        Validate.gtZero( numDataBytes, "numDataBytes" );

        int dataAndHeaderByteCount = numDataBytes + OBJECTOVERHEAD_BYTES;
        int dataRegionPointer      = assignDataRegionPointer( dataAndHeaderByteCount );
        int virtualPointer         = assignAndInitialiseNewVirtualPointerFor( dataRegionPointer );


        buf.setPosition( dataRegionPointer );
        buf.writeInt( virtualPointer );
        buf.writeInt( numDataBytes );

        return new AllocatedBytes( virtualPointer, this );
    }

    public void release() {
        //todo
    }

    /**
     * Everytime the memory region moves objects around due to compaction, the version number is incremented. This allows
     * AllocatedBuffer to detect when it may be holding onto a view that has been moved.
     */
    int getVersion() {
        return 0;
    }

    /**
     *
     *
     * @return null if the pointer is unknown
     */
    public AllocatedBytes fetch( int pointer ) {
        return null;
    }



    /**
     * Internal version of fetch that returns a Bytes object pointing directly into the data region. This view could become
     * invalid due to compactions so check the memory region version before reusing this view.
     *
     * @package provides internal access to a part of MemoryRegion needed by AllocatedBytes
     */
    Bytes fetchUnderlyingView( int virtualPointerOffset ) {
        int dataPointer = lookupVirtualPointer( virtualPointerOffset );

        return null;
    }

    void throwIfInvalidPointer( int virtualPointerOffset ) {
        lookupVirtualPointer( virtualPointerOffset );
    }

    /**
     * Translates a virtual pointer (as an offset from the beginning of the pointer region) to the direct index into the
     * nio buffer. Where the object begins.
     *
     * @return the data pointer
     */
    private int lookupVirtualPointer( int virtualPointerOffset ) {
        if ( virtualPointerOffset >= endOfPointerRegionIndex() ) {
            throw new IllegalArgumentException( String.format("specified pointer '%' is out of bounds") );
        }

        int virtualPointer = sizeOfMemoryRegion() - (virtualPointerOffset * INT_SIZE_BYTES);
        int dataPointer    = buf.readInt( virtualPointer );

        if ( dataPointer == NULL ) {
            throw new IllegalArgumentException( String.format("specified pointer '%' has been deallocated") );
        }

        int vpVersion = virtualPointer & POINTER_VERSION_BITMASK;
        int dpVersion = dataPointer & POINTER_VERSION_BITMASK;

        if ( vpVersion != dpVersion ) {
            throw new IllegalArgumentException( String.format("specified pointer '%' has been deallocated and reassigned") );
        }

        return dataPointer;
    }

    private int assignDataRegionPointer( int dataAndHeaderByteCount ) {
        int ptr = endOfDataRegionIndex();

        setEndOfDataRegionIndex( ptr+dataAndHeaderByteCount );
        setEarliestGapInDataRegionIndex( ptr+dataAndHeaderByteCount );
        setNumberOfObjectsInDataRegion( numberOfObjectsInDataRegion()+1 );

        return ptr;
    }

    private int assignAndInitialiseNewVirtualPointerFor( int dataRegionPointer ) {
        int ptr = endOfPointerRegionIndex();

        setEndOfPointerRegionIndex(ptr+1);
        setEarliestGapInPointerRegionIndex(ptr+1);

        buf.writeInt( buf.capacity()-((ptr+1)*INT_SIZE_BYTES), dataRegionPointer );


        return ptr;
    }
}
