package com.mosaic.caches.impl;

import com.mosaic.caches.CacheFactory;

/**
 *
 */
public class ReadWriteCacheTests extends BasicCacheTestCases {

    public ReadWriteCacheTests() {
        super( CacheFactory.readWriteInlineMapCache("junit", String.class, Integer.class) );
    }

}
