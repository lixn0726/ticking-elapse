package indl.lixn.tickingelapse.by_kafka;

import jdk.nashorn.internal.runtime.Timing;
import lombok.Data;

import java.util.concurrent.DelayQueue;

/**
 * @author listen
 **/
@Data
public class TimingWheel {

    private final long tickMs;

    private long internal;

    private final long bucketSize;

    private DelayQueue<TimerTaskList> buckets;

    private long currentTime;

    private TimingWheel previous;

    private TimingWheel next;

    public void advanceClock(long advanceTimeMs) {
        if (advanceTimeMs >= currentTime + tickMs) {
            currentTime = advanceTimeMs - (advanceTimeMs % tickMs);

            if (this.next != null) {
                this.next.advanceClock(advanceTimeMs);
            }
        }
    }

    public TimingWheel(long tickMs, long bucketSize) {
        this.tickMs = tickMs;
        this.bucketSize = bucketSize;
    }

    public void addOverflowWheel() {
        long overflowWheelTimeMs = this.internal * this.bucketSize;
        this.next = new TimingWheel(overflowWheelTimeMs, this.bucketSize);
        this.next.previous = this;
    }

}
