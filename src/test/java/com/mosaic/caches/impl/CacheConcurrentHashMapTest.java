package com.mosaic.caches.impl;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class CacheConcurrentHashMapTest extends BasicCacheTestCases {

    public CacheConcurrentHashMapTest() {
        super( new CacheMap<String, Integer>(new ConcurrentHashMap<String,Integer>()) );
    }

}
