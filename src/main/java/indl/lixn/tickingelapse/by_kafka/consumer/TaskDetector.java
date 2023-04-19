package indl.lixn.tickingelapse.by_kafka.consumer;

import indl.lixn.tickingelapse.by_kafka.TimerTaskList;
import indl.lixn.tickingelapse.by_kafka.TimingWheel;

import java.util.concurrent.TimeUnit;

/**
 * @author listen - Leader
 **/
public class TaskDetector implements Runnable {

    private TimingWheel wheel;

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            TimerTaskList list = wheel.getBuckets().poll();
            if (list == null) {
                continue;
            }
            long delay = list.getDelay(TimeUnit.MILLISECONDS);
            if (delay <= 0) {
                list.foreach(t -> {
                    System.out.println("Need to define execution logic");
                });
            }
            try {
                Thread.currentThread().wait(delay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
