package com.mosaic.caches.stores;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
@SuppressWarnings("UnnecessaryBoxing")
public abstract class BaseStoreTestCases {

    private Store<String, Integer> store;

    public BaseStoreTestCases( Store<String,Integer> store ) {
        this.store = store;
    }

    @Test
    public void givenEmptyStore_retrieveValue_expectNull() {
        assertEquals( null, store.get("foo", "foo".hashCode()) );
    }

    @Test
    public void givenEmptyStore_storeValueAndRetrieveIt_expectValueBack() {
        store.put( "foo", new Integer(20), "foo".hashCode() );

        assertEquals( new Integer(20), store.get("foo", "foo".hashCode()) );
    }

    @Test
    public void givenEmptyStore_removeNoneExistingValue_expectNull() {
        assertEquals( null, store.remove( "foo", "foo".hashCode() ) );
    }

    @Test
    public void givenStoreWithValue_removeExistingValue_expectValue() {
        store.put( "foo", new Integer(20), "foo".hashCode() );

        assertEquals( new Integer(20), store.remove( "foo", "foo".hashCode() ) );
    }

    @Test
    public void givenStoreWithValue_removeExistingValue_expectValueToNoLongerBeRetrievable() {
        store.put( "foo", new Integer(20), "foo".hashCode() );

        store.remove( "foo", "foo".hashCode() );

        assertEquals( null, store.get( "foo", "foo".hashCode() ) );
    }

    @Test
    public void givenStoreWithValue_getExistingValueTwice_expectValueToBeAlwaysRetrieved() {
        store.put( "foo", new Integer(20), "foo".hashCode() );

        store.get( "foo", "foo".hashCode() );

        assertEquals( new Integer( 20 ), store.get( "foo", "foo".hashCode() ) );
    }


    @Test
    public void emptyStore_callSize_expect0() {
        assertEquals( 0, store.size() );
    }

    @Test
    public void storeWithOneValue_callSize_expect1() {
        store.put( "foo", new Integer(20), "foo".hashCode() );

        assertEquals( 1, store.size() );
    }

    @Test
    public void storeWithTwoValues_callSize_expect1() {
        store.put( "foo", new Integer(20), "foo".hashCode() );
        store.put( "bar", new Integer(2), "bar".hashCode() );

        assertEquals( 2, store.size() );
    }

    @Test
    public void overwriteValue_expectSizeToRemainAtOne() {
        store.put( "foo", new Integer(20), "foo".hashCode() );
        store.put( "foo", new Integer(2), "foo".hashCode() );

        assertEquals( 1, store.size() );
    }

    @Test
    public void overwriteValue_expectValueToChange() {
        store.put( "foo", new Integer(20), "foo".hashCode() );
        store.put( "foo", new Integer(2), "foo".hashCode() );

        assertEquals( new Integer(2), store.get( "foo", "foo".hashCode() ) );
    }


    @Test
    public void store20Values_removeSubset_thenRetrieve() {
        for ( int i=0; i<100; i++ ) {
            store.put( Long.toString(i), i, Long.toString(i).hashCode() );
            if ( i%3 == 0 ) {
                store.remove( Long.toString(i-1), Long.toString(i-1).hashCode() );
            }
        }

        for ( int i=0; i<100; i++ ) {
            if ( (i+1)%3 != 0 ) {
                assertEquals( new Integer(i), store.get(Long.toString(i), Long.toString(i).hashCode()) );
            }
        }
    }
}
