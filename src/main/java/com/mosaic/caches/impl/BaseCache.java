package com.mosaic.caches.impl;

import com.mosaic.caches.Cache;
import com.mosaic.caches.Fetcher;

/**
 *
 */
public abstract class BaseCache<K,V> implements Cache<K,V> {

    private String cacheName;

    protected BaseCache( String cacheName ) {
        this.cacheName = cacheName;
    }


    @Override
    public String getCacheName() {
        return cacheName;
    }

    /**
     * Returns how many values are currently stored within the cache.
     */
    public abstract int size();

    public final V get( K key ) {
        return doGet( key, key.hashCode() );
    }

    /**
     * Place the specified mapping into the cache. Returns the old mapping that was just replaced.
     */
    public final V put( K key, V newValue ) {
        return doPut( key, newValue, key.hashCode() );
    }

    /**
     * Stores the specified mapping. Returns true if the write was successful, else false means that a mapping was
     * already in the cache and so it was not updated.
     */
    public final V putIfAbsent( K key, V newValue ) {
        return doPutIfAbsent( key, newValue, key.hashCode() );
    }


    public final V remove( K key ) {
        return doRemove( key, key.hashCode() );
    }

    public final V getOrFetch( K key, Fetcher<K,V> fetcher ) {
        return doGetOrFetch( key, fetcher, key.hashCode() );
    }



    public abstract V doGet( K key, int keyHashCode );
    public abstract V doPut( K key, V newValue, int keyHashCode );
    public abstract V doPutIfAbsent( K key, V newValue, int keyHashCode );
    public abstract V doRemove( K key, int keyHashCode );
    public abstract V doGetOrFetch( K key, Fetcher<K,V> fetcher, int keyHashCode );
}

