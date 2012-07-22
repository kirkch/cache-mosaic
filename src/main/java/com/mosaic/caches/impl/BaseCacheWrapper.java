package com.mosaic.caches.impl;

import com.mosaic.caches.Cache;
import com.mosaic.caches.Fetcher;

/**
 *
 */
public abstract class BaseCacheWrapper<K,V> extends Cache<K,V> {

    private final Cache<K, V> wrappedCache;

    public BaseCacheWrapper( Cache<K,V> wrappedCache ) {
        this.wrappedCache = wrappedCache;
    }

    @Override
    public int size() {
        return wrappedCache.size();
    }

    @Override
    public V doGet( K key, int keyHashCode ) {
        return wrappedCache.doGet(key, keyHashCode);
    }

    @Override
    public V doPut( K key, V newValue, int keyHashCode ) {
        return wrappedCache.doPut( key, newValue, keyHashCode );
    }

    @Override
    public V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
        return wrappedCache.doPutIfAbsent( key, newValue, keyHashCode );
    }

    @Override
    public V doRemove( K key, int keyHashCode ) {
        return wrappedCache.doRemove( key, keyHashCode );
    }

    @Override
    public V doGetOrFetch( K key, Fetcher<K, V> kvFetcher, int keyHashCode ) {
        return wrappedCache.doGetOrFetch( key, kvFetcher, keyHashCode );
    }
}
