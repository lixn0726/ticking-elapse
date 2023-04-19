package indl.lixn.tickingelapse.delay;

import java.util.concurrent.BlockingQueue;

/**
 * @author listen
 **/
public class DelayTaskConsumer extends Thread {

    private final BlockingQueue<DelayTask> queue;

    public DelayTaskConsumer(BlockingQueue<DelayTask> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        DelayTask task = null;
        try {
            while (true) {
                task = queue.take();
                task.execTask();
                DelayTask.taskCount.decrementAndGet();
            }
        } catch (InterruptedException ex) {
            System.out.println(getName() + " finished");
        }
    }
}
