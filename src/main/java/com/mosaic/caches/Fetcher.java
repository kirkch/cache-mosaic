package com.mosaic.caches;

/**
 *
 */
public interface Fetcher<K,V> {

    public V fetch( K key );

}
