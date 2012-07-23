package com.mosaic.caches.impl;

import com.mosaic.caches.stores.InlineMapStore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class CacheInlineHashMapTest extends BasicCacheTestCases {
    public CacheInlineHashMapTest() {
        super( new DefaultCache<String, Integer>("test-cache", new InlineMapStore()) );
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
