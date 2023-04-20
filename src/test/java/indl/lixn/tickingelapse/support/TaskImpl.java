package indl.lixn.tickingelapse.support;

import indl.lixn.tickingelapse.common.LogWrapper;
import indl.lixn.tickingelapse.wheel.TimerTask;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author listen
 **/
public class TaskImpl implements TimerTask {

    private static final LogWrapper LOG = LogWrapper.instance;

    private static final AtomicInteger taskCount =new AtomicInteger(0);

    private final int order = taskCount.getAndIncrement();

    private final long delay;

    public TaskImpl(long delay) {
        this.delay = delay;
    }

    public TaskImpl(long delay, TimeUnit timeUnit) {
        this.delay = timeUnit.toMillis(delay);
    }

    @Override
    public void cancel() {
        // TODO
        LOG.error("not support operation");
    }

    @Override
    public void perform() {
        LOG.info("task {} perform", order);
    }

    @Override
    public long getDelay() {
        return this.delay;
    }

    @Override
    public String toString() {
        return "TimerTask_" + order;
    }
}
