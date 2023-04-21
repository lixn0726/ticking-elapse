package indl.lixn.tickingelapse.wheel.complex.consumer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author listen - Leader
 **/
public class TaskDetector implements Runnable {

    private static final AtomicInteger detectorCount = new AtomicInteger(0);



    private String detectorName;

    @Override
    public void run() {
//        while (!Thread.currentThread().isInterrupted()) {
//            TimerTaskList list = wheel.getBuckets().poll();
//            if (list == null) {
//                continue;
//            }
//            long delay = list.getDelay(TimeUnit.MILLISECONDS);
//            if (delay <= 0) {
//                list.foreach(t -> {
//                    System.out.println("Need to define execution logic");
//                });
//            }
//            try {
//                Thread.currentThread().wait(delay);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//        }
    }

}
