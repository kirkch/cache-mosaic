package com.mosaic.caches.decorators;

import com.mosaic.caches.CacheFactory;
import com.mosaic.caches.impl.BasicCacheTestCases;
import com.mosaic.caches.util.HashWheel;
import com.mosaic.jtunit.TestTools;
import org.junit.Test;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import static org.junit.Assert.*;

/**
 *
 */
@SuppressWarnings({"unchecked", "UnnecessaryBoxing", "UnusedAssignment"})
public class TTLEvictionCacheTest extends BasicCacheTestCases {
    public TTLEvictionCacheTest() {
        super( new TTLEvictionCache( CacheFactory.singleThreadedInlineHashMapCache("junit",String.class,Integer.class), 5, new HashWheel(System.currentTimeMillis(),4,4)) );
    }

    @Test
    public void waitTTLPeriod_expectGetToReturnNull() throws InterruptedException {
        Integer value = 42;

        cache.put( "a", value );

        Thread.sleep(10);

        assertNull( cache.get( "a" ) );
    }

    @Test
    public void waitTTLPeriod_callGetExpectValueToGetGCd() throws InterruptedException {
        Integer value = new Integer(42);

        cache.put( "a", value );

        Reference ref = new WeakReference( value );
        value = null;
        Thread.sleep(10);

        assertNull( cache.get("a") );

        TestTools.spinUntilReleased( ref );
    }

    @Test
    public void cacheTwoValues_callGetOnOneWaitTTLPeriod_expectOtherElementToExpire() throws InterruptedException {
        Integer value1 = new Integer(42);
        Integer value2 = new Integer(12);

        cache.put( "a", value1 );
        cache.put( "b", value2 );

        long startMillis = System.currentTimeMillis();

        Thread.sleep(4);

        if ( (System.currentTimeMillis()-startMillis) < 5 ) {   // timing logic added to avoid flickering test on slow/heavily loaded CI machines
            assertEquals( new Integer(42), cache.get("a") );
            cache.get("a");

            Thread.sleep(5);

            if ( (System.currentTimeMillis()-startMillis) < 10 ) {
                assertEquals( new Integer(42), cache.get("a") );
                assertNull( cache.get("b") );
            }
        }
    }

}
