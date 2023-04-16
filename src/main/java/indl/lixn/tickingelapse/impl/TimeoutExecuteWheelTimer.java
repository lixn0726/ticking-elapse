package indl.lixn.tickingelapse.impl;

import indl.lixn.tickingelapse.common.Constants;
import indl.lixn.tickingelapse.common.LogWrapper;
import indl.lixn.tickingelapse.config.ConfigImpl;
import indl.lixn.tickingelapse.config.WheelTimerConfig;
import indl.lixn.tickingelapse.cursor.Cursor;
import indl.lixn.tickingelapse.cursor.CursorImpl;
import indl.lixn.tickingelapse.impl.base.AbstractWheelTimer;
import indl.lixn.tickingelapse.timeout.Timeout;
import indl.lixn.tickingelapse.util.TimeUnitUtil;

import java.time.Instant;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * @author listen
 **/
public class TimeoutExecuteWheelTimer extends AbstractWheelTimer {

    private static final TimeUnit MILLISECONDS = TimeUnit.MILLISECONDS;

    private static final LogWrapper LOG = Constants.LOG;

    private final Calendar calendar = Calendar.getInstance();

    private Thread tickWorker;

    // 默认是类似于始终的构造
    public TimeoutExecuteWheelTimer() {
        this(new CursorImpl(0, 60), new ConfigImpl(TimeUnit.SECONDS, 1L, 60));
    }

    public TimeoutExecuteWheelTimer(Cursor cursor, WheelTimerConfig config) {
        super(cursor, config);
    }

    @Override
    public void start() {
        LOG.info("WheelTimer start now");
        super.start();
        ensureCalendar();
        callUpWorker();
        Runnable ticker = () -> {
            while (workerRunning()) {
                tick();
                this.cursor.move();
                // 这里报错？？？
                execute();
                LOG.info("move to index {}", this.current());
            }
        };
        this.tickWorker = new Thread(ticker);
        this.tickWorker.start();
        System.out.println();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void handleLargeDelayedTimeout(Timeout timeout) {
        LOG.error("can not handle timeout for delay is {} but current max duration is {}", timeout.getDelayed(), config.maxDuration());
    }

    private void ensureCalendar() {
        this.calendar.add(TimeUnitUtil.calenderUnitOf(this.config.unit()), (int) this.config.tickDuration());
    }

    private boolean workerRunning() {
        return !tickWorker.isInterrupted() && isRunning();
    }

    // core method
    private void tick() {
        try {
            long tickTime = tickTime();
            MILLISECONDS.sleep(tickTime);
            ensureCalendar();
        } catch (InterruptedException ex) {
            stop();
        }
    }

    private void execute() {
        for (Timeout expireTimeout : this.buckets[this.current()].getExpireTimeouts()) {
            expireTimeout.perform();
        }
    }

    private long tickTime() {
        return this.calendar.getTimeInMillis() - Instant.now(CLOCK).toEpochMilli();
    }

    private void callUpWorker() {

    }

}
