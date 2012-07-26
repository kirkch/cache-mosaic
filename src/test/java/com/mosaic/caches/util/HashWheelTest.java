package com.mosaic.caches.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class HashWheelTest {

    private HashWheel hashWheel = new HashWheel( 10, 128, 4 );
//2,4,8,16,32,64,128,256,512

    @Test
    public void scheduleATaskWithinTheCurrentBucket_performBookingKeepingBeforeBucketEnd_expectTaskNotToRun() {
        MyTask task = new MyTask();

        hashWheel.register( 120, task );
        hashWheel.applyBookKeeping( 127 );


        assertEquals( 0, task.hasRunCount );
    }

    @Test
    public void scheduleATaskWithinTheCurrentBucket_performBookingKeepingAfterBucketExpires_expectTaskToRun() {
        MyTask task = new MyTask();

        hashWheel.register( 120, task );
        hashWheel.applyBookKeeping( 128 );


        assertEquals( 1, task.hasRunCount );
    }

    @Test
    public void scheduleTwoTasksWithinTheCurrentBucket_performBookingKeepingAfterBucketExpires_expectBothTasksToRun() {
        MyTask task1 = new MyTask();
        MyTask task2 = new MyTask();

        hashWheel.register( 120, task1 );
        hashWheel.register( 50, task2 );

        hashWheel.applyBookKeeping( 128 );


        assertEquals( 1, task1.hasRunCount );
        assertEquals( 1, task2.hasRunCount );
    }

    @Test
    public void scheduleTaskInThePast_expectItToRunImmediately() {
        MyTask task1 = new MyTask();

        hashWheel.applyBookKeeping( 128 );

        hashWheel.register( 120, task1 );


        assertEquals( 1, task1.hasRunCount );
    }


    // scheduleTaskInCurrentBucket_runBookKeeping_expectTicketToReportTaskHasNotRun
    // scheduleTaskInCurrentBucket_runBookKeepingInNextBucketWindow_expectTicketToReportTaskHasRun

    // scheduleTaskInThePast_expectTicketToReportTaskHasRun
    // scheduleTaskInThePastAFullRotationAfterTheFact_expectItToRunImmediately
    // scheduleTaskInThePastAFullRotationAfterTheFact_expectTicketToReportTaskHasRun


    // scheduleTask_cancelViaTicket_expectCallToBookKeepingToNotRunTask
    // scheduleTwoTasks_cancelOneViaTicket_expectBookKeepingToOneTheOtherTask
    // scheduleTask_cancelViaTicketRunbookKeepingAndLetGoOfTask_expectTaskToBeGCd
    // scheduleTask_cancelViaTicketRunbookKeepingAndLetGoOfTicket_expectTicketToBeGCd

    // scheduleTask_cancelViaTicketRunbookKeepingAndLetGoOfTicket_expectTicketToBeGCd
    // scheduleTask_runTaskViaBookKeepingAndDropTestRefToTask_expectTaskToBeGCd

    // scheduleTwoTasks_callClearTheBookkeeping_expectNeitherTaskToRun
    // scheduleTwoTasks_callClearTheBookkeepingDropReferencesToTasksAndForceGC_expectTasksToBeGCd
    //



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
        public int hasRunCount = 0;

        @Override
        public void run() {
            hasRunCount++;
        }
    }
}
