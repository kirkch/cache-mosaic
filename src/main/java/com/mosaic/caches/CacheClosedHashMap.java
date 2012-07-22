package com.mosaic.caches;

import static com.mosaic.caches.CacheUtils.roundUpToClosestPowerOf2;

/**
 * Roughly twice as fast as java.util.HashMap, but uses four times the amount of memory. Best to use this cache
 * for small fixed size caches that need to be very very fast. <p/>
 *
 * The most robust (in terms of GC impact on performance) and flexible cache is to use CacheMap (backed by java.util.HashMap),
 * however if one profiles their use case the closed hashing algorithm is very fast.
 */
@SuppressWarnings("unchecked")
public class CacheClosedHashMap<K,V> extends Cache<K,V> {

    private Element<K,V>[] map;

    private int currentSize;
    private int bitMask;
    private int maxSizeBeforeResizing;

    private final int    reservationShift;
    private final double loadFactor;

    public CacheClosedHashMap() {
        this(10);
    }

    public CacheClosedHashMap( int initialMapSize ) {
        this( initialMapSize, 0.25, 1 );
    }

    public CacheClosedHashMap( int initialMapSize, double loadFactor, int reservationShift ) {
        this.reservationShift = reservationShift;
        this.loadFactor       = loadFactor;

        int mapSize = roundUpToClosestPowerOf2( initialMapSize );

        initNewMap( mapSize );
    }

    private void initNewMap( int mapSize ) {
        currentSize = 0;

        map     = new Element[mapSize << reservationShift];
        bitMask = mapSize-1;

        maxSizeBeforeResizing = (int) (mapSize*loadFactor);
    }


    @Override
    protected V doGet( K key, int keyHashCode ) {
        int i = toIndex( keyHashCode );
        while ( true ) {
            Element<K,V> e = map[i];

            if ( e == null ) {
                return null;
            } else if ( e.keyHashCode == keyHashCode && e.key.equals(key) ) {
                return e.value;
            }

            i = (i + 1) & bitMask;
        }
    }

    @Override
    protected V doPut( K key, V newValue, int keyHashCode ) {
        int attemptCount = 0;

        while ( true ) {
            int          i = toIndex( keyHashCode+attemptCount );
            Element<K,V> e = map[i];

            if ( e == null ) {
                Element newElement = new Element( key, newValue, keyHashCode );

                map[i] = newElement;
                newElementAdded();

                return null;
            } else if ( e.keyHashCode == hashCode() && e.key.equals(key) ) {
                V oldValue = e.value;

                e.value = newValue;

                return oldValue;
            }

            attemptCount++;
        }
    }

    @Override
    protected V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
        int i = toIndex( keyHashCode );
        while ( true ) {
            Element<K,V> e = map[i];

            if ( e == null ) {
                Element newElement = new Element( key, newValue, keyHashCode );

                map[i] = newElement;
                newElementAdded();

                return null;
            } else if ( e.keyHashCode == keyHashCode && e.key.equals(key) ) {
                return e.value;
            }

            i = (i + 1) & bitMask;
        }
    }

    @Override
    protected V doRemove( K key, int keyHashCode ) {
        //todo
        return null;
    }

    @Override
    protected V doGetOrFetch( K key, Fetcher<K, V> kvFetcher, int keyHashCode ) {
        //todo
        return null;
    }

    private int toIndex( int hashCode ) {
        return (hashCode & bitMask) << reservationShift;
    }

    private void newElementAdded() {
        currentSize++;

        if ( currentSize > maxSizeBeforeResizing ) {
            Element<K,V>[] oldMap = map;

            initNewMap( oldMap.length << 2 );

            outer:
            for ( Element e : oldMap ) {
                if ( e != null ) {
                    int keyHashCode = e.key.hashCode();
                    int i = toIndex( keyHashCode );
                    while ( true ) {

                        if ( map[i] == null ) {
                            map[i] = e;
                            continue outer;
                        }

                        i = (i + 1) & bitMask;
                    }
                }
            }
        }
    }

    private static class Element<K,V> {
        private final K key;
        private       V value;
        private final int keyHashCode;

        public Element( K key, V value, int keyHashCode ) {
            this.key         = key;
            this.value       = value;
            this.keyHashCode = keyHashCode;
        }
    }
}
