package com.mosaic.caches.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class DoubleLinkedListTest {

    private DoubleLinkList<MyNode> list = new DoubleLinkList<MyNode>();

// actions on newly created, empty list

    @Test
    public void emptyList_callSize_expect0() {
        assertEquals( 0, list.size() );
    }

    @Test
    public void emptyList_callIsEmpty_expectTrue() {
        assertTrue( list.isEmpty() );
    }

    @Test
    public void emptyList_callHasContents_expectFalse() {
        assertFalse( list.hasContents() );
    }

    @Test
    public void emptyList_callHead_expectNull() {
        assertNull( list.head() );
    }

    @Test
    public void emptyList_callTail_expectNull() {
        assertNull( list.tail() );
    }

    @Test
    public void emptyList_callPopTail_expectNull() {
        assertNull( list.popTail() );
    }

    @Test
    public void emptyList_callPopHead_expectNull() {
        assertNull( list.popHead() );
    }

// actionsOnList with single element inserted to tail


    @Test
    public void emptyList_insertTail_callSize_expect1() {
        list.insertTail( new MyNode( "a" ) );

        assertEquals( 1, list.size() );
    }

    @Test
    public void emptyList_insertTail_callIsEmpty_expectFalse() {
        list.insertTail( new MyNode( "a" ) );

        assertFalse( list.isEmpty() );
    }

    @Test
    public void emptyList_insertTail_callHasContents_expectTrue() {
        list.insertTail( new MyNode( "a" ) );

        assertTrue( list.hasContents() );
    }

    @Test
    public void emptyList_insertTail_callHead_expectNewlyInsertedNode() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        assertTrue( newNode == list.head() );
    }

    @Test
    public void emptyList_insertTail_callTail_expectNewlyInsertedNode() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        assertTrue( newNode == list.tail() );
    }

    @Test
    public void emptyList_insertTail_callTail_callNextNode_expectNull() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        MyNode tail = list.tail();
        assertNull( tail.nextNode() );
    }

    @Test
    public void emptyList_insertTail_callTail_callPreviousNode_expectNull() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        MyNode tail = list.tail();
        assertNull( tail.previousNode() );
    }

    @Test
    public void emptyList_insertTail_callTail_callDetachOnNode_callSize_expect0() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        MyNode tail = list.tail();
        assertTrue( tail.detachNode() );

        assertEquals( 0, list.size() );
    }

    @Test
    public void emptyList_insertTail_callTail_callDetachOnNode_callIsEmpty_expectTrue() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        MyNode tail = list.tail();
        assertTrue( tail.detachNode() );

        assertTrue( list.isEmpty() );
    }

    @Test
    public void emptyList_insertTail_callTail_callDetachOnNode_callHasContents_expectFalse() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        MyNode tail = list.tail();
        assertTrue( tail.detachNode() );

        assertFalse( list.hasContents() );
    }

    @Test
    public void emptyList_insertTail_callTail_callShiftTowardsHeads_expectFalse() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        MyNode tail = list.tail();
        assertFalse( tail.shiftTowardsHead() );
    }

    @Test
    public void emptyList_insertTail_callTail_callShiftTowardsTail_expectFalse() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        MyNode tail = list.tail();
        assertFalse( tail.shiftTowardsTail() );
    }

    @Test
    public void emptyList_insertTail_callTail_callShiftTowardsTail_callHead_expectOriginalNodeRemains() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        MyNode tail = list.tail();
        tail.shiftTowardsTail();

        assertTrue( newNode == list.head() );
    }

    @Test
    public void emptyList_insertTail_callTail_callShiftTowardsTail_callTail_expectOriginalNodeRemains() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        MyNode tail = list.tail();
        tail.shiftTowardsTail();

        assertTrue( newNode == list.tail() );
    }

    @Test
    public void emptyList_insertTail_callTail_callShiftTowardsHead_callTail_expectOriginalNodeRemains() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        MyNode tail = list.tail();
        tail.shiftTowardsHead();

        assertTrue( newNode == list.tail() );
    }

    @Test
    public void emptyList_insertTail_callTail_callShiftTowardsHead_callHead_expectOriginalNodeRemains() {
        MyNode newNode = new MyNode( "a" );
        list.insertTail( newNode );

        MyNode tail = list.tail();
        tail.shiftTowardsHead();

        assertTrue( newNode == list.head() );
    }

// actionsOnList with single element inserted to head

    @Test
    public void emptyList_insertHead_callSize_expect1() {
        list.insertHead( new MyNode( "a" ) );

        assertEquals( 1, list.size() );
    }

    @Test
    public void emptyList_insertHead_callIsEmpty_expectFalse() {
        list.insertHead( new MyNode( "a" ) );

        assertFalse( list.isEmpty() );
    }

    @Test
    public void emptyList_insertHead_callHasContents_expectTrue() {
        list.insertHead( new MyNode( "a" ) );

        assertTrue( list.hasContents() );
    }

    @Test
    public void emptyList_insertHead_callHead_expectNewlyInsertedNode() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        assertTrue( newNode == list.head() );
    }

    @Test
    public void emptyList_insertHead_callTail_expectNewlyInsertedNode() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        assertTrue( newNode == list.tail() );
    }

    @Test
    public void emptyList_insertHead_callTail_callNextNode_expectNull() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        MyNode tail = list.tail();
        assertNull( tail.nextNode() );
    }

    @Test
    public void emptyList_insertHead_callTail_callPreviousNode_expectNull() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        MyNode tail = list.tail();
        assertNull( tail.previousNode() );
    }

    @Test
    public void emptyList_insertHead_callTail_callDetachOnNode_callSize_expect0() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        MyNode tail = list.tail();
        assertTrue( tail.detachNode() );

        assertEquals( 0, list.size() );
    }

    @Test
    public void emptyList_insertHead_callTail_callDetachOnNode_callIsEmpty_expectTrue() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        MyNode tail = list.tail();
        assertTrue( tail.detachNode() );

        assertTrue( list.isEmpty() );
    }

    @Test
    public void emptyList_insertHead_callTail_callDetachOnNode_callHasContents_expectFalse() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        MyNode tail = list.tail();
        assertTrue( tail.detachNode() );

        assertFalse( list.hasContents() );
    }

    @Test
    public void emptyList_insertHead_callTail_callShiftTowardsHeads_expectFalse() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        MyNode tail = list.tail();
        assertFalse( tail.shiftTowardsHead() );
    }

    @Test
    public void emptyList_insertHead_callTail_callShiftTowardsTail_expectFalse() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        MyNode tail = list.tail();
        assertFalse( tail.shiftTowardsTail() );
    }

    @Test
    public void emptyList_insertHead_callTail_callShiftTowardsTail_callHead_expectOriginalNodeRemains() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        MyNode tail = list.tail();
        tail.shiftTowardsTail();

        assertTrue( newNode == list.head() );
    }

    @Test
    public void emptyList_insertHead_callTail_callShiftTowardsTail_callTail_expectOriginalNodeRemains() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        MyNode tail = list.tail();
        tail.shiftTowardsTail();

        assertTrue( newNode == list.tail() );
    }

    @Test
    public void emptyList_insertHead_callTail_callShiftTowardsHead_callTail_expectOriginalNodeRemains() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        MyNode tail = list.tail();
        tail.shiftTowardsHead();

        assertTrue( newNode == list.tail() );
    }

    @Test
    public void emptyList_insertHead_callTail_callShiftTowardsHead_callHead_expectOriginalNodeRemains() {
        MyNode newNode = new MyNode( "a" );
        list.insertHead( newNode );

        MyNode tail = list.tail();
        tail.shiftTowardsHead();

        assertTrue( newNode == list.head() );
    }

// singleElement insert next from node


    @Test
    public void singleElementList_insertTail_callSize_expect2() {
        list.insertHead( new MyNode( "a" ) );

        MyNode newNode = new MyNode( "b" );

        list.insertTail( newNode  );

        assertEquals( 2, list.size() );
    }

    @Test
    public void singleElementList_insertTail_callIsEmpty_expectFalse() {
        list.insertHead( new MyNode( "a" ) );

        MyNode newNode = new MyNode( "b" );

        list.insertTail( newNode  );

        assertFalse( list.isEmpty() );
    }

    @Test
    public void singleElementList_insertTail_callHasContents_expectTrue() {
        list.insertHead( new MyNode( "a" ) );

        MyNode newNode = new MyNode( "b" );

        list.insertTail( newNode  );

        assertTrue( list.hasContents() );
    }

    @Test
    public void singleElementList_insertTail_callHeadThenNext_expectNewlyInsertedNode() {
        list.insertHead( new MyNode( "a" ) );

        MyNode newNode = new MyNode( "b" );

        list.insertTail( newNode  );

        assertTrue( newNode == list.head().nextNode() );
    }

    @Test
    public void singleElementList_insertTail_callHeadThenPrevious_expectNull() {
        list.insertHead( new MyNode( "a" ) );

        MyNode newNode = new MyNode( "b" );

        list.insertTail( newNode  );

        assertNull( list.head().previousNode() );
    }

    @Test
    public void singleElementList_insertTail_callTail_expectNewTail() {
        list.insertHead( new MyNode( "a" ) );

        MyNode newNode = new MyNode( "b" );

        list.insertTail( newNode  );

        assertTrue( newNode == list.tail() );
    }

    @Test
    public void singleElementList_insertTail_callTailThenPrevious_expectHead() {
        MyNode head = new MyNode( "a" );
        list.insertHead( head );

        MyNode newNode = new MyNode( "b" );

        list.insertTail( newNode  );

        assertTrue( head == list.tail().previousNode() );
    }



// ways to insert new node around single element

    @Test
    public void singleElementList_insertNextFromNode() {
        MyNode origNode = new MyNode("a");
        MyNode newNode  = new MyNode("b");

        list.insertHead( origNode );
        origNode.insertNext( newNode );

        assertEquals( 2, list.size() );
        assertNull( origNode.previousNode() );
        assertTrue( newNode == origNode.nextNode() );
        assertTrue( origNode == newNode.previousNode() );
        assertTrue( list.head() == origNode );
        assertTrue( list.tail() == newNode );
    }

    @Test
    public void singleElementList_insertPreviousFromNode() {
        MyNode origNode = new MyNode("a");
        MyNode newNode  = new MyNode("b");

        list.insertHead( origNode );
        origNode.insertPrevious( newNode );

        assertEquals( 2, list.size() );
        assertTrue( newNode == origNode.previousNode() );
        assertNull( origNode.nextNode() );
        assertNull( newNode.previousNode() );
        assertTrue( origNode == newNode.nextNode() );
        assertTrue( list.head() == newNode );
        assertTrue( list.tail() == origNode );
    }

// ways to remove single node

    @Test
    public void singleElementList_popHead() {
        MyNode origNode = new MyNode("a");

        list.insertHead( origNode );

        assertTrue( origNode == list.popHead() );
        assertNull( origNode.nextNode() );
        assertNull( origNode.previousNode() );
        assertNull( list.head() );
        assertNull( list.tail() );
    }

    @Test
    public void singleElementList_popTail() {
        MyNode origNode = new MyNode("a");

        list.insertHead( origNode );

        assertTrue( origNode == list.popTail() );
        assertNull( origNode.nextNode() );
        assertNull( origNode.previousNode() );
        assertNull( list.head() );
        assertNull( list.tail() );
    }

// ways to insert value inbetween two existing nodes

    @Test
    public void twoElementList_insertValueBetweenElementsFromHead() {
        MyNode headNode = new MyNode("a");
        MyNode newNode  = new MyNode("b");
        MyNode tailNode = new MyNode("c");

        list.insertHead( headNode );
        headNode.insertNext( tailNode );
        headNode.insertNext( newNode );

        assertTrue( list.head() == headNode );
        assertTrue( list.tail() == tailNode );
        assertNull( list.head().previousNode() );
        assertTrue( list.head().nextNode() == newNode );
        assertTrue( newNode.previousNode() == headNode );
        assertTrue( newNode.nextNode() == tailNode );
        assertTrue( tailNode.previousNode() == newNode );
        assertNull( tailNode.nextNode() );
    }

    @Test
    public void twoElementList_insertValueBetweenElementsFromTail() {
        MyNode headNode = new MyNode("a");
        MyNode newNode  = new MyNode("b");
        MyNode tailNode = new MyNode("c");

        list.insertHead( headNode );
        headNode.insertNext( tailNode );
        tailNode.insertPrevious( newNode );

        assertTrue( list.head() == headNode );
        assertTrue( list.tail() == tailNode );
        assertNull( list.head().previousNode() );
        assertTrue( list.head().nextNode() == newNode );
        assertTrue( newNode.previousNode() == headNode );
        assertTrue( newNode.nextNode() == tailNode );
        assertTrue( tailNode.previousNode() == newNode );
        assertNull( tailNode.nextNode() );
    }

// ways to remove middle value from three existing nodes

    @Test
    public void threeElementList_removeMiddleElement() {
        MyNode headNode = new MyNode("a");
        MyNode newNode  = new MyNode("b");
        MyNode tailNode = new MyNode("c");

        list.insertHead( headNode );
        headNode.insertNext( tailNode );
        tailNode.insertPrevious( newNode );

        assertTrue( newNode.detachNode() );

        assertEquals( 2, list.size() );
        assertTrue( list.head() == headNode );
        assertTrue( list.tail() == tailNode );
        assertNull( list.head().previousNode() );
        assertTrue( list.head().nextNode() == tailNode );
        assertNull( newNode.previousNode() );
        assertNull( newNode.nextNode() );
        assertTrue( tailNode.previousNode() == headNode );
        assertNull( tailNode.nextNode() );
    }

// ways to remove head from three existing nodes

    @Test
    public void threeElementList_removeHeadByPoppingIt() {
        MyNode headNode = new MyNode("a");
        MyNode newNode  = new MyNode("b");
        MyNode tailNode = new MyNode("c");

        list.insertHead( headNode );
        headNode.insertNext( tailNode );
        tailNode.insertPrevious( newNode );

        list.popHead();

        assertEquals( 2, list.size() );
        assertTrue( list.head() == newNode );
        assertTrue( list.tail() == tailNode );
        assertNull( newNode.previousNode() );
        assertTrue( newNode.nextNode() == tailNode );
        assertTrue( tailNode.previousNode() == newNode );
        assertNull( tailNode.nextNode() );
    }

    @Test
    public void threeElementList_removeHeadByDetachingIt() {
        MyNode headNode = new MyNode("a");
        MyNode newNode  = new MyNode("b");
        MyNode tailNode = new MyNode("c");

        list.insertHead( headNode );
        headNode.insertNext( tailNode );
        tailNode.insertPrevious( newNode );

        assertTrue( headNode.detachNode() );

        assertEquals( 2, list.size() );
        assertTrue( list.head() == newNode );
        assertTrue( list.tail() == tailNode );
        assertNull( newNode.previousNode() );
        assertTrue( newNode.nextNode() == tailNode );
        assertTrue( tailNode.previousNode() == newNode );
        assertNull( tailNode.nextNode() );
    }


// ways to remove tail from three existing nodes

    @Test
    public void threeElementList_removeTailByPoppingIt() {
        MyNode headNode = new MyNode("a");
        MyNode newNode  = new MyNode("b");
        MyNode tailNode = new MyNode("c");

        list.insertHead( headNode );
        headNode.insertNext( tailNode );
        tailNode.insertPrevious( newNode );

        list.popTail();

        assertEquals( 2, list.size() );
        assertTrue( list.head() == headNode );
        assertTrue( list.tail() == newNode );
        assertNull( headNode.previousNode() );
        assertTrue( headNode.nextNode() == newNode );
        assertTrue( newNode.previousNode() == headNode );
        assertNull( newNode.nextNode() );
    }

    @Test
    public void threeElementList_removeTailByDetachingIt() {
        MyNode headNode = new MyNode("a");
        MyNode newNode  = new MyNode("b");
        MyNode tailNode = new MyNode("c");

        list.insertHead( headNode );
        headNode.insertNext( tailNode );
        tailNode.insertPrevious( newNode );

        assertTrue( tailNode.detachNode() );

        assertEquals( 2, list.size() );
        assertTrue( list.head() == headNode );
        assertTrue( list.tail() == newNode );
        assertNull( headNode.previousNode() );
        assertTrue( headNode.nextNode() == newNode );
        assertTrue( newNode.previousNode() == headNode );
        assertNull( newNode.nextNode() );
    }

// shift nodes around within a three node list

    @Test
    public void threeNodeList_shiftHeadToTail() {
        MyNode a = new MyNode("a");
        MyNode b = new MyNode("b");
        MyNode c = new MyNode("c");

        list = new DoubleLinkList<MyNode>( a, b, c );

        assertListEquals( a, b, c );

        assertTrue( a.shiftTowardsTail() );

        assertListEquals( b, a, c );

        assertTrue( a.shiftTowardsTail() );

        assertListEquals( b, c, a );

        assertFalse( a.shiftTowardsTail() );

        assertListEquals( b, c, a );
    }

    @Test
    public void threeNodeList_shiftTailToHead() {
        MyNode a = new MyNode("a");
        MyNode b = new MyNode("b");
        MyNode c = new MyNode("c");

        list = new DoubleLinkList<MyNode>( a, b, c );

        assertListEquals( a, b, c );

        assertTrue( c.shiftTowardsHead() );

        assertListEquals( a, c, b );

        assertTrue( c.shiftTowardsHead() );

        assertListEquals( c, a, b );

        assertFalse( c.shiftTowardsHead() );

        assertListEquals( c, a, b );
    }

// shift nodes around within a two node list

    @Test
    public void twoNodeList_shiftHeadToTail() {
        MyNode a = new MyNode("a");
        MyNode b = new MyNode("b");

        list = new DoubleLinkList<MyNode>( a, b );

        assertListEquals( a, b );

        assertTrue( a.shiftTowardsTail() );

        assertListEquals( b, a );

        assertFalse( a.shiftTowardsTail() );

        assertListEquals( b, a );
    }

    @Test
    public void twoNodeList_shiftTailToHead() {
        MyNode a = new MyNode("a");
        MyNode b = new MyNode("b");

        list = new DoubleLinkList<MyNode>( a, b );

        assertListEquals( a, b );

        assertTrue( b.shiftTowardsHead() );

        assertListEquals( b, a );

        assertFalse( b.shiftTowardsHead() );

        assertListEquals( b, a );
    }

// shift nodes around within a one node list

    @Test
    public void oneNodeList_shiftHeadToTail() {
        MyNode a = new MyNode("a");

        list = new DoubleLinkList<MyNode>( a );

        assertListEquals( a );

        assertFalse( a.shiftTowardsTail() );

        assertListEquals( a );
    }

    @Test
    public void oneNodeList_shiftTailToHead() {
        MyNode a = new MyNode("a");

        list = new DoubleLinkList<MyNode>( a );

        assertListEquals( a );

        assertFalse( a.shiftTowardsHead() );

        assertListEquals( a );
    }


// actions on a removed node



    private void assertListEquals( MyNode...expectedNodes ) {
        MyNode pos = list.head();

        for ( MyNode n : expectedNodes ) {
            assertTrue( pos.getValue() + " was not equal to " + n.getValue(), pos == n );

            pos = pos.nextNode();
        }

        assertNull( pos );
    }

    private static class MyNode extends DoubleLinkList.Node<String, MyNode> {
        public MyNode( String v ) {
            super(v);
        }
    }
}
