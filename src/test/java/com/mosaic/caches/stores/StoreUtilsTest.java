package com.mosaic.caches.stores;

import org.junit.Test;

import static com.mosaic.caches.stores.StoreUtils.roundUpToClosestPowerOf2;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class StoreUtilsTest {

    @Test
    public void testRoundUpToClosestPowerOf2() {
        assertEquals( 2, roundUpToClosestPowerOf2( -1 ) );
        assertEquals( 2, roundUpToClosestPowerOf2( 0 ) );
        assertEquals( 2, roundUpToClosestPowerOf2( 1 ) );
        assertEquals( 2, roundUpToClosestPowerOf2( 2 ) );
        assertEquals( 4, roundUpToClosestPowerOf2( 3 ) );
        assertEquals( 4, roundUpToClosestPowerOf2( 4 ) );
        assertEquals( 8, roundUpToClosestPowerOf2( 5 ) );
        assertEquals( 8, roundUpToClosestPowerOf2( 6 ) );
        assertEquals( 8, roundUpToClosestPowerOf2( 7 ) );
        assertEquals( 8, roundUpToClosestPowerOf2( 8 ) );
        assertEquals( 16, roundUpToClosestPowerOf2( 9 ) );
        assertEquals( 32, roundUpToClosestPowerOf2( 18 ) );
    }

}
