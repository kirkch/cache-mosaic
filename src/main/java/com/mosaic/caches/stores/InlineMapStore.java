package com.mosaic.caches.stores;

import static com.mosaic.caches.stores.StoreUtils.roundUpToClosestPowerOf2;

/**
 *
 */
public class InlineMapStore<K,V> implements Store<K,V>{
    private Element<K,V>[] map;

    private int currentSize;
    private int bitMask;
    private int maxSizeBeforeResizing;

    private final int    reservationShift;
    private final double loadFactor;

    public InlineMapStore() {
        this(10);
    }

    public InlineMapStore( int initialMapSize ) {
        this( initialMapSize, 0.25, 1 );
    }

    public InlineMapStore( int initialMapSize, double loadFactor, int reservationShift ) {
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
    public V get( K key, int keyHashCode ) {
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
    public V put( K key, V newValue, int keyHashCode ) {
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
    public V remove( K key, int keyHashCode ) {
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
