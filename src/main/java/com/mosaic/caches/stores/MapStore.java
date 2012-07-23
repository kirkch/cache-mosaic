package com.mosaic.caches.stores;

import java.util.Map;

/**
 *
 */
public class MapStore<K,V> implements Store<K,V> {

    private Map<K, V> map;

    public MapStore( Map<K,V> map ) {
        this.map = map;
    }

    @Override
    public V get( K key, int keyHashCode ) {
        return map.get( key );
    }

    @Override
    public V put( K key, V newValue, int keyHashCode ) {
        return map.put( key, newValue );
    }

    @Override
    public V remove( K key, int keyHashCode ) {
        return map.remove( key );
    }

    @Override
    public int size() {
        return map.size();
    }
}
