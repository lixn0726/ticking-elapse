package indl.lixn.tickingelapse.wheel;

/**
 * @author listen
 **/
public interface TimerTask {

    /**
     * 取消任务
     */
    void cancel();

    /**
     * 执行任务
     */
    void perform();

    long getDelay();

}
