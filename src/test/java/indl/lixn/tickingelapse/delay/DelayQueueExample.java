package indl.lixn.tickingelapse.delay;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

/**
 * @author listen
 **/
public class DelayQueueExample {

    public static void main(String[] args) {
        BlockingQueue<DelayTask> queue = new DelayQueue<>();

        for (int i = 0; i < 10; i++) {
            try {
                queue.put(new DelayTask("work " + i, 2000));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        ThreadGroup g = new ThreadGroup("Consumers");
        for (int i = 0; i < 1; i++) {
            new Thread(g, new DelayTaskConsumer(queue)).start();
        }

        while (DelayTask.taskCount.get() > 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        g.interrupt();
        System.out.println("Main Thread Finished");
    }

}
