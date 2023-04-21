package indl.lixn.tickingelapse.wheel.simple;

import indl.lixn.tickingelapse.common.LogWrapper;
import indl.lixn.tickingelapse.entity.dataobject.TimingWheelConfig;
import indl.lixn.tickingelapse.wheel.TimerTask;
import indl.lixn.tickingelapse.wheel.simple.cursor.WheelCursor;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author listen
 **/
public class ArrayBasedTimingWheel {

    private static final LogWrapper LOG = LogWrapper.instance;

    private ArrayBasedTimingWheel overflowTimingWheel = null;

    private ArrayBasedTimingWheel underflowTimingWheel = null;

    private final WheelCursor cursor;

    private final TimerTaskList[] buckets;

    private final Calendar calendar = Calendar.getInstance();

    private final int wheelSize;

    private final long tickMs;

    private final long interval;

    public ArrayBasedTimingWheel(TimingWheelConfig config) {
        this(config.getBucketSize(), config.getTickMs());
    }

    public ArrayBasedTimingWheel(int wheelSize, long tickMs) {
        this.wheelSize = wheelSize;
        this.tickMs = tickMs;
        cursor = new WheelCursor(wheelSize);
        interval = tickMs * wheelSize;
        buckets = new TimerTaskList[wheelSize];
        for (int i = 0; i < wheelSize; i++) {
            buckets[i] = new TimerTaskList();
        }
    }

    public void start() {
        LOG.info("start now");
        calendar.setTime(new Date());
        advanceCalendar();
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                advanceClock();
            }
        }).start();
    }

    public void add(TimerTask task) {
        add(task, task.getDelay());
    }

    public void add(TimerTask task, long delayMs) {
        if (delayMs >= interval) {
            addOverflowTimingWheel();
//            LOG.info("{} add to overflow timingWheel", task.toString());
            overflowTimingWheel.add(task, delayMs - interval);
            return;
        }
        int listIndex = (int) (delayMs / tickMs);
        if ((delayMs % tickMs) != 0) {
            listIndex += 1;
        }
        listIndex += cursor.current();
        listIndex %= wheelSize;
        LOG.info("{} add at {}", task.toString(), listIndex);
        buckets[listIndex].add(task);
    }

    public void advanceClock() {
        // 1. 休眠
        tick();
        // 2. 拨动
        if (cursor.advance() && overflowTimingWheel != null) {
            overflowTimingWheel.advanceClock();
        }
        LOG.info("cursor advance to {}", cursor.current());
        // 3. 执行
        TimerTaskList tasks = buckets[cursor.current()];
        if (tasks.hasTask()) {
            if (underflowTimingWheel != null) {
                tasks.foreach(task -> {
                    LOG.warn("发往下层时间轮 {}", task);
                    underflowTimingWheel.add(task, task.getDelay() - underflowTimingWheel.interval);
                });
            } else {
                tasks.foreach(TimerTask::perform);
            }
        }

    }

    private void addOverflowTimingWheel() {
        overflowTimingWheel = new ArrayBasedTimingWheel(wheelSize, tickMs * wheelSize);
        overflowTimingWheel.underflowTimingWheel = this;
    }

    private void advanceCalendar() {
        calendar.add(TimeUnitSupport.toCalendarUnit(TimeUnit.MILLISECONDS), (int) tickMs);
    }

    private void tick() {
        if (underflowTimingWheel != null) {
            return;
        }
        long tickMs = realTickMs();
        try {
            TimeUnit.MILLISECONDS.sleep(tickMs);
        } catch (InterruptedException ex) {
            System.out.println("TimingWheel finish ticking ... ");
        } finally {
            advanceCalendar();
        }
    }

    private long realTickMs() {
        return calendar.getTimeInMillis() - System.currentTimeMillis();
    }
}
