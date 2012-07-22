package com.mosaic.caches;

/**
 *
 */
public abstract class Cache<K,V> {

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



    protected abstract V doGet( K key, int keyHashCode );
    protected abstract V doPut( K key, V newValue, int keyHashCode );
    protected abstract V doPutIfAbsent( K key, V newValue, int keyHashCode );
    protected abstract V doRemove( K key, int keyHashCode );
    protected abstract V doGetOrFetch( K key, Fetcher<K,V> fetcher, int keyHashCode );
}
