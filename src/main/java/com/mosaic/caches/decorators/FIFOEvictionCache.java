package com.mosaic.caches.decorators;

import com.mosaic.caches.Cache;
import com.mosaic.caches.Fetcher;

/**
 * Last In First Out cache. Keeps hold of all values until a max size is reached, at which time it evicts entries
 * newest first.
 */
public class FIFOEvictionCache<K,V> extends BaseEvicitionCache<K,V> {

    public FIFOEvictionCache( Cache<K, V> wrappedCache, int maxCacheSize ) {
        super( wrappedCache.getCacheName(), wrappedCache, maxCacheSize );
    }


    @Override
    public V doGet( K key, int keyHashCode ) {
        EvictionNode<K,V> evictionRecord = underlyingCache.doGet( key, keyHashCode );

        return evictionRecord == null ? null : evictionRecord.value;
    }

    @Override
    public V doPut( K key, V newValue, int keyHashCode ) {
        EvictionNode<K,V> currentEvictionRecord = underlyingCache.doGet( key, keyHashCode );

        if ( currentEvictionRecord == null ) {
            return writeToHead( key, newValue, keyHashCode );
        } else {
            V oldValue = currentEvictionRecord.value;

            currentEvictionRecord.value = newValue;

            return oldValue;
        }
    }

    @Override
    public V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
        EvictionNode<K,V> currentEvictionRecord = underlyingCache.doGet( key, keyHashCode );

        if ( currentEvictionRecord == null ) {
            return writeToHead( key, newValue, keyHashCode );
        } else {
            return currentEvictionRecord.value;
        }
    }

    @Override
    public V doGetOrFetch( K key, Fetcher<K, V> kvFetcher, int keyHashCode ) {
        EvictionNode<K,V> currentEvictionRecord = underlyingCache.doGet( key, keyHashCode );

        if ( currentEvictionRecord == null ) {
            V newValue = kvFetcher.fetch( key );

            writeToHead( key, newValue, keyHashCode );

            return newValue;
        }

        return currentEvictionRecord.value;
    }

    protected void trimCacheToSize( int targetSize ) {
        trimFromTail( targetSize );
    }
}
