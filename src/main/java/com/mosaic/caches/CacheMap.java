package com.mosaic.caches;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CacheMap<K,V> extends Cache<K,V> {

    private Map<K,V> map;

    public CacheMap() {
        this( new HashMap<K,V>() );
    }

    public CacheMap( Map<K,V> underlyingMap ) {
        map = underlyingMap;
    }

    public int size() {
        return map.size();
    }

    protected V doGet( K key, int keyHashCode ) {
        return map.get(key);
    }

    protected V doPut( K key, V newValue, int keyHashCode ) {
        return map.put(key, newValue);
    }

    protected V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
        V currentValue = map.get(key);
        if ( currentValue != null ) {
            return currentValue;
        }

        return doPut( key, newValue, keyHashCode );
    }

    protected V doRemove( K key, int keyHashCode ) {
        return map.remove(key);
    }

    protected V doGetOrFetch( K key, Fetcher<K,V> fetcher, int keyHashCode ) {
        V currentValue = map.get(key);
        if ( currentValue != null ) {
            return currentValue;
        }

        V newValue = fetcher.fetch( key );

        doPut( key, newValue, keyHashCode );

        return newValue;
    }
}
