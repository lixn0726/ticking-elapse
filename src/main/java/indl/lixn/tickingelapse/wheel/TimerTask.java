package indl.lixn.tickingelapse.wheel;

import indl.lixn.tickingelapse.wheel.complex.TimerTaskLinkedList;

/**
 * @author listen
 **/
public interface TimerTask extends Runnable {

    /**
     * 取消任务
     */
    void cancel();

    /**
     * 执行任务
     */
    void perform();

    long getDelay();

    TimerTaskLinkedList.TimerTaskEntry getTimerTaskEntry();


}
