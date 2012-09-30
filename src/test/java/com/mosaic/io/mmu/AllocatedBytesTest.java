package com.mosaic.io.mmu;

import com.mosaic.io.Bytes;
import com.mosaic.io.NioBytes;
import com.mosaic.io.SharedBytesTestCases;
import org.junit.After;

/**
 *
 */
public class AllocatedBytesTest extends SharedBytesTestCases {

    private MemoryRegion memoryRegion = MemoryRegion.offheap( 1024 );

    protected Bytes createBuffer( int size ) {
        return NioBytes.newBuffer( size );
    }

    @After
    public void tearDown() {
        memoryRegion.release();
    }
}
