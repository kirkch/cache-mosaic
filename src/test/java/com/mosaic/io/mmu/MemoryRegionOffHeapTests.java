package com.mosaic.io.mmu;

import com.mosaic.io.IOConstants;
import org.junit.Ignore;

/**
 *
 */
@Ignore
public class MemoryRegionOffHeapTests extends MemoryRegionSharedTestCases {
    public MemoryRegionOffHeapTests() {
        super( MemoryRegion.offheap( IOConstants.ONE_MEGABYTE ) );
    }
}
