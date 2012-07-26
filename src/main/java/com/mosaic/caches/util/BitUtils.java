package com.mosaic.caches.util;

/**
 *
 */
public abstract class BitUtils {

    public static int roundUpToClosestPowerOf2( int v ) {
        int n = 2;

        while ( v > n ) {
            n *= 2;
        }

        return n;
    }

    public static long roundUpToClosestPowerOf2( long v ) {
        long n = 2;

        while ( v > n ) {
            n *= 2;
        }

        return n;
    }

    public static int numberOfBitsSet( int v ) {
        int count = 0;

        while ( v != 0 ) {
            count++;

            v = v >> 1;
        }

        return count;
    }

}
