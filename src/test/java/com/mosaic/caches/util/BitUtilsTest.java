package com.mosaic.caches.util;

import org.junit.Test;

import static com.mosaic.caches.util.BitUtils.roundUpToClosestPowerOf2;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class BitUtilsTest {

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


    @Test
    public void numberOfBitsSet() {
        assertEquals( 0, BitUtils.numberOfBitsSet( 0 ) );
        assertEquals( 1, BitUtils.numberOfBitsSet( 1 ) );
        assertEquals( 2, BitUtils.numberOfBitsSet( 3 ) );
        assertEquals( 3, BitUtils.numberOfBitsSet( 7 ) );
        assertEquals( 4, BitUtils.numberOfBitsSet( 15 ) );
        assertEquals( 5, BitUtils.numberOfBitsSet( 31 ) );

        assertEquals( 0, BitUtils.numberOfBitsSet( 0 ) );
        assertEquals( 2, BitUtils.numberOfBitsSet( 2 ) );
        assertEquals( 3, BitUtils.numberOfBitsSet( 4 ) );
        assertEquals( 4, BitUtils.numberOfBitsSet( 8 ) );
        assertEquals( 5, BitUtils.numberOfBitsSet( 16 ) );
        assertEquals( 6, BitUtils.numberOfBitsSet( 32 ) );
    }
}
