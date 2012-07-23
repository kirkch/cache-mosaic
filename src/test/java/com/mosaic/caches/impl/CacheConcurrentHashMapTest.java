package com.mosaic.caches.impl;

import com.mosaic.caches.stores.MapStore;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class CacheConcurrentHashMapTest extends BasicCacheTestCases {

    public CacheConcurrentHashMapTest() {
        super( new DefaultCache<String, Integer>("test-cache", new MapStore(new ConcurrentHashMap<String,Integer>())) );
    }

}
