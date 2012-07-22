package com.mosaic.caches.impl;

import com.mosaic.caches.Cache;
import com.mosaic.caches.Fetcher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *
 */
public abstract class BasicCacheTestCases {

    protected final Cache<String,Integer> cache;

    protected BasicCacheTestCases( Cache<String,Integer> cache ) {
        this.cache = cache;
    }

    @Test
    public void emptyCache_get_expectNull() {
        assertNull( cache.get("a") );
    }

    @Test
    public void emptyCache_size_expect0() {
        assertEquals( 0, cache.size() );
    }

    @Test
    public void emptyCache_putValue_expectPutToReturnNull() {
        assertNull( cache.put( "a", 1 ) );
    }

    @Test
    public void cacheWithOneValue_callSize_expect1() {
        cache.put( "a", 1 );

        assertEquals( 1, cache.size() );
    }

    @Test
    public void noneEmptyCache_putOverValue_expectOldValueReturned() {
        cache.put("a",1);

        assertEquals( new Integer( 1 ), cache.put( "a", 2 ) );
    }

    @Test
    public void noneEmptyCache_getExistingValue_expectToBeAbleToGetValueBack() {
        cache.put("a", 1);

        assertEquals( new Integer( 1 ), cache.get( "a" ) );
    }

    @Test
    public void noneEmptyCache_put_expectValueOverwritten() {
        cache.put("a", 1);

        assertEquals( new Integer( 1 ), cache.get( "a" ) );
    }

    @Test
    public void emptyCache_putIfAbsent_expectPutIfAbsentToReturnNull() {
        assertEquals( null, cache.putIfAbsent( "a", 1 ) );
    }

    @Test
    public void emptyCache_putIfAbsent_expectValueToBeWritten() {
        cache.putIfAbsent("a", 1);

        assertEquals( new Integer( 1 ), cache.get( "a" ) );
    }

    @Test
    public void noneEmptyCache_putIfAbsent_expectValueToNotBeWritten() {
        cache.put("a", 1);

        cache.putIfAbsent("a", 2);

        assertEquals( new Integer( 1 ), cache.get( "a" ) );
    }

    @Test
    public void noneEmptyCache_putIfAbsent_expectPutIfAbsentToReturnPreexistingValue() {
        cache.put( "a", 1 );

        assertEquals( new Integer( 1 ), cache.putIfAbsent( "a", 2 ) );
    }

    @Test
    public void noneEmptyCache_putIfAbsentNewKey_expectValueToBeWritten() {
        cache.put("a", 1);

        cache.putIfAbsent( "b", 2 );

        assertEquals( new Integer( 2 ), cache.get( "b" ) );
    }

    @Test
    public void emptyCache_removeKey_expectNoChange() {
        assertNull( cache.remove( "a" ) );
    }

    @Test
    public void noneEmptyCache_removeKey_expectRemoveToReturnTheRemovedValue() {
        cache.put("a", 1);

        assertEquals( new Integer( 1 ), cache.remove( "a" ) );
    }

    @Test
    public void noneEmptyCache_removeKey_expectValueRemoved() {
        cache.put("a", 1);

        cache.remove("a");
        assertNull( cache.get( "a" ) );
    }

    @Test
    public void emptyCache_getOrFetch_expectFetchToReturnValue() {
        assertEquals( new Integer( 1 ), cache.getOrFetch( "a", new MyFetcher() ) );
    }

    @Test
    public void emptyCache_getOrFetch_expectFetchToHaveStoredTheValue() {
        cache.getOrFetch( "a", new MyFetcher() );

        assertEquals( new Integer(1), cache.get( "a" ) );
    }

    @Test
    public void noneEmptyCache_getOrFetch_expectNoFetchAndNoStore() {
        cache.put("a", 20);

        assertEquals( new Integer(20), cache.getOrFetch( "a", new MyFetcher() ) );
    }

    @Test
    public void cacheWithTwoValues_callSize_expect2() {
        cache.put("a", 20);
        cache.put("b", 10);

        assertEquals( 2, cache.size() );
    }

    private static class MyFetcher implements Fetcher<String,Integer> {

        public Integer fetch( String key ) {
            if ( key.equals("a") ) {
                return 1;
            } else if ( key.equals("b") ) {
                return 2;
            }

            return 3;
        }

    }
}
