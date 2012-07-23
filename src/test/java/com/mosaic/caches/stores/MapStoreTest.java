package com.mosaic.caches.stores;

import java.util.HashMap;

/**
 *
 */
public class MapStoreTest extends BaseStoreTestCases {

    public MapStoreTest() {
        super( new MapStore(new HashMap()) );
    }

}
