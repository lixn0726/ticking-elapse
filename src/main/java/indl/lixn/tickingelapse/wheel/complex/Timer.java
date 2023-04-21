package indl.lixn.tickingelapse.wheel.complex;

import ch.qos.logback.classic.layout.TTLLLayout;
import indl.lixn.tickingelapse.wheel.TimerTask;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author listen
 **/
public abstract class Timer {

    int size;

    abstract void add(TimerTask task);

    abstract boolean advanceClock(long timeoutMs);

    abstract void shutdown();

    public static class SystemTimer extends Timer {
        String executorName;

        long tickMs = 1;

        int wheelSize = 20;

        long startTime = System.currentTimeMillis();

        ExecutorService taskExecutor = Executors.newFixedThreadPool(1,
                (Thread::new));

        DelayQueue<TimerTaskLinkedList> delayQueue = new DelayQueue<>();

        AtomicInteger taskCounter = new AtomicInteger(0);

        TimingWheel timingWheel = new TimingWheel();

        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();

        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

        @Override
        void add(TimerTask task) {
            readLock.lock();
            try {
                addTimerTaskEntry(new TimerTaskLinkedList.TimerTaskEntry(task, task.getDelay() + System.currentTimeMillis()));
            } finally {
                readLock.unlock();
            }
        }

        private void addTimerTaskEntry(TimerTaskLinkedList.TimerTaskEntry timerTaskEntry) {
            if (!timingWheel.add(timerTaskEntry)) {
                if (!timerTaskEntry.cancel()) {
                    taskExecutor.submit(timerTaskEntry.getTask());
                }
            }
        }

        @Override
        boolean advanceClock(long timeoutMs) {
            try {
                TimerTaskLinkedList bucket = delayQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
                if (bucket != null) {
                    writeLock.lock();
                    try {
                        while (bucket != null) {
                            timingWheel.advanceClock(bucket.getExpiration());
                            bucket.flush();
                            bucket = delayQueue.poll();
                        }
                    } finally {
                        writeLock.unlock();
                    }
                    return true;
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return false;
        }

        @Override
        void shutdown() {
            taskExecutor.shutdown();
        }
    }

}
