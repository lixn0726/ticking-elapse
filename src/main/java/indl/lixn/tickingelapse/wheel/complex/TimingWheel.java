package indl.lixn.tickingelapse.wheel.complex;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author listen
 **/
public class TimingWheel {

    long tickMs;

    int wheelSize;

    long startTime;

    AtomicInteger taskCounter;

    DelayQueue<TimerTaskLinkedList> queue;

    long interval = tickMs * wheelSize;

    TimerTaskLinkedList[] buckets = new TimerTaskLinkedList[wheelSize];

    long currentTime = startTime - (startTime % tickMs);

    TimingWheel overflowWheel;

    private void addOverflowWheel() {
        if (overflowWheel == null) {
            overflowWheel = new TimingWheel();
        }
    }

    public boolean add(TimerTaskLinkedList.TimerTaskEntry entry) {
        long expiration = entry.getDelay();

        if (expiration < currentTime + tickMs) {
            // Already expired
            return false;
        } else if (expiration < currentTime + interval) {
            // Put in its own bucket
            long virtualId = expiration / tickMs;
            TimerTaskLinkedList bucket = buckets[(int) ((virtualId % (long)(wheelSize)))];
            bucket.add(entry);

            // Set the bucket expiration time
            if (bucket.setExpiration(virtualId * tickMs)) {
                // The bucket needs to be enqueued because it was an expired bucket
                // We only need to enqueue the bucket when its expiration time has changed, i.e. the wheel has advanced
                // and the previous buckets gets reused; further calls to set the expiration within the same cycle
                // will pass in the same value and hence return false, thus the bucket with the same expiration will not
                // be enqueued multiple times
                queue.offer(bucket);
            }
            return true;
        } else {
            // Out of the interval. Put it into the parent timer
            if (overflowWheel == null) {
                addOverflowWheel();
            }
            overflowWheel.add(entry);
            return true;
        }
    }

    public void advanceClock(long timeMs) {
        if (timeMs >= currentTime + tickMs) {
            currentTime = timeMs - (timeMs % tickMs);

            if (overflowWheel != null) {
                overflowWheel.advanceClock(currentTime);
            }
        }
    }

}
