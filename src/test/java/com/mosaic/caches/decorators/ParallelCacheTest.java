package com.mosaic.caches.decorators;

import com.mosaic.caches.Cache;
import com.mosaic.caches.CacheFactory;
import com.mosaic.caches.Factory;
import com.mosaic.caches.impl.BasicCacheTestCases;

/**
 *
 */
public class ParallelCacheTest extends BasicCacheTestCases {
    public ParallelCacheTest() {
        super( new ParallelCache<String, Integer>("junit", 3, new MyCacheFactory()) );
    }

    private static class MyCacheFactory implements Factory<Cache> {
        @Override
        public Cache create() {
            return CacheFactory.singleThreadedInlineHashMapCache("c", String.class, Integer.class);
        }
    }
}
