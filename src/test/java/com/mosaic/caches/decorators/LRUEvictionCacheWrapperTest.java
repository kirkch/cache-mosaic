package com.mosaic.caches.decorators;

import com.mosaic.caches.impl.BasicCacheTestCases;
import com.mosaic.caches.impl.CacheHashMap;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
@SuppressWarnings("unchecked")
public class LRUEvictionCacheWrapperTest extends BasicCacheTestCases {

    public LRUEvictionCacheWrapperTest() {
        super( new LRUEvictionCacheWrapper( new CacheHashMap(), 3 ) );
    }

    @Test
    public void insertOneMoreValueThanLimit_expectFirstValueInsertedToBeDropped() {
        cache.put( "a", 1 );
        cache.put( "b", 2 );
        cache.put( "c", 3 );
        cache.put( "d", 4 );

        assertEquals( 3, cache.size() );
        assertEquals( null, cache.get("a") );
        assertEquals( new Integer(2), cache.get("b") );
        assertEquals( new Integer(3), cache.get("c") );
        assertEquals( new Integer( 4 ), cache.get( "d" ) );
    }

    @Test
    public void fullCache_getTailThenInsertNewElement_ensureSecondOldestValueRemoved() {
        cache.put( "a", 1 );
        cache.put( "b", 2 );
        cache.put( "c", 3 );

        cache.get("a");

        cache.put( "d", 4 );

        assertEquals( 3, cache.size() );
        assertEquals( new Integer(1), cache.get("a") );
        assertEquals( null, cache.get("b") );
        assertEquals( new Integer(3), cache.get("c") );
        assertEquals( new Integer( 4 ), cache.get( "d" ) );
    }

    @Test
    public void fullCache_touchLastTwoElements_ensureThirdOldestValueRemoved() {
        cache.put( "a", 1 );
        cache.put( "b", 2 );
        cache.put( "c", 3 );

        cache.get("a");
        cache.get("b");

        cache.put( "d", 4 );

        assertEquals( 3, cache.size() );
        assertEquals( new Integer( 1 ), cache.get( "a" ) );
        assertEquals( new Integer(2), cache.get("b") );
        assertEquals( null, cache.get("c") );
        assertEquals( new Integer(4), cache.get("d") );
    }

}
