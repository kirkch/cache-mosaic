package com.mosaic.caches.decorators;

import com.mosaic.caches.Cache;
import com.mosaic.caches.impl.BaseCache;

/**
 *
 */
@SuppressWarnings("unchecked")
abstract class BaseEvicitionCache<K,V> extends BaseCache<K,V> {

    protected final Cache<K,EvictionNode<K,V>> underlyingCache;
    protected final int                        maxCacheSize;

    private EvictionNode<K,V> head;
    private EvictionNode<K,V> tail;


    protected BaseEvicitionCache( String cacheName, Cache underlyingCache, int maxCacheSize ) {
        super( cacheName );

        this.underlyingCache = underlyingCache;
        this.maxCacheSize    = maxCacheSize;
    }


    @Override
    public int size() {
        return underlyingCache.size();
    }

    @Override
    public V doRemove( K key, int keyHashCode ) {
        EvictionNode<K,V> currentEvictionRecord = underlyingCache.doGet( key, keyHashCode );

        return removeNode( currentEvictionRecord );
    }



    protected V writeToHead( K key, V newValue, int keyHashCode ) {
        EvictionNode<K,V> newEvictionRecord = new EvictionNode<K,V>( key, newValue );

        if ( head == null ) {
            head = newEvictionRecord;
            tail = newEvictionRecord;
        } else {
            trimCacheToSize( maxCacheSize-1 );

            newEvictionRecord.next = head;

            head.prev = newEvictionRecord;
            head = newEvictionRecord;
        }

        underlyingCache.doPut( key, newEvictionRecord, keyHashCode );

        return null;
    }

    protected abstract void trimCacheToSize( int targetSize );

    protected void trimFromHead( int targetSize ) {
        int numIterations = underlyingCache.size() - targetSize;

        while ( numIterations > 0 ) {
            EvictionNode<K,V> n = underlyingCache.remove( head.key );
            assert n == head;

            head = head.next;
            head.prev = null;

            numIterations--;
        }
    }

    protected void trimFromTail( int targetSize ) {
        int numIterations = underlyingCache.size() - targetSize;

        while ( numIterations > 0 ) {
            EvictionNode<K,V> n = underlyingCache.remove( tail.key );
            assert n == tail;

            tail = tail.prev;
            tail.next = null;

            numIterations--;
        }
    }

    protected void moveEvictionRecordToHead( EvictionNode evictionRecord ) {
        if ( evictionRecord == head ) {
            return;
        }

        if ( evictionRecord == tail ) {
            assert evictionRecord.prev != null;

            tail = evictionRecord.prev;
        }

        evictionRecord.detach();

        evictionRecord.next = head;
        head.prev = evictionRecord;

        head = evictionRecord;
    }

    protected V removeNode( EvictionNode<K,V> node ) {
        if ( node == null ) {
            return null;
        }

        if ( head == node ) {
            head = head.next;
        }

        if ( tail == node ) {
            tail = tail.prev;
        }

        node.detach();

        underlyingCache.remove( node.key );

        return node.value;
    }



    protected static class EvictionNode<K,V> {
        private EvictionNode<K,V> next;
        private EvictionNode<K,V> prev;

        protected final K key;
        protected       V value;

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
