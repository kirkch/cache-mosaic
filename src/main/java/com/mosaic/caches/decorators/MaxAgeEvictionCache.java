package com.mosaic.caches.decorators;

import com.mosaic.caches.Cache;
import com.mosaic.caches.Fetcher;
import com.mosaic.caches.impl.BaseCache;
import com.mosaic.caches.util.HashWheel;

/**
 * Evict entries that have not been accessed for a specified amount of time.
 */
@SuppressWarnings("unchecked")
public class MaxAgeEvictionCache<K,V> extends BaseCache<K,V> {

    private Cache<K, Node<V>> underlyingCache;
    private int               ttlMillis;
    private HashWheel         hashWheel;

    public MaxAgeEvictionCache( Cache underlyingCache, int ttlMillis ) {
        this( underlyingCache, ttlMillis, new HashWheel() );
    }

    public MaxAgeEvictionCache( Cache underlyingCache, int ttlMillis, HashWheel hashWheel ) {
        super( underlyingCache.getCacheName() );

        this.underlyingCache = underlyingCache;
        this.ttlMillis       = ttlMillis;
        this.hashWheel       = hashWheel;
    }

    @Override
    public synchronized int size() {
        return underlyingCache.size();
    }

    @Override
    public synchronized V doGet( K key, int keyHashCode ) {
        hashWheel.applyBookKeeping( System.currentTimeMillis() );

        return nodeToValue( underlyingCache.doGet( key, keyHashCode ) );
    }

    @Override
    public synchronized V doPut( final K key, V newValue, final int keyHashCode ) {
        hashWheel.applyBookKeeping( System.currentTimeMillis() );

        Node<V> node = createInternalNode( key, keyHashCode, newValue );

        return nodeToValue( underlyingCache.doPut( key, node, keyHashCode ) );
    }

    @Override
    public synchronized V doPutIfAbsent( final K key, V newValue, final int keyHashCode ) {
        hashWheel.applyBookKeeping( System.currentTimeMillis() );

        Node<V> node = createInternalNode( key, keyHashCode, newValue );

        Node<V> activeNode = underlyingCache.doPutIfAbsent( key, node, keyHashCode );
        if ( activeNode != node ) {
            node.hashWheelTicket.cancel();
        }

        return nodeToValue( activeNode );
    }

    @Override
    public synchronized V doRemove( K key, int keyHashCode ) {
        hashWheel.applyBookKeeping( System.currentTimeMillis() );

        Node<V> node = underlyingCache.doRemove( key, keyHashCode );
        if ( node == null ) {
            return null;
        }

        node.hashWheelTicket.cancel();

        return node.value;
    }

    @Override
    public synchronized V doGetOrFetch( final K key, Fetcher<K, V> kvFetcher, final int keyHashCode ) {
        hashWheel.applyBookKeeping( System.currentTimeMillis() );

        Node<V> node = underlyingCache.doGet( key, keyHashCode );
        if ( node == null ) {
            V newValue = kvFetcher.fetch( key );

            node = createInternalNode( key, keyHashCode, newValue );

            underlyingCache.doPut( key, node, keyHashCode );
        }

        return node.value;
    }

    private Node createInternalNode( final K key, final int keyHashCode, V newValue ) {
        return new Node( newValue, hashWheel.register( System.currentTimeMillis()+ttlMillis, new Runnable() {
            public void run() {
                underlyingCache.doRemove( key, keyHashCode );
            }
        }));
    }

    private V nodeToValue( Node<V> node ) {
        if ( node == null ) {
            return null;
        }

        return node.value;
    }

    private static class Node<V> {
        private V value;
        private HashWheel.Ticket hashWheelTicket;

        public Node( V value, HashWheel.Ticket hashWheelTicket ) {
            this.value           = value;
            this.hashWheelTicket = hashWheelTicket;
        }
    }

}
