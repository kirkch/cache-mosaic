package com.mosaic.caches.decorators;

import com.mosaic.caches.Cache;
import com.mosaic.caches.Factory;
import com.mosaic.caches.Fetcher;
import com.mosaic.caches.impl.BaseCache;
import com.mosaic.caches.util.BitUtils;

/**
 *
 */
@SuppressWarnings("unchecked")
public class ParallelCache<K,V> extends BaseCache<K,V> {

    private Cache<K,V>[] stripes;
    private int          bitmask;

    public ParallelCache( String cacheName, int numStripes, Factory<Cache> cacheFactory ) {
        super( cacheName );

        int size = BitUtils.roundUpToClosestPowerOf2( numStripes );

        bitmask = size - 1;
        stripes = new Cache[size];

        for ( int i=0; i<size; i++ ) {
            stripes[i] = cacheFactory.create();
        }
    }

    @Override
    public int size() {
        int count = 0;

        for ( Cache c : stripes ) {
            count += c.size();
        }

        return count;
    }

    @Override
    public V doGet( K key, int keyHashCode ) {
        Cache<K,V> c = selectCache(keyHashCode);

        return c.doGet( key, keyHashCode );
    }

    @Override
    public V doPut( K key, V newValue, int keyHashCode ) {
        Cache<K,V> c = selectCache(keyHashCode);

        return c.doPut( key, newValue, keyHashCode );
    }

    @Override
    public V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
        Cache<K,V> c = selectCache(keyHashCode);

        return c.doPutIfAbsent( key, newValue, keyHashCode );
    }

    @Override
    public V doRemove( K key, int keyHashCode ) {
        Cache<K,V> c = selectCache(keyHashCode);

        return c.doRemove( key, keyHashCode );
    }

    @Override
    public V doGetOrFetch( K key, Fetcher<K, V> kvFetcher, int keyHashCode ) {
        Cache<K,V> c = selectCache(keyHashCode);

        return c.doGetOrFetch( key, kvFetcher, keyHashCode );
    }

    private Cache<K, V> selectCache( int keyHashCode ) {
        return stripes[ keyHashCode & bitmask ];
    }
}
