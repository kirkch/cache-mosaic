package com.mosaic.caches;

import static com.mosaic.caches.CacheUtils.roundUpToClosestPowerOf2;

/**
 * Experiment in getting a faster cache that java.util.HashMap. Conclusion so far: Marginally faster than java.util.HashMap
 * (1-2ms over 100,000 get/putIfAbsent calls) but is much more prone to GC interference.
 *
 * This implementation is not recommended. But it remains as record of what has been tried and in case of inspiration of
 * how to tune it further.
 */
@SuppressWarnings("unchecked")
public class CacheCustomHashMap<K,V> extends Cache<K,V> {

    private InternalLinkedList<K,V>[] map;
    private int                       bitmask;

    private int currentSize;
    private int maxSizeBeforeResizing;

    public CacheCustomHashMap() {
        this(16);
    }

    public CacheCustomHashMap( int initialMapSize ) {
        int mapSize = roundUpToClosestPowerOf2( initialMapSize );

        initNewMap( mapSize );
    }

    private void initNewMap( int mapSize ) {
        currentSize = 0;

        map = new InternalLinkedList[mapSize];
        for ( int i=0; i<mapSize; i++ ) {
            map[i] = new InternalLinkedList<K,V>();
        }

        bitmask = mapSize-1;

        maxSizeBeforeResizing = (mapSize*9)/10;
    }

    protected V doGet( K key, int keyHashCode ) {
        int i = toIndex( keyHashCode );

        return map[i].get( key, keyHashCode );
    }

    protected V doPut( K key, V newValue, int keyHashCode ) {
        int i = toIndex( keyHashCode );

        return map[i].put( key, newValue, keyHashCode );
    }

    protected V doPutIfAbsent( K key, V newValue, int keyHashCode ) {
        int i = toIndex( keyHashCode );

        return map[i].putIfAbsent(key, newValue, keyHashCode);
    }

    protected V doRemove( K key, int keyHashCode ) {
        int i = toIndex( keyHashCode );

        return map[i].remove(key, keyHashCode);
    }

    protected V doGetOrFetch( K key, Fetcher<K,V> fetcher, int keyHashCode ) {
        int i = toIndex( keyHashCode );

        return map[i].doGetOrFetch(key, fetcher, keyHashCode);
    }

    private int toIndex( int hashCode ) {
        return hashCode & bitmask;
    }

    private void elementAdded() {
        currentSize++;

        if ( currentSize > maxSizeBeforeResizing ) {
            resize();
        }
    }


    private void resize() {
        InternalLinkedList<K,V>[] oldMap = map;

        initNewMap( oldMap.length << 1 );

        for ( InternalLinkedList<K,V> list : oldMap ) {
            Element<K,V> e = list.head;

            while ( e != null ) {
                Element<K,V> next = e.next;

                reinsertAfterResize( e );

                e = next;
            }
        }
    }

    private void reinsertAfterResize( Element<K, V> e ) {
        int i = toIndex( e.keyHashCode );

        map[i].reinsertAfterResize( e );
    }


    private class InternalLinkedList<K,V> {

        private Element<K,V> head;

        public V get( K key, int keyHashCode ) {
            Element<K,V> e = scanFor( key, keyHashCode );

            if ( e == null ) {
                return null;
            }

            return e.value;
        }

        public V put( K key, V newValue, int keyHashCode ) {
            Element<K,V> e = scanFor( key, keyHashCode );

            if ( e == null ) {
                e = new Element( key, newValue, keyHashCode );
                e.next = head;

                if ( head != null ) {
                    head.prev = e;
                }

                head = e;

                elementAdded();

                return null;
            } else {
                V oldValue = e.value;

                e.value = newValue;

                return oldValue;
            }
        }

        private void reinsertAfterResize( Element<K, V> e ) {
            e.next = head;
            e.prev = null;

            if ( head != null ) {
                head.prev = e;
            }

            head = e;
        }

        public V putIfAbsent( K key, V newValue, int keyHashCode ) {
            Element<K,V> e = scanFor( key, keyHashCode );

            if ( e == null ) {
                e = new Element( key, newValue, keyHashCode );
                e.next = head;

                if ( head != null ) {
                    head.prev = e;
                }

                head = e;

                elementAdded();

                return null;
            } else {
                return e.value;
            }
        }

        public V remove( K key, int keyHashCode ) {
            Element<K,V> e = scanFor( key, keyHashCode );

            if ( e == null ) {
                return null;
            } else {
                if ( e == head ) {
                    head = head.next;

                    if ( head != null ) {
                        head.prev = null;
                    }
                } else {
                    e.detach();
                }

                elementAdded();

                return e.value;
            }
        }

        public V doGetOrFetch( K key, Fetcher<K, V> fetcher, int keyHashCode ) {
            Element<K,V> e = scanFor( key, keyHashCode );

            if ( e == null ) {
                V newValue = fetcher.fetch( key );

                put( key, newValue, keyHashCode );

                return newValue;
            }

            return e.value;
        }

        private Element<K,V> scanFor( K key, int keyHashCode ) {
            Element<K,V> e = head;

            while ( e != null ) {
                // NB perf tests showed that because collisions are relatively rare, comparing hashcode first is
                // wasted overhead and does not speed up the search  (apx 2ms faster in benchmark comparing the key only)
                // naturally this relies on the implementation of the equals method being fast
                if ( /*e.keyHashCode == keyHashCode && */e.key.equals(key) ) {
                    return e;
                }

                e = e.next;
            }

            return null;
        }
    }

    private static class Element<K,V> {
        public final int keyHashCode;
        public final K   key;
        public       V   value;

        public Element<K,V> next;
        public Element<K,V> prev;

        private Element( K key, V value, int keyHashCode ) {
            this.keyHashCode = keyHashCode;
            this.key         = key;
            this.value       = value;
        }

        public void detach() {
            if ( next != null ) {
                next.prev = prev;
            }

            if ( prev != null ) {
                prev.next = next;
            }
        }
    }
}
