package com.mosaic.caches.impl;

import com.mosaic.caches.Cache;
import com.mosaic.caches.Fetcher;

import static com.mosaic.caches.impl.CacheUtils.roundUpToClosestPowerOf2;

/**
 * Roughly twice as fast as java.util.HashMap, but uses two times the amount of memory when empty. Best to use this cache
 * for small fixed size caches that need to be very very fast. <p/>
 *
 * The most robust (in terms of GC impact on performance) and flexible cache is to use CacheMap (backed by java.util.HashMap).<p/>
 *
 * This cache type is very sensitive to the characteristics of the hashing algorithm. In order to have some robustness
 * to clustering this implementation reserves one space in the hash array for a collision; at the expense of memory. This
 * optimisation works for many cases however no optimisation will save the closed map from clustering of keys around
 * a single hash code.<p/>
 *
 * It is thus strongly encouraged that you measure the performance of the cache against your use case before committing
 * to this cache. Otherwise fall back to CacheMap which is much more tolerant to clustering of hash values and thus has
 * much better worst case performance.<p/>
 *
 * This cache is not thread safe, but can be made thread safe by wrapping it with SynchronizedCacheWrapper or ReadWriteCacheWrapper.
 */
@SuppressWarnings("unchecked")
public class CacheInlineHashMap<K,V> extends Cache<K,V> {

    private Element<K,V>[] map;

    private int currentSize;
    private int bitMask;
    private int maxSizeBeforeResizing;

    private final int    reservationShift;
    private final double loadFactor;

    public CacheInlineHashMap() {
        this(10);
    }

    public CacheInlineHashMap( int initialMapSize ) {
        this( initialMapSize, 0.25, 1 );
    }

    public CacheInlineHashMap( int initialMapSize, double loadFactor, int reservationShift ) {
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

    public int size() {
        return currentSize;
    }

    @Override
    public V doGet( K key, int keyHashCode ) {
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
    public V doPut( K key, V newValue, int keyHashCode ) {
        int attemptCount = 0;

        while ( true ) {
            int          i = toIndex( keyHashCode+attemptCount );
            Element<K,V> e = map[i];

            if ( e == null ) {
                Element newElement = new Element( key, newValue, keyHashCode );

                map[i] = newElement;
                newElementAdded();

                return null;
            } else if ( e.keyHashCode == keyHashCode && e.key.equals(key) ) {
                V oldValue = e.value;

                e.value = newValue;

                return oldValue;
            }

            attemptCount++;
        }
    }

    @Override
    public V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
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
    public V doRemove( K key, int keyHashCode ) {
        int startOfChainIndex = toIndex( keyHashCode );
        int i = startOfChainIndex;
        while ( true ) {
            Element<K,V> e = map[i];

            if ( e == null ) {
                return null;
            } else if ( e.keyHashCode == keyHashCode && e.key.equals(key) ) {
                int endOfChainIndex = findEndOfChain(startOfChainIndex, i+1 );

                if ( endOfChainIndex == i ) {
                    map[i] = null;
                } else {
                    map[i] = map[endOfChainIndex];
                    map[endOfChainIndex] = null;
                }

                return e.value;
            }

            i = (i + 1) & bitMask;
        }
    }

    @Override
    public V doGetOrFetch( K key, Fetcher<K, V> kvFetcher, int keyHashCode ) {
        int i = toIndex( keyHashCode );
        while ( true ) {
            Element<K,V> e = map[i];

            if ( e == null ) {
                V       newValue   = kvFetcher.fetch( key );
                Element newElement = new Element( key, newValue, keyHashCode );

                map[i] = newElement;
                newElementAdded();

                return newValue;
            } else if ( e.keyHashCode == keyHashCode && e.key.equals(key) ) {
                return e.value;
            }

            i = (i + 1) & bitMask;
        }
    }


    private int findEndOfChain( int startOfChainIndex, int startingFrom ) {
        int i = startingFrom;

        while ( map[i] != null && toIndex(map[i].keyHashCode) == startOfChainIndex ) {
            i++;
        }

        return i-1;
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
