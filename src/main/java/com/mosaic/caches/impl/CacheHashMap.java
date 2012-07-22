package com.mosaic.caches.impl;

import com.mosaic.caches.Cache;
import com.mosaic.caches.Fetcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps an instance of java.util.Map.<p/>
 *
 * It should be noted that ConcurrentHashMap is approximately 50% slower than java.util.HashMap. However HashMap is
 * not thread safe.
 */
public class CacheHashMap<K,V> extends Cache<K,V> {

    private Map<K,V> map;

    public CacheHashMap() {
        this( new HashMap<K,V>() );
    }

    public CacheHashMap( Map<K, V> underlyingMap ) {
        map = underlyingMap;
    }

    public int size() {
        return map.size();
    }

    public V doGet( K key, int keyHashCode ) {
        return map.get(key);
    }

    public V doPut( K key, V newValue, int keyHashCode ) {
        return map.put(key, newValue);
    }

    public V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
        V currentValue = map.get(key);
        if ( currentValue != null ) {
            return currentValue;
        }

        return doPut( key, newValue, keyHashCode );
    }

    public V doRemove( K key, int keyHashCode ) {
        return map.remove(key);
    }

    public V doGetOrFetch( K key, Fetcher<K,V> fetcher, int keyHashCode ) {
        V currentValue = map.get(key);
        if ( currentValue != null ) {
            return currentValue;
        }

        V newValue = fetcher.fetch( key );

        doPut( key, newValue, keyHashCode );

        return newValue;
    }
}
