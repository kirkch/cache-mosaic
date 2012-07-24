package com.mosaic.caches;

import org.junit.Test;

/**
 *
 */
//@Ignore
@SuppressWarnings({"unchecked", "UnnecessaryBoxing"})
public class CachePerfTests {

/*   2Ghz macbook pro java 1.6 - single thread
CacheMap CacheCustomHashMap concurrentHashMap CacheClosedHashMap
  13.986  15.259  27.827  8.854
  16.441  15.876  27.065  8.38
  17.979  14.838  26.721  8.465
  18.781  13.691  25.404  8.414
  15.018  14.307  25.95  8.304
     */

    public static void main( String[] args ) {
        new CachePerfTests().perfTest();
    }

    private int round = 0;
// -server -Xms800m -Xmx800m -XX:MaxPermSize=100m -XX:PermSize=100m -Dsun.net.inetaddr.ttl=120 -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:+CMSIncrementalPacing -XX:CMSIncrementalDutyCycle=10 -XX:CMSIncrementalDutyCycleMin=0 -XX:SurvivorRatio=2 -XX:NewRatio=3 -XX:-CMSClassUnloadingEnabled
    @Test
    public void perfTest() {
        for ( int i=0; i<5; i++ ) {
            doPerfTests(
                CacheFactory.singleThreadedDefaultCache( "ju.HashMap", String.class, Integer.class ),
                CacheFactory.threadSafeConcurrentMapCache( "ju.ConcHashMap", String.class, Integer.class ),
                CacheFactory.singleThreadedInlineHashMapCache( "inline-hashmap", String.class, Integer.class ),
                CacheFactory.readWriteInlineMapCache( "rw-inline-hashmap", String.class, Integer.class )
            );
        }
    }

//    @Test
//    public void composites() {
//        doPerfTests( new CacheInlineHashMap(), new LRUEvictionCacheWrapper(new CacheInlineHashMap(),3) );
//        doPerfTests( new CacheInlineHashMap(), new LRUEvictionCacheWrapper(new CacheInlineHashMap(),3) );
//        doPerfTests( new CacheInlineHashMap(), new LRUEvictionCacheWrapper(new CacheInlineHashMap(),3) );
//        doPerfTests( new CacheInlineHashMap(), new LRUEvictionCacheWrapper(new CacheInlineHashMap(),3) );
//        doPerfTests( new CacheInlineHashMap(), new LRUEvictionCacheWrapper(new CacheInlineHashMap(),3) );
//        doPerfTests( new CacheInlineHashMap(), new LRUEvictionCacheWrapper(new CacheInlineHashMap(),3) );
//    }

    private void doPerfTests( Cache...caches ) {

        if ( round == 0 ) {
            for ( Cache c : caches ) {
                System.out.print( c.getCacheName() + " " );
            }

            System.out.println( "" );
        }

        StringBuffer buf = new StringBuffer();


        for ( Cache c : caches ) {
            long durationNanos = doPerfTest(c);

            buf.append( "  " );
            buf.append( durationNanos/1000000.0 );
        }

//        if ( round > 1 ) {
            System.out.println(buf.toString());
//        }

        round++;

        caches = null; // free up the caches for GC

        Runtime.getRuntime().gc();
    }

    private long doPerfTest( Cache cache ) {
        long startNanos = System.nanoTime();

//        String prevKey = "0";
        for ( int i=0; i<=10000; i++ ) {
//            String key = Integer.toString(i);
            Integer v = new Integer(i);
            cache.get(v);
            cache.putIfAbsent( v, v );
//            cache.putIfAbsent( key, i);
//            cache.put( key, i );
//            cache.get(prevKey);


//            prevKey = key;
        }

        long endNanos = System.nanoTime();

        return endNanos - startNanos;
    }

}
