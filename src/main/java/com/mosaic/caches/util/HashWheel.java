package com.mosaic.caches.util;

/**
 * A priority queue. Offers average O(1) insert, O(1) book keeping and O(1) expiry. Worst case inserts are O(n), which occurs
 * when all items are scheduled for the fall onto the same bucket of the hash wheel but overflow into separate lists. This
 * is significantly better than a sorted linked list, sorted heap or tree. Specifically designed for expiring timers.<p/>
 *
 * This scheduler does not guarantee strict timings. Any promise to execute a task is on the premise that it will occur
 * after the specified time and not before.<p/>
 *
 *
 * The wheel is not thread safe.
 *
 * @see "http://www.cse.wustl.edu/~cdgill/courses/cs6874/TimingWheels.ppt"
 */
@SuppressWarnings("unchecked")
public class HashWheel {

    private final HashWheelBucket[] hashWheel;
    private final int               bitmask;
    private final int               bucketGranularityBitShift;
    private final int               rotationBitShift;

    private final long wheelGranularityMillis;

    private int currentWheelIndex;
    private int currentRotationCount;

    public HashWheel() {
        this( System.currentTimeMillis(), 128, 256 );
    }

    public HashWheel( long startMillis ) {
        this( startMillis, 128, 256 );
    }

    /**
     * As an optimisation to replace divisions with bitshifts and modulo with bitands, scheduling granularity and hash wheel
     * size are rounded up to the closest power of two (eg 4,8,16,32,64,128,...)
     */
    public HashWheel( long startMillis, long schedulingGranularityMillis, int hashWheelSize ) {
        int size  = BitUtils.roundUpToClosestPowerOf2( hashWheelSize );
        hashWheel = new HashWheelBucket[size];

        for ( int i=0; i<size; i++ ) {
            hashWheel[i] = new HashWheelBucket();
        }

        bitmask                   = size - 1;
        wheelGranularityMillis    = BitUtils.roundUpToClosestPowerOf2( schedulingGranularityMillis );
        bucketGranularityBitShift = BitUtils.numberOfBitsSet( (int) wheelGranularityMillis-1 );
        rotationBitShift          = bucketGranularityBitShift + BitUtils.numberOfBitsSet( bitmask );

        currentWheelIndex      = toWheelIndex( startMillis );
        currentRotationCount   = toRotationCount( startMillis );
    }

    /**
     * Register the specified runnable to execute after the specified time. Registration usually happens in O(1) time.
     * Worst case occurs when registering tasks where the scheduled time increases such that the schedule rotates around
     * the hash wheel once from the last task scheduled and falls onto the same bucket. ie it increments roughly by
     * (bucketGranularity)*wheelSize each time.
     */
    public Ticket register( long whenMillis, Runnable task ) {
        TaskNode taskNode = new TaskNode(task);

        return register( whenMillis, taskNode );
    }

    public void clear() {
        for ( HashWheelBucket bucket : hashWheel ) {
            bucket.clear();
        }

        currentRotationCount = 0;
        currentWheelIndex    = 0;
    }

    public void applyBookKeeping( long nowMillis ) {
        int targetWheelIndex    = toWheelIndex( nowMillis );
        int targetRotationCount = toRotationCount( nowMillis );

        while ( currentWheelIndex != targetWheelIndex || targetRotationCount != currentRotationCount ) {
            hashWheel[currentWheelIndex].trigger(currentRotationCount);

            incrementWheel();
        }
    }


    private Ticket register( long whenMillis, TaskNode taskNode ) {
        int targetWheelIndex    = toWheelIndex( whenMillis );
        int targetRotationCount = toRotationCount( whenMillis );

        boolean hasBucketAlreadyFired = targetRotationCount < currentRotationCount || (targetRotationCount == currentRotationCount && targetWheelIndex < currentWheelIndex);
        if ( hasBucketAlreadyFired ) {
            taskNode.run();

            return ALREADY_FIRED_TICKET;
        }

        HashWheelBucket bucket = hashWheel[targetWheelIndex];

        Ticket ticket = bucket.register( targetRotationCount, taskNode );
        ticket.setOwningHashWheel( this );

        return taskNode;
    }

    private void incrementWheel() {
        int previousWheelIndex = currentWheelIndex;

        currentWheelIndex = (currentWheelIndex+1) & bitmask;

        if ( currentWheelIndex < previousWheelIndex ) {
            currentRotationCount++;
        }
    }

    protected int toWheelIndex( long millis ) {
        return (int) (millis >> bucketGranularityBitShift) & bitmask;
    }

    protected int toRotationCount( long millis ) {
        return (int) (millis >> rotationBitShift);
    }

    private static Ticket ALREADY_FIRED_TICKET = new Ticket() {

        @Override
        public boolean isScheduledToRun() {
            return false;
        }

        @Override
        public boolean wasCancelled() {
            return false;
        }

        @Override
        public boolean hasRun() {
            return true;
        }

        public void cancel() {}
        public void rescheduleTo( long whenMillis ) {}
        public void setOwningHashWheel( HashWheel hashWheel ) {}
    };


    public static interface Ticket {

        public boolean isScheduledToRun();
        public boolean wasCancelled();
        public boolean hasRun();

        public void cancel();

        public void rescheduleTo( long whenMillis );

        public void setOwningHashWheel( HashWheel hashWheel );
    }


    private static class HashWheelBucket {
        private RotationList rotationList = new RotationList();

        public Ticket register( int rotationSeq, TaskNode taskNode ) {
            DoubleLinkList<TaskNode> taskList = rotationList.selectOrCreateTaskListForRotation( rotationSeq );

            taskList.insertTail( taskNode );

            return taskNode;
        }

        public void trigger( int currentRotationCount ) {
            RotationNode rotationNode = rotationList.selectRotationNode( currentRotationCount );

            if ( rotationNode != null ) {
                rotationNode.detachNode();

                TaskNode nextTaskNode = rotationNode.getValue().head();
                while ( nextTaskNode != null ) {
                    nextTaskNode.run();

                    nextTaskNode = nextTaskNode.nextNode();
                }
            }
        }

        public void clear() {
            rotationList.clear();
        }
    }

    private static class RotationList {
        private DoubleLinkList<RotationNode> rotationList = new DoubleLinkList();

        public RotationNode selectRotationNode( int rotationSeq ) {
            RotationNode candidate = rotationList.head();

            while ( candidate != null ) {
                if ( candidate.rotationSeq == rotationSeq ) {
                    return candidate;
                }

                candidate = candidate.nextNode();
            }

            return null;
        }

        public DoubleLinkList<TaskNode> selectOrCreateTaskListForRotation( int rotationSeq ) {
            RotationNode candidate = rotationList.head();

            while ( candidate != null && candidate.rotationSeq < rotationSeq) {
                candidate = candidate.nextNode();
            }

            if ( candidate == null ) {
                candidate = new RotationNode(rotationSeq);
                rotationList.insertTail( candidate );
            } else if ( candidate.rotationSeq != rotationSeq ) {
                RotationNode newRotationNode = new RotationNode(rotationSeq);
                candidate.insertPrevious( newRotationNode );

                candidate = newRotationNode;
            }

            return candidate.getValue();
        }

        public void clear() {
            RotationNode rotationNode = rotationList.head();

            while ( rotationNode != null ) {
                TaskNode taskNode = rotationNode.getValue().head();

                while ( taskNode != null ) {
                    taskNode.cancel();

                    taskNode = rotationNode.getValue().head();
                }

                rotationNode.detachNode();
                rotationNode = rotationList.head();
            }
        }
    }

    private static class RotationNode extends DoubleLinkList.Node<DoubleLinkList<TaskNode>, RotationNode> {
        private final int rotationSeq;

        public RotationNode( int rotationSeq ) {
            super( new DoubleLinkList() );

            this.rotationSeq = rotationSeq;
        }
    }

    private static class TaskNode extends DoubleLinkList.Node<Runnable, TaskNode> implements Ticket {
        private boolean   hasRun;
        private boolean   wasCancelled;
        private HashWheel owningHashWheel;

        public TaskNode( Runnable runnable ) {
            super( runnable );
        }

        public boolean isScheduledToRun() {
            return isAttached() && !wasCancelled && !hasRun;
        }

        public boolean wasCancelled() {
            return wasCancelled;
        }

        public boolean hasRun() {
            return hasRun;
        }

        public void cancel() {
            detachNode();

            clear();

            wasCancelled = true;
        }

        public void rescheduleTo( long whenMillis ) {
            if ( !isScheduledToRun() ) {
                return;
            }

            detachNode();
            owningHashWheel.register( whenMillis, this );
        }

        public void setOwningHashWheel( HashWheel hashWheel ) {
            owningHashWheel = hashWheel;
        }

        protected void run() {
            if ( !hasRun ) {
                getValue().run();

                hasRun          = true;
                owningHashWheel = null;
            }
        }
    }
}
