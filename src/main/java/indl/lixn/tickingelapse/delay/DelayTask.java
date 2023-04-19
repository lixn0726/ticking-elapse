package indl.lixn.tickingelapse.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author listen
 **/
public class DelayTask implements Delayed {

    private static long currentTime = System.currentTimeMillis();

    protected final String taskName;

    protected final int timeCost;

    protected final long scheduleTime;

    protected static final AtomicInteger taskCount = new AtomicInteger(0);

    public DelayTask(String taskName, int timeCost) {
        this.taskName = taskName;
        this.timeCost = timeCost;
        taskCount.incrementAndGet();
        currentTime += 1000 + (long) (Math.random() * 1000);
        scheduleTime = currentTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long expirationTime = scheduleTime - System.currentTimeMillis();
        return unit.convert(expirationTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.scheduleTime - ((DelayTask) o).scheduleTime);
    }

    public void execTask() {
        long startTime = System.currentTimeMillis();
        System.out.println("Task " + taskName + ": schedule start time = " + scheduleTime
                + ", real start time = " + startTime + ", delay = " + (startTime - scheduleTime));
        try {
            Thread.sleep(timeCost);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
