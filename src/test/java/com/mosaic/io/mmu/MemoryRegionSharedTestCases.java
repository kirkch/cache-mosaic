package com.mosaic.io.mmu;

import com.mosaic.io.IOConstants;
import org.junit.Test;

import java.nio.BufferOverflowException;

import static org.junit.Assert.*;


/**
 *
 */
public abstract class MemoryRegionSharedTestCases {

    protected static final int MRSIZE = IOConstants.ONE_MEGABYTE;

    private MemoryRegion memoryRegion;

    protected MemoryRegionSharedTestCases( MemoryRegion memoryRegion ) {
        this.memoryRegion = memoryRegion;
    }

    @Test
    public void newBuffer_verifyHeaderInformation() {
        assertEquals(      1, memoryRegion.dataFormatVersion() );
        assertEquals(     80, memoryRegion.endOfDataRegionIndex() );
        assertEquals(     80, memoryRegion.earliestGapInDataRegionIndex() );
        assertEquals(      0, memoryRegion.endOfPointerRegionIndex() );
        assertEquals(      0, memoryRegion.earliestGapInPointerRegionIndex() );
        assertEquals(      0, memoryRegion.numberOfObjectsInDataRegion() );
        assertEquals(      0, memoryRegion.unallocatedDataRegionByteCount() );
        assertEquals( MRSIZE, memoryRegion.sizeOfMemoryRegion() );
    }

//    @Test todo
    public void allocate100Bytes_expectToBeAbleToStoreAndRetrieveDataInBytes() {
        AllocatedBytes bytes = memoryRegion.allocate( 100 );

        assertValidBytes( bytes );

        assertEquals(      1, memoryRegion.dataFormatVersion() );
        assertEquals( 80+108, memoryRegion.endOfDataRegionIndex() );
        assertEquals( 80+108, memoryRegion.earliestGapInDataRegionIndex() );
        assertEquals(      1, memoryRegion.endOfPointerRegionIndex() );
        assertEquals(      1, memoryRegion.earliestGapInPointerRegionIndex() );
        assertEquals(      1, memoryRegion.numberOfObjectsInDataRegion() );
        assertEquals(      0, memoryRegion.unallocatedDataRegionByteCount() );
        assertEquals( MRSIZE, memoryRegion.sizeOfMemoryRegion() );
    }

//    @Test todo
    public void allocate5Bytes_overflowit_expectException() {
        AllocatedBytes bytes = memoryRegion.allocate( 5 );

        assertEquals(      1, memoryRegion.dataFormatVersion() );
        assertEquals(  80+13, memoryRegion.endOfDataRegionIndex() );
        assertEquals(  80+13, memoryRegion.earliestGapInDataRegionIndex() );
        assertEquals(      1, memoryRegion.endOfPointerRegionIndex() );
        assertEquals(      1, memoryRegion.earliestGapInPointerRegionIndex() );
        assertEquals(      1, memoryRegion.numberOfObjectsInDataRegion() );
        assertEquals(      0, memoryRegion.unallocatedDataRegionByteCount() );
        assertEquals( MRSIZE, memoryRegion.sizeOfMemoryRegion() );

        bytes.writeInt( 101 );

        try {
            bytes.writeInt(1);
            fail( "expected overflow exception" );
        } catch ( BufferOverflowException ex ) {

        }
    }

//    @Test
//    public void allocateThenDeallocatePointer_expectRegionToReturnToSameStateAsWhenFirstCreated() {
//        AllocatedBytes bytes = memoryRegion.allocate( 5 );
//
//
//        assertEquals(  1, memoryRegion.dataFormatVersion() );
//        assertEquals( 80, memoryRegion.endOfDataRegionIndex() );
//        assertEquals( 80, memoryRegion.earliestGapInDataRegionIndex() );
//        assertEquals(  0, memoryRegion.endOfPointerRegionIndex() );
//        assertEquals(  0, memoryRegion.earliestGapInPointerRegionIndex() );
//        assertEquals(  0, memoryRegion.numberOfObjectsInDataRegion() );
//        assertEquals(  0, memoryRegion.unallocatedDataRegionByteCount() );
//    }

    //
    //
    // givenValidPointer_expectToBeAbleToRetrieveValidBytes
    // givenValidPointer_freePointer_expectPointerToBecomeInvalid
    // givenValidPointer_freePointer_expectAllocatedBytesToBeMarkedInvalid

    // allocation20Pointers_expectAllToBeValid
    // allocate2PointersFreeFirstOne_repeatTwentyTimesToForceFragmentation_expectAllRemainingPointersToBeValid

    private void assertValidBytes( AllocatedBytes bytes ) {
        bytes.writeInt( 42 );
        bytes.writeString( "hello" );

        bytes.setPosition( 0 );

        assertEquals( 42, bytes.readInt() );
        assertEquals( "hello", bytes.readString() );
    }
}
