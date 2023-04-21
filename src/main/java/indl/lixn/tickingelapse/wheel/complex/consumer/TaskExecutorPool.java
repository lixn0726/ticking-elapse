package indl.lixn.tickingelapse.wheel.complex.consumer;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;

/**
 * @author listen
 **/
public class TaskExecutorPool {

    private ThreadPoolExecutor pool;

    private final AtomicInteger submittedTaskCount = new AtomicInteger(0);

    public TaskExecutorPool() {}

    public TaskExecutorPool(
            int corePoolSize,
            int maxPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler rjtHandler) {
        if (corePoolSize < 0 || maxPoolSize <= 0 || corePoolSize > maxPoolSize || keepAliveTime < 0) {
            throw new IllegalArgumentException();
        }
        if (workQueue == null || threadFactory == null || rjtHandler == null) {
            throw new NullPointerException();
        }
        this.pool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, threadFactory, rjtHandler);
        // TODO 预热线程池，提前创建并启动所有的核心线程
        this.pool.prestartAllCoreThreads();
    }

    public void reset() {
        pool.shutdownNow();
    }

    public void submit(Runnable task) {
        submittedTaskCount.incrementAndGet();
        pool.submit(task);
    }

    public void execute(Runnable task) {
        submittedTaskCount.incrementAndGet();
        pool.execute(task);
    }

}
