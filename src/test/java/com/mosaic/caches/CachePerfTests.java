package com.mosaic.caches;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class CachePerfTests {

/*   2Ghz macbook pro java 1.6 - single thread
CacheMap CacheCustomHashMap concurrentHashMap CacheClosedHashMap
  13.986  15.259  27.827  8.854
  16.441  15.876  27.065  8.38
  17.979  14.838  26.721  8.465
  18.781  13.691  25.404  8.414
  15.018  14.307  25.95  8.304
     */

    private int round = 0;
// -server -Xms800m -Xmx800m -XX:MaxPermSize=100m -XX:PermSize=100m -Dsun.net.inetaddr.ttl=120 -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:+CMSIncrementalPacing -XX:CMSIncrementalDutyCycle=10 -XX:CMSIncrementalDutyCycleMin=0 -XX:SurvivorRatio=2 -XX:NewRatio=3 -XX:-CMSClassUnloadingEnabled
    @Test
    public void perfTest() {
        doPerfTests( new CacheMap(), new CacheCustomHashMap(), new CacheMap( new ConcurrentHashMap() ), new CacheClosedHashMap() ); // warmup
        doPerfTests( new CacheMap(), new CacheCustomHashMap(), new CacheMap( new ConcurrentHashMap() ), new CacheClosedHashMap() );
        doPerfTests( new CacheMap(), new CacheCustomHashMap(), new CacheMap(new ConcurrentHashMap())  , new CacheClosedHashMap() );
        doPerfTests( new CacheMap(), new CacheCustomHashMap(), new CacheMap(new ConcurrentHashMap())  , new CacheClosedHashMap() );
        doPerfTests( new CacheMap(), new CacheCustomHashMap(), new CacheMap(new ConcurrentHashMap())  , new CacheClosedHashMap() );
        doPerfTests( new CacheMap(), new CacheCustomHashMap(), new CacheMap(new ConcurrentHashMap())  , new CacheClosedHashMap() );
    }

    private void doPerfTests( Cache...caches ) {

        if ( round == 0 ) {
            for ( Cache c : caches ) {
                System.out.print( c.getClass().getSimpleName() + " " );
            }

            System.out.println( "" );
        }

        StringBuffer buf = new StringBuffer();


        for ( Cache c : caches ) {
            long durationNanos = doPerfTest(c);

            buf.append( "  " );
            buf.append( durationNanos/1000000.0 );
        }

        if ( round > 0 ) {
            System.out.println(buf.toString());
        }

        round++;

        caches = null;

        Runtime.getRuntime().gc();
    }

    private long doPerfTest( Cache cache ) {
        long startNanos = System.nanoTime();

//        String prevKey = "0";
        for ( int i=0; i<=100000; i++ ) {
            String key = Integer.toString(i);

            cache.get(key);
            cache.putIfAbsent( key, i );
//            cache.putIfAbsent( key, i);
//            cache.put( key, i );
//            cache.get(prevKey);


//            prevKey = key;
        }

        long endNanos = System.nanoTime();

        return endNanos - startNanos;
    }

}