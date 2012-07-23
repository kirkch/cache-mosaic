package com.mosaic.caches;

import com.mosaic.caches.impl.DefaultCache;
import com.mosaic.caches.stores.InlineMapStore;
import com.mosaic.caches.stores.MapStore;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class CacheFactory {

    /**
     * Create the standard in memory cache. This cache is not thread safe. It is however robust and handles most
     * hash code schemes efficiently.
     */
    public static <K,V> Cache<K,V> singleThreadedDefaultCache( String cacheName, Class<K> keyType, Class<V> valueType ) {
        MapStore<K, V> store = new MapStore<K, V>( new HashMap<K, V>() );

        return new DefaultCache<K,V>( cacheName, store );
    }

    /**
     * Create a very fast cache backed by an inline hash map. Inline meaning that the hash map handles collisions by
     * moving on to the next bucket within the hash map array rather than using linked lists. This makes the hash map
     * extremely fast for sparse data (and sparse hashing algorithms). However it does not handle clustering very well,
     * in which case the worst case performance becomes linear.  
     *
     * This cache is not thread safe and while it is approximately two times faster than the default cache backed by java.util.HashMap;
     * however care must be taken because it does have worse worst case performance and it uses more memory. So testing for
     * your specific scenario is recommended before committing to using this hash map.
     */
    public static <K,V> Cache<K,V> singleThreadedInlineHashMapCache( String cacheName, Class<K> keyType, Class<V> valueType ) {
        InlineMapStore<K, V> store = new InlineMapStore<K, V>();

        return new DefaultCache<K,V>( cacheName, store );
    }

    /**
     * Create an in memory cache that wraps java.util.concurrent.ConcurrentHashMap. The cache is thread safe and robust.
     * It is however not always the fastest cache. Running between 50% and 200% slower than using an inline cache.
     */
    public static <K,V> Cache<K,V> threadSafeConcurrentMapCache( String cacheName, Class<K> keyType, Class<V> valueType ) {
        MapStore<K, V> store = new MapStore<K, V>( new ConcurrentHashMap<K, V>() );

        return new DefaultCache<K,V>( cacheName, store );
    }

}
