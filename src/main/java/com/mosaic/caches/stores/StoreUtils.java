package com.mosaic.caches.stores;

/**
 *
 */
public abstract class StoreUtils {

    public static int roundUpToClosestPowerOf2( int v ) {
        int n = 2;

        while ( v > n ) {
            n *= 2;
        }

        return n;
    }

}
