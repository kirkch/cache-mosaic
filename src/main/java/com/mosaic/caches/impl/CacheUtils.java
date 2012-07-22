package com.mosaic.caches.impl;

/**
 *
 */
abstract class CacheUtils {

    public static int roundUpToClosestPowerOf2( int v ) {
        int n = 2;

        while ( v > n ) {
            n *= 2;
        }

        return n;
    }

}
