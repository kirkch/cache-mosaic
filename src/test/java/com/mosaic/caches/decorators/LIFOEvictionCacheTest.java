package com.mosaic.caches.decorators;

import com.mosaic.caches.impl.BasicCacheTestCases;
import com.mosaic.caches.impl.DefaultCache;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
@SuppressWarnings("unchecked")
public class LIFOEvictionCacheTest extends BasicCacheTestCases {

    public LIFOEvictionCacheTest() {
        super( new LIFOEvictionCache( new DefaultCache("test-cache"), 3 ) );
    }

    @Test
    public void insertOneMoreValueThanLimit_expectLastValueInsertedToBeDropped() {
        cache.put( "a", 1 );
        cache.put( "b", 2 );
        cache.put( "c", 3 );
        cache.put( "d", 4 );

        assertEquals( 3, cache.size() );
        assertEquals( new Integer(1), cache.get("a") );
        assertEquals( new Integer(2), cache.get("b") );
        assertEquals( null, cache.get("c") );
        assertEquals( new Integer(4), cache.get( "d" ) );
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
        assertEquals( new Integer(2), cache.get("b") );
        assertEquals( null, cache.get("c") );
        assertEquals( new Integer(4), cache.get( "d" ) );
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
        assertEquals( new Integer(1), cache.get("a") );
        assertEquals( new Integer(2), cache.get("b") );
        assertEquals( null, cache.get("c") );
        assertEquals( new Integer(4), cache.get( "d" ) );
    }

    @Test
    public void fullCache_addTwoMoreElements_onlyLastOneIsVisible() {
        cache.put( "a", 1 );
        cache.put( "b", 2 );
        cache.put( "c", 3 );
        cache.put( "d", 4 );
        cache.put( "e", 5 );

        assertEquals( 3, cache.size() );
        assertEquals( new Integer(1), cache.get("a") );
        assertEquals( new Integer(2), cache.get("b") );
        assertEquals( null, cache.get("c") );
        assertEquals( null, cache.get( "d" ) );
        assertEquals( new Integer(5), cache.get( "e" ) );
    }

}
