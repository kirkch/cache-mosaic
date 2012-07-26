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
        int targetWheelIndex    = toWheelIndex( whenMillis );
        int targetRotationCount = toRotationCount( whenMillis );

        boolean hasBucketAlreadyFired = targetRotationCount < currentRotationCount || (targetRotationCount == currentRotationCount && targetWheelIndex < currentWheelIndex);
        if ( hasBucketAlreadyFired ) {
            task.run();

            return null; // todo
        }

        HashWheelBucket bucket = hashWheel[targetWheelIndex];

        return bucket.register( targetRotationCount, task );
    }

    public void clear() {

    }

    public void applyBookKeeping( long nowMillis ) {
        int targetWheelIndex    = toWheelIndex( nowMillis );
        int targetRotationCount = toRotationCount( nowMillis );

        while ( currentWheelIndex != targetWheelIndex || targetRotationCount != currentRotationCount ) {
            hashWheel[currentWheelIndex].trigger(currentRotationCount);

            incrementWheel();
        }
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


    public static class Ticket {
        private Ticket( TaskNode taskNode ) {
        }

        public void cancel() {

        }
    }


    private static class HashWheelBucket {
        private RotationList rotationList = new RotationList();

        public Ticket register( int rotationSeq, Runnable task ) {
            DoubleLinkList<TaskNode> taskList = rotationList.selectOrCreateTaskListForRotation( rotationSeq );

            TaskNode taskNode = new TaskNode( task );
            taskList.insertTail( taskNode );

            return new Ticket( taskNode );
        }

        public void trigger( int currentRotationCount ) {
            RotationNode rotationNode = rotationList.selectRotationNode( currentRotationCount );

            if ( rotationNode != null ) {
                rotationNode.detachNode();

                TaskNode nextTaskNode = rotationNode.getValue().head();
                while ( nextTaskNode != null ) {
                    nextTaskNode.getValue().run();

                    nextTaskNode = nextTaskNode.nextNode();
                }
            }
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
    }

    private static class RotationNode extends DoubleLinkList.Node<DoubleLinkList<TaskNode>, RotationNode> {
        private final int rotationSeq;

        public RotationNode( int rotationSeq ) {
            super( new DoubleLinkList() );

            this.rotationSeq = rotationSeq;
        }
    }

    private static class TaskNode extends DoubleLinkList.Node<Runnable, TaskNode> {
        public TaskNode( Runnable runnable ) {
            super( runnable );
        }
    }
}
