package com.mosaic.io.mmu;

import com.mosaic.io.IOConstants;

/**
 *
 */
public class MemoryRegionOffHeapTests extends MemoryRegionSharedTestCases {
    public MemoryRegionOffHeapTests() {
        super( MemoryRegion.offheap( MRSIZE ) );
    }
}
