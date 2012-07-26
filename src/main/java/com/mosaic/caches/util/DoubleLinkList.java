package com.mosaic.caches.util;

/**
 * An implementation of linked list that allows access to its internal nodes. This allows efficient remove of a node
 * after it has been located, without the need to rescan the list.
 */
@SuppressWarnings("unchecked")
public class DoubleLinkList<N extends DoubleLinkList.Node> {
    private int size;

    private N head;
    private N tail;

    public DoubleLinkList() {}

    public DoubleLinkList( N...nodes) {
        for ( N n : nodes ) {
            insertTail( n );
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean hasContents() {
        return size > 0;
    }

    public N head() {
        return head;
    }

    public N tail() {
        return tail;
    }

    public DoubleLinkList<N> insertHead( N newNode ) {
        newNode.next = head;

        if ( head == null ) {
            tail = newNode;
        } else {
            head.prev = newNode;
        }

        newNode.setOwningList(this);

        head = newNode;
        size++;

        return this;
    }

    public DoubleLinkList<N> insertTail( N newNode ) {
        newNode.prev = tail;

        if ( tail == null ) {
            head = newNode;
        } else {
            tail.next = newNode;
        }

        newNode.setOwningList(this);

        tail = newNode;
        size++;

        return this;
    }

    public N popHead() {
        if ( head == null ) {
            return null;
        }

        N node = head;
        node.detachNode();

        return node;
    }

    public N popTail() {
        if ( tail == null ) {
            return null;
        }

        N node = tail;
        node.detachNode();

        return node;
    }

    /**
     * Designed to be extended. Offers chance to store more data within the lists data structure without encurring extra
     * object allocations.
     */
    public static abstract class Node<V,SELF extends Node> {
        private SELF next;
        private SELF prev;
        private V    value;

        private DoubleLinkList<SELF> owningList;

        public Node( V v ) {
            this.value = v;
        }

        public V getValue() {
            return value;
        }

        public SELF nextNode() {
            return next;
        }

        public SELF previousNode() {
            return prev;
        }

        public boolean detachNode() {
            if ( owningList == null ) {
                return false;
            }

            if ( next != null ) {
                next.prev = this.prev;
            }

            if ( prev != null ) {
                prev.next = this.next;
            }

            if ( owningList.head == this ) {
                owningList.head = next;
            }

            if ( owningList.tail == this ) {
                owningList.tail = prev;
            }

            owningList.size--;
            owningList = null;
            next       = null;
            prev       = null;

            return true;
        }

        public void insertNext( SELF newNeighbour ) {
            newNeighbour.errorIfAlreadyOwned();

            if ( next == null ) {
                assert owningList.tail == this;

                owningList.tail = newNeighbour;
            } else {
                next.prev = newNeighbour;
                newNeighbour.next = next;
            }

            newNeighbour.prev = this;
            this.next = newNeighbour;

            newNeighbour.setOwningList( owningList );

            owningList.size++;
        }

        public void insertPrevious( SELF newNeighbour ) {
            newNeighbour.errorIfAlreadyOwned();

            if ( prev == null ) {
                assert owningList.head == this;

                owningList.head = newNeighbour;
            } else {
                prev.next = newNeighbour;
                newNeighbour.prev = prev;
            }

            newNeighbour.next = this;
            this.prev = newNeighbour;

            newNeighbour.setOwningList( owningList );

            owningList.size++;
        }

        public boolean shiftTowardsHead() {
            if ( prev == null ) {
                return false;
            }

            SELF nodeToJumpOver = prev;

            detachNode();
            nodeToJumpOver.insertPrevious( this );

            return true;
        }

        public boolean shiftTowardsTail() {
            if ( next == null ) {
                return false;
            }

            SELF nodeToJumpOver = next;

            detachNode();
            nodeToJumpOver.insertNext( this );

            return true;
        }

        private void setOwningList( DoubleLinkList<SELF> owningList ) {
            this.owningList = owningList;
        }

        private void errorIfAlreadyOwned() {
            if ( owningList != null ) {
                throw new IllegalStateException( "already member of a list" );
            }
        }
    }
}
