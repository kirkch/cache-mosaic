package com.mosaic.caches.decorators;

import com.mosaic.caches.Cache;
import com.mosaic.caches.Fetcher;
import com.mosaic.caches.impl.BaseCacheWrapper;

/**
 *
 */
public class SynchronizedCacheWrapper<K,V> extends BaseCacheWrapper<K,V> {

    public SynchronizedCacheWrapper( Cache<K, V> wrappedCache ) {
        super( wrappedCache );
    }

    @Override
    public synchronized int size() {
        return super.size();
    }

    @Override
    public synchronized V doGet( K key, int keyHashCode ) {
        return super.doGet( key, keyHashCode );
    }

    @Override
    public synchronized V doPut( K key, V newValue, int keyHashCode ) {
        return super.doPut( key, newValue, keyHashCode );
    }

    @Override
    public synchronized V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
        return super.doPutIfAbsent( key, newValue, keyHashCode );
    }

    @Override
    public synchronized V doRemove( K key, int keyHashCode ) {
        return super.doRemove( key, keyHashCode );
    }

    @Override
    public synchronized V doGetOrFetch( K key, Fetcher<K, V> kvFetcher, int keyHashCode ) {
        return super.doGetOrFetch( key, kvFetcher, keyHashCode );
    }
}
