package com.mosaic.caches.decorators;

import com.mosaic.caches.Cache;
import com.mosaic.caches.Fetcher;
import com.mosaic.caches.impl.BaseCache;

/**
 *
 */
public class SynchronizedCacheWrapper<K,V> extends BaseCache<K,V> {

    private Cache<K, V> wrappedCache;

    public SynchronizedCacheWrapper( Cache<K, V> wrappedCache ) {
        super( wrappedCache.getCacheName() );

        this.wrappedCache = wrappedCache;
    }

    @Override
    public synchronized int size() {
        return wrappedCache.size();
    }

    @Override
    public synchronized V doGet( K key, int keyHashCode ) {
        return wrappedCache.doGet( key, keyHashCode );
    }

    @Override
    public synchronized V doPut( K key, V newValue, int keyHashCode ) {
        return wrappedCache.doPut( key, newValue, keyHashCode );
    }

    @Override
    public synchronized V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
        return wrappedCache.doPutIfAbsent( key, newValue, keyHashCode );
    }

    @Override
    public synchronized V doRemove( K key, int keyHashCode ) {
        return wrappedCache.doRemove( key, keyHashCode );
    }

    @Override
    public synchronized V doGetOrFetch( K key, Fetcher<K, V> kvFetcher, int keyHashCode ) {
        return wrappedCache.doGetOrFetch( key, kvFetcher, keyHashCode );
    }
}
