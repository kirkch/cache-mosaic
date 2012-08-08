package com.mosaic.io.mmu;

/**
 * Manages large amounts within a NIO ByteBuffer. Named in honour of Memory Management Unit from Virtual Memory paging as
 * it borrows some of the same techniques. Capable of persisting data to disks.<p/>
 *
 * Defragmentation is carried out incrementally as part of other requests. Spreading the cost out over time and removing
 * the need for any form of stop the world defragmentation. During periods of high load, unless the fragmentation is
 * terminal then defragmentation can be delayed.<p/>
 *
 * The MMU is capable of managing multiple regions of memory. It starts with one and then adds more as necessary. Each
 * region of memory follows the same pattern and is contained with the class MemoryRegion.<p/>
 *
 * The MMU is single threaded.
 */
public class MMU {

    public AllocatedBytes allocate( int numBytes ) {
        return null;
    }

    public AllocatedBytes fetch( long pointer ) {
        return null;
    }

}
