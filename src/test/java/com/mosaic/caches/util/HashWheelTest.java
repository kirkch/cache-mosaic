package com.mosaic.caches.util;

import com.mosaic.jtunit.TestTools;
import org.junit.Test;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
@SuppressWarnings({"unchecked", "UnusedAssignment"})
public class HashWheelTest {

    private HashWheel hashWheel = new HashWheel( 10, 128, 4 );
//2,4,8,16,32,64,128,256,512

    @Test
    public void scheduleATaskWithinTheCurrentBucket_performBookingKeepingBeforeBucketEnd_expectTaskNotToRun() {
        MyTask task = new MyTask();

        hashWheel.register( 120, task );
        hashWheel.applyBookKeeping( 127 );


        assertEquals( 0, task.runCount );
    }

    @Test
    public void scheduleATaskWithinTheCurrentBucket_performBookingKeepingAfterBucketExpires_expectTaskToRun() {
        MyTask task = new MyTask();

        hashWheel.register( 120, task );
        hashWheel.applyBookKeeping( 128 );


        assertEquals( 1, task.runCount );
    }

    @Test
    public void scheduleTwoTasksWithinTheCurrentBucket_performBookingKeepingAfterBucketExpires_expectBothTasksToRun() {
        MyTask task1 = new MyTask();
        MyTask task2 = new MyTask();

        hashWheel.register( 120, task1 );
        hashWheel.register( 50, task2 );

        hashWheel.applyBookKeeping( 128 );


        assertEquals( 1, task1.runCount );
        assertEquals( 1, task2.runCount );
    }

    @Test
    public void scheduleTaskInThePast_expectItToRunImmediately() {
        MyTask task1 = new MyTask();

        hashWheel.applyBookKeeping( 128 );

        hashWheel.register( 120, task1 );


        assertEquals( 1, task1.runCount );
    }

    @Test
    public void scheduleTaskInCurrentBucket_runBookKeepingAtTimeOfCurrentBucket_expectTicketToReportTaskHasNotRun() {
        MyTask task1 = new MyTask();

        HashWheel.Ticket ticket = hashWheel.register( 120, task1 );
        hashWheel.applyBookKeeping( 127 );


        assertTrue( ticket.isScheduledToRun() );
        assertFalse( ticket.hasRun() );
        assertFalse( ticket.wasCancelled() );
    }

    @Test
    public void scheduleTaskInCurrentBucket_runBookKeepingInNextBucketWindow_expectTicketToReportTaskHasRun() {
        MyTask task1 = new MyTask();

        HashWheel.Ticket ticket = hashWheel.register( 120, task1 );
        hashWheel.applyBookKeeping( 128 );


        assertFalse( ticket.isScheduledToRun() );
        assertTrue( ticket.hasRun() );
        assertFalse( ticket.wasCancelled() );
    }

    @Test
    public void scheduleTaskInThePast_expectTicketToReportTaskHasRun() {
        MyTask task1 = new MyTask();

        hashWheel.applyBookKeeping( 128 );

        HashWheel.Ticket ticket = hashWheel.register( 120, task1 );

        assertFalse( ticket.isScheduledToRun() );
        assertTrue( ticket.hasRun() );
        assertFalse( ticket.wasCancelled() );
    }

    @Test
    public void scheduleTaskInThePastAFullRotationAfterTheFact_expectTicketToReportTaskHasRun() {
        MyTask task1 = new MyTask();

        hashWheel.applyBookKeeping( 128*4 );

        HashWheel.Ticket ticket = hashWheel.register( 120, task1 );

        assertEquals( 1, task1.runCount );
        assertFalse( ticket.isScheduledToRun() );
        assertTrue( ticket.hasRun() );
        assertFalse( ticket.wasCancelled() );
    }

    @Test
    public void scheduleTask_cancelViaTicket_expectCallToBookKeepingToNotRunTask() {
        MyTask task1 = new MyTask();

        HashWheel.Ticket ticket = hashWheel.register( 120, task1 );
        ticket.cancel();

        hashWheel.applyBookKeeping( 128*4 );

        assertEquals( 0, task1.runCount );
        assertFalse( ticket.isScheduledToRun() );
        assertFalse( ticket.hasRun() );
        assertTrue( ticket.wasCancelled() );
    }

    @Test
    public void scheduleTwoTasks_cancelOneViaTicket_expectBookKeepingToOneTheOtherTask() {
        MyTask task1 = new MyTask();
        MyTask task2 = new MyTask();

        HashWheel.Ticket ticket1 = hashWheel.register( 120, task1 );
        HashWheel.Ticket ticket2 = hashWheel.register( 115, task2 );
        ticket1.cancel();

        hashWheel.applyBookKeeping( 128*4 );

        assertEquals( 0, task1.runCount );
        assertFalse( ticket1.isScheduledToRun() );
        assertFalse( ticket1.hasRun() );
        assertTrue( ticket1.wasCancelled() );

        assertEquals( 1, task2.runCount );
        assertFalse( ticket2.isScheduledToRun() );
        assertTrue( ticket2.hasRun() );
        assertFalse( ticket2.wasCancelled() );
    }

    @Test
    public void scheduleTwoTasks_callClearTheBookkeeping_expectNeitherTaskToRun() {
        MyTask task1 = new MyTask();
        MyTask task2 = new MyTask();

        HashWheel.Ticket ticket1 = hashWheel.register( 120, task1 );
        HashWheel.Ticket ticket2 = hashWheel.register( 115, task2 );
        hashWheel.clear();

        hashWheel.applyBookKeeping( 128*4 );

        assertEquals( 0, task1.runCount );
        assertFalse( ticket1.isScheduledToRun() );
        assertFalse( ticket1.hasRun() );
        assertTrue( ticket1.wasCancelled() );

        assertEquals( 0, task2.runCount );
        assertFalse( ticket2.isScheduledToRun() );
        assertFalse( ticket2.hasRun() );
        assertTrue( ticket2.wasCancelled() );
    }

    @Test
    public void scheduleTask_cancelViaTicketAndLetGoOfTask_expectTaskToBeGCd() {
        MyTask task1 = new MyTask();

        HashWheel.Ticket ticket = hashWheel.register( 120, task1 );
        ticket.cancel();


        assertEquals( 0, task1.runCount );
        assertFalse( ticket.isScheduledToRun() );
        assertFalse( ticket.hasRun() );
        assertTrue( ticket.wasCancelled() );


        final Reference ref = new WeakReference(task1 );
        task1 = null;

        TestTools.spinUntilReleased( ref );
    }

    @Test
    public void scheduleTask_cancelViaTicketAndLetGoOfTicket_expectTicketToBeGCd() {
        MyTask task1 = new MyTask();

        HashWheel.Ticket ticket = hashWheel.register( 120, task1 );
        ticket.cancel();


        final Reference ref = new WeakReference(ticket);
        ticket = null;

        TestTools.spinUntilReleased( ref );
    }

    @Test
    public void scheduleTask_runTaskViaBookKeepingAndDropTestRefToTask_expectTaskToBeGCd() {
        MyTask task1 = new MyTask();

        hashWheel.register( 120, task1 );
        hashWheel.applyBookKeeping( 128 );


        final Reference ref = new WeakReference(task1);
        task1 = null;

        TestTools.spinUntilReleased( ref );
    }

    @Test
    public void scheduleTask_runTaskViaBookKeepingAndDropRefToTicket_expectTicketToBeGCd() {
        MyTask task1 = new MyTask();

        HashWheel.Ticket ticket = hashWheel.register( 120, task1 );
        hashWheel.applyBookKeeping( 128 );


        final Reference ref = new WeakReference(ticket);
        ticket = null;

        TestTools.spinUntilReleased( ref );
    }

    @Test
    public void scheduleTwoTasks_callClearTheBookkeepingDropReferencesToTasksAndForceGC_expectTasksToBeGCd() {
        MyTask task1 = new MyTask();
        MyTask task2 = new MyTask();

        HashWheel.Ticket ticket1 = hashWheel.register( 120, task1 );
        HashWheel.Ticket ticket2 = hashWheel.register( 115, task2 );
        hashWheel.clear();

        hashWheel.applyBookKeeping( 128*4 );

        List<Reference> weakRefs = TestTools.createWeakReferences( task1, task2, ticket1, ticket2 );

        task1 = null;
        task2 = null;
        ticket1 = null;
        ticket2 = null;

        TestTools.spinUntilReleased( weakRefs );
    }

    @Test
    public void rescheduleIntoTheNextBucket_runCurrentBucket_expectTaskToNotRun() {
        MyTask task = new MyTask();

        HashWheel.Ticket ticket = hashWheel.register( 200, task );
        ticket.rescheduleTo( 128*2+50 );

        hashWheel.applyBookKeeping( 128*2 );

        assertEquals( 0, task.runCount );
    }

    @Test
    public void rescheduleIntoTheNextBucket_runNextBucket_expectTaskToRun() {
        MyTask task = new MyTask();

        HashWheel.Ticket ticket = hashWheel.register( 200, task );
        ticket.rescheduleTo( 128*2+50 );

        hashWheel.applyBookKeeping( 128*3 );

        assertEquals( 1, task.runCount );
    }

    //
    // rescheduleIntoTheNextBucket_runNextBucket_expectTaskToGetGCd
    // rescheduleIntoPreviousBucket_expectTaskToRunImmediately
    // rescheduleIntoPreviousBucket_expectTaskToGetGCd

    @Test
    public void toWheelIndex() {
        assertEquals( 0, hashWheel.toWheelIndex(0) );
        assertEquals( 0, hashWheel.toWheelIndex(1) );
        assertEquals( 0, hashWheel.toWheelIndex(127) );
        assertEquals( 1, hashWheel.toWheelIndex(128) );
        assertEquals( 1, hashWheel.toWheelIndex(255) );
        assertEquals( 2, hashWheel.toWheelIndex(256) );
        assertEquals( 2, hashWheel.toWheelIndex(383) );
        assertEquals( 3, hashWheel.toWheelIndex(384) );
        assertEquals( 3, hashWheel.toWheelIndex(511) );
        assertEquals( 0, hashWheel.toWheelIndex(512) );
    }

    @Test
    public void toRotationCount() {
        assertEquals( 0, hashWheel.toRotationCount( 0 ) );
        assertEquals( 0, hashWheel.toRotationCount( 1 ) );
        assertEquals( 0, hashWheel.toRotationCount( 127 ) );
        assertEquals( 0, hashWheel.toRotationCount( 128 ) );
        assertEquals( 0, hashWheel.toRotationCount( 255 ) );
        assertEquals( 0, hashWheel.toRotationCount( 256 ) );
        assertEquals( 0, hashWheel.toRotationCount( 383 ) );
        assertEquals( 0, hashWheel.toRotationCount( 384 ) );
        assertEquals( 0, hashWheel.toRotationCount( 511 ) );
        assertEquals( 1, hashWheel.toRotationCount( 512 ) );
    }

    private static class MyTask implements Runnable {
        public int runCount = 0;

        @Override
        public void run() {
            runCount++;
        }
    }
}
