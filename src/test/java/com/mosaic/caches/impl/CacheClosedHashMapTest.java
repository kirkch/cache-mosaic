package com.mosaic.caches.impl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class CacheClosedHashMapTest extends BasicCacheTestCases {
    public CacheClosedHashMapTest() {
        super( new CacheClosedHashMap<String,Integer>() );
    }

    @Test
    public void loadEnoughValuesToForceAResize_ensureNoElementsAreLost() {
        for ( int i=0; i<100; i++ ) {
            cache.put( Long.toString(i), i );
            if ( i%3 == 0 ) {
                cache.remove( Long.toString(i-1) );
            }
        }

        for ( int i=0; i<100; i++ ) {
            if ( (i+1)%3 != 0 ) {
                assertEquals( new Integer(i), cache.get(Long.toString(i)) );
            }
        }
    }
}
