package com.mosaic.caches.impl;

import com.mosaic.caches.Fetcher;
import com.mosaic.caches.stores.Store;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public class ReadWriteCache<K,V> extends BaseCache<K,V> {
    private ReentrantReadWriteLock           rwLock    = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock  readLock  = rwLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();


    private Store<K, V> underlyingStore;


    public ReadWriteCache( String cacheName, Store<K, V> underlyingStore ) {
        super( cacheName );

        this.underlyingStore = underlyingStore;
    }

    @Override
    public String getCacheName() {
        return super.getCacheName();
    }

    @Override
    public int size() {
        readLock.lock();

        try {
            return underlyingStore.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V doGet( K key, int keyHashCode ) {
        readLock.lock();

        try {
            return underlyingStore.get(key, keyHashCode);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V doPut( K key, V newValue, int keyHashCode ) {
        writeLock.lock();

        try {
            return underlyingStore.put( key, newValue, keyHashCode );
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
        V currentValue = doGet(key,keyHashCode);  // try with read lock first; majority of time this should be it

        if ( currentValue != null ) {
            return currentValue;
        }


        writeLock.lock();

        try {
            currentValue = underlyingStore.get(key, keyHashCode);   // retry once the write lock is established

            if ( currentValue != null ) {
                return currentValue;
            }

            return underlyingStore.put( key, newValue, keyHashCode );
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public V doRemove( K key, int keyHashCode ) {
        writeLock.lock();

        try {
            return underlyingStore.remove( key, keyHashCode );
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public V doGetOrFetch( K key, Fetcher<K, V> kvFetcher, int keyHashCode ) {
        V currentValue = doGet(key,keyHashCode);  // try with read lock first; majority of time this should be it

        if ( currentValue != null ) {
            return currentValue;
        }


        writeLock.lock();

        try {
            currentValue = underlyingStore.get(key, keyHashCode);   // retry once the write lock is established

            if ( currentValue != null ) {
                return currentValue;
            }

            V newValue = kvFetcher.fetch( key );
            underlyingStore.put( key, newValue, keyHashCode );

            return newValue;
        } finally {
            writeLock.unlock();
        }
    }
}
