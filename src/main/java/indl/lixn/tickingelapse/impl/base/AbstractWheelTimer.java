package indl.lixn.tickingelapse.impl.base;

import indl.lixn.tickingelapse.WheelTimer;
import indl.lixn.tickingelapse.bucket.TimeoutBucket;
import indl.lixn.tickingelapse.common.Constants;
import indl.lixn.tickingelapse.config.WheelTimerConfig;
import indl.lixn.tickingelapse.cursor.Cursor;
import indl.lixn.tickingelapse.exception.StopException;
import indl.lixn.tickingelapse.timeout.Timeout;
import lombok.ToString;

import java.time.Clock;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author listen
 **/
@ToString
public abstract class AbstractWheelTimer implements WheelTimer {

    protected static final Clock CLOCK = Constants.CLOCK;

    protected Cursor cursor;

    protected WheelTimerConfig config;

    protected TimeoutBucket[] buckets;

    protected AtomicBoolean running = new AtomicBoolean(false);

    protected AbstractWheelTimer(Cursor cursor, WheelTimerConfig config) {
        this.cursor = cursor;
        this.config = config;
        this.buckets = new TimeoutBucket[config.tickCount()];
        for (int i = 0; i < config.tickCount(); i++) {
            buckets[i] = new TimeoutBucket();
        }
        System.out.println(this.toString());
    }

    @Override
    public void addTimeout(Timeout timeout) {
        if (timeout == null) {
            throw StopException.instance;
        }
        long delay = timeout.getDelayed();
        if (largerThanLimit(delay)) {
            handleLargeDelayedTimeout(timeout);
        }
        buckets[getBucketIndex(delay)].addTimeout(timeout);
    }

    @Override
    public void start() {
        if (this.running.get()) {
            throw StopException.instance;
        }
        this.running.compareAndSet(false, true);
    }

    @Override
    public void stop() {
        if (!this.running.get()) {
            throw StopException.instance;
        }
        this.running.compareAndSet(true, false);
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }

    @Override
    public Cursor cursor() {
        return this.cursor;
    }

    @Override
    public WheelTimerConfig config() {
        return this.config;
    }

    protected abstract void handleLargeDelayedTimeout(Timeout timeout);

    private boolean largerThanLimit(long delay) {
        return delay >= this.config.maxDurationMs();
    }

    private int getBucketIndex(long delay) {
        int step = (int) (delay / this.config.tickDurationMs());
        if ((delay % this.config.tickDurationMs()) != 0) {
            step += 1;
        }
        return (step + current()) % (config.tickCount());
    }

    protected int current() {
        return this.cursor.currentStep();
    }
}
