package com.mosaic.caches.util;

/**
 *
 */
public class Validate {

    public static void gtZero( int v, String argName ) {
        if ( v <= 0 ) {
            throw new IllegalArgumentException( argName + " must be greater than zero" );
        }
    }
}
