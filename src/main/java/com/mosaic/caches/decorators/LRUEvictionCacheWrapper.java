package com.mosaic.caches.decorators;

import com.mosaic.caches.Cache;
import com.mosaic.caches.Fetcher;

/**
 *
 */
@SuppressWarnings("unchecked")
public class LRUEvictionCacheWrapper<K,V> extends Cache<K,V> {

    private final Cache<K,EvictionNode<K,V>> underlyingCache;
    private final int                        maxCacheSize;

    private EvictionNode<K,V> insertEnd;   // head
    private EvictionNode<K,V> deletionEnd; // tail


    public LRUEvictionCacheWrapper( Cache<K, V> wrappedCache, int maxCacheSize ) {
        this.maxCacheSize = maxCacheSize;
        underlyingCache = (Cache<K, EvictionNode<K,V>>) wrappedCache;
    }

    @Override
    public int size() {
        return underlyingCache.size();
    }

    @Override
    public V doGet( K key, int keyHashCode ) {
        EvictionNode<K,V> evictionRecord = underlyingCache.doGet( key, keyHashCode );

        if ( evictionRecord == null ) {
            return null;
        }

        moveEvictionRecordToHead( evictionRecord );

        return evictionRecord.value;
    }

    @Override
    public V doPut( K key, V newValue, int keyHashCode ) {
        EvictionNode<K,V> currentEvictionRecord = underlyingCache.doGet( key, keyHashCode );

        if ( currentEvictionRecord == null ) {
            return insertNewEntry( key, newValue, keyHashCode );
        } else {
            moveEvictionRecordToHead( currentEvictionRecord );

            V oldValue = currentEvictionRecord.value;
            currentEvictionRecord.value = newValue;

            return oldValue;
        }
    }

    @Override
    public V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
        EvictionNode<K,V> currentEvictionRecord = underlyingCache.doGet( key, keyHashCode );

        if ( currentEvictionRecord == null ) {
            return insertNewEntry( key, newValue, keyHashCode );
        } else {
            return currentEvictionRecord.value;
        }
    }

    private V insertNewEntry( K key, V newValue, int keyHashCode ) {
        EvictionNode<K,V> newEvictionRecord = new EvictionNode<K,V>( key, newValue );

        if ( insertEnd == null ) {
            insertEnd   = newEvictionRecord;
            deletionEnd = newEvictionRecord;
        } else {
            newEvictionRecord.next = insertEnd;

            insertEnd.prev = newEvictionRecord;
            insertEnd      = newEvictionRecord;
        }

        underlyingCache.doPut( key, newEvictionRecord, keyHashCode );

        while ( underlyingCache.size() > maxCacheSize ) {
            underlyingCache.remove( deletionEnd.key );

            deletionEnd.prev.next = null;
        }

        return null;
    }

    @Override
    public V doRemove( K key, int keyHashCode ) {
        EvictionNode<K,V> currentEvictionRecord = underlyingCache.doGet( key, keyHashCode );

        if ( currentEvictionRecord == null ) {
            return null;
        }

        currentEvictionRecord.detach();
        underlyingCache.remove( key );

        return currentEvictionRecord.value;
    }

    @Override
    public V doGetOrFetch( K key, Fetcher<K, V> kvFetcher, int keyHashCode ) {
        EvictionNode<K,V> currentEvictionRecord = underlyingCache.doGet( key, keyHashCode );

        if ( currentEvictionRecord == null ) {
            V newValue = kvFetcher.fetch( key );

            insertNewEntry( key, newValue, keyHashCode );

            return newValue;
        }

        return currentEvictionRecord.value;
    }

    private void moveEvictionRecordToHead( EvictionNode evictionRecord ) {
        if ( evictionRecord == insertEnd ) {
            return;
        }

        if ( evictionRecord == deletionEnd ) {
            assert evictionRecord.prev != null;

            deletionEnd = evictionRecord.prev;
        }

        evictionRecord.detach();

        evictionRecord.next = insertEnd;
        insertEnd.prev = evictionRecord;
    }


    private static class EvictionNode<K,V> {
        private EvictionNode<K,V> next;
        private EvictionNode<K,V> prev;

        private final K key;
        private       V value;

        public EvictionNode( K key, V value) {
            this.key   = key;
            this.value = value;
        }

        public void detach() {
            EvictionNode<K,V> oldNext = next;

            if ( oldNext != null ) {
                next.prev = this.prev;

                this.next = null;
            }

            if ( this.prev != null ) {
                this.prev.next = oldNext;

                this.prev = null;
            }
        }
    }
}
