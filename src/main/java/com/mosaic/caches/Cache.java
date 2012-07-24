package com.mosaic.caches;

/**
 *
 */
public interface Cache<K,V> {


    public String getCacheName();

    /**
     * Returns how many values are currently stored within the cache.
     */
    public abstract int size();

    public V get( K key );

    /**
     * Place the specified mapping into the cache. Returns the old mapping that was just replaced.
     */
    public V put( K key, V newValue );

    /**
     * Stores the specified mapping. Returns true if the write was successful, else false means that a mapping was
     * already in the cache and so it was not updated.
     */
    public V putIfAbsent( K key, V newValue );

    public V remove( K key );

    public V getOrFetch( K key, Fetcher<K,V> fetcher );



    public abstract V doGet( K key, int keyHashCode );
    public abstract V doPut( K key, V newValue, int keyHashCode );
    public abstract V doPutIfAbsent( K key, V newValue, int keyHashCode );
    public abstract V doRemove( K key, int keyHashCode );
    public abstract V doGetOrFetch( K key, Fetcher<K,V> fetcher, int keyHashCode );
}
