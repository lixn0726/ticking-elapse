package indl.lixn.tickingelapse.wheel.complex.consumer;

import java.util.concurrent.ExecutorService;

/**
 * @author listen
 **/
public class TaskExecutorPool {

    private ExecutorService pool;

    public void reset() {
        this.pool.shutdownNow();
    }

}
