package indl.lixn.tickingelapse.wheel.complex;

import indl.lixn.tickingelapse.wheel.TimerTask;
import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author listen
 **/
@Data
public class TimerTaskLinkedList implements Delayed {

    private final TimerTaskEntry root = new TimerTaskEntry(null, -1);

    private final AtomicInteger taskCounter = new AtomicInteger(0);

    private final AtomicLong expiration = new AtomicLong(-1);

    public void add(TimerTaskEntry timerTaskEntry) {
        boolean done = false;
        synchronized (this) {
            while (!done) {
                // 清空原本的list，让这个Entry属于自己这个list
                timerTaskEntry.remove();

                if (timerTaskEntry.list == null) {
                    TimerTaskEntry tail = root.prev;
                    timerTaskEntry.next = root;
                    timerTaskEntry.prev = tail;
                    timerTaskEntry.list = this;
                    taskCounter.incrementAndGet();
                    done = true;
                }
            }
        }
    }

    public boolean setExpiration(long expiration) {
        return this.expiration.getAndSet(expiration) != expiration;
    }

    public long getExpiration() {
        return this.expiration.get();
    }

    public void remove(TimerTaskEntry timerTaskEntry) {
        synchronized (this) {
            if (timerTaskEntry.list.equals(this)) {
                timerTaskEntry.next.prev = timerTaskEntry.prev;
                timerTaskEntry.prev.next = timerTaskEntry.next;
                timerTaskEntry.next = null;
                timerTaskEntry.prev = null;
                timerTaskEntry.list = null;
                taskCounter.decrementAndGet();
            }
        }
    }

    public void flush() {
        TimerTaskEntry head = root.next;
        while (!head.equals(root)) {
            remove(head);
            head = root.next;
        }
        this.expiration.set(-1);
    }


    @Override
    public long getDelay(TimeUnit unit) {
        return unit.toMillis(this.expiration.get());
    }

    @Override
    public int compareTo(Delayed o) {
        long oDelay = o.getDelay(TimeUnit.MILLISECONDS);
        long delay = this.getDelay(TimeUnit.MILLISECONDS);
        return Long.compare(oDelay, delay);
    }

    @Data
    public static class TimerTaskEntry implements Comparable<Long> {

        private TimerTask task;

        private long delay;

        private volatile TimerTaskLinkedList list;

        private volatile TimerTaskEntry prev;

        private volatile TimerTaskEntry next;

        public TimerTaskEntry(TimerTask task, long delay) {
            this.task = task;
            this.delay = delay;
        }

        public void remove() {
            TimerTaskLinkedList currentList = list;
            while (currentList != null) {
                currentList.remove(this);
                currentList = list;
            }
        }

        public boolean cancel() {
            return task.getTimerTaskEntry().equals(this);
        }

        @Override
        public int compareTo(Long o) {
            return Long.compare(this.delay, o);
        }
    }

}
