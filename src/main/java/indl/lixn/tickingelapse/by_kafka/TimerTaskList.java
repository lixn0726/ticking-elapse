package indl.lixn.tickingelapse.by_kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author listen
 **/
public class TimerTaskList implements Delayed {

    private final List<TimerTask> taskEntryList = new ArrayList<>();

    private long delay;

    public void add(TimerTask task) {
        this.taskEntryList.add(task);
    }

    public void foreach(Consumer<TimerTask> taskConsumer) {
        for (TimerTask task : taskEntryList) {
            taskConsumer.accept(task);
        }
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.toMillis(this.delay);
    }

    @Override
    public int compareTo(Delayed o) {
        return Long.compare(o.getDelay(TimeUnit.MILLISECONDS), this.getDelay(TimeUnit.MILLISECONDS));
    }

}
