package com.mosaic.caches.impl;

import com.mosaic.caches.Fetcher;
import com.mosaic.caches.stores.MapStore;
import com.mosaic.caches.stores.Store;

import java.util.HashMap;

/**
 * Wraps an instance of java.util.Map.<p/>
 *
 * It should be noted that ConcurrentHashMap is approximately 50% slower than java.util.HashMap. However HashMap is
 * not thread safe.
 */
public class StoreCache<K,V> extends BaseCache<K,V> {

    private Store<K,V> store;

    public StoreCache( String name ) {
        this( name, new MapStore<K,V>(new HashMap<K,V>()) );
    }

    public StoreCache( String cacheName, Store<K, V> underlyingStore ) {
        super( cacheName );

        this.store     = underlyingStore;
    }


    public int size() {
        return store.size();
    }

    public V doGet( K key, int keyHashCode ) {
        return store.get(key, keyHashCode);
    }

    public V doPut( K key, V newValue, int keyHashCode ) {
        return store.put(key, newValue, keyHashCode);
    }

    public V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
        V currentValue = store.get(key, keyHashCode);
        if ( currentValue != null ) {
            return currentValue;
        }

        return doPut( key, newValue, keyHashCode );
    }

    public V doRemove( K key, int keyHashCode ) {
        return store.remove(key, keyHashCode);
    }

    public V doGetOrFetch( K key, Fetcher<K,V> fetcher, int keyHashCode ) {
        V currentValue = store.get(key, keyHashCode);
        if ( currentValue != null ) {
            return currentValue;
        }

        V newValue = fetcher.fetch( key );

        doPut( key, newValue, keyHashCode );

        return newValue;
    }
}
