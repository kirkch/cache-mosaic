package com.mosaic.caches.stores;

/**
 *
 */
public interface Store<K,V> {

    public V get( K key, int keyHashCode );

    public V put( K key, V newValue, int keyHashCode );

    public V remove( K key, int keyHashCode );

    public int size();
}
