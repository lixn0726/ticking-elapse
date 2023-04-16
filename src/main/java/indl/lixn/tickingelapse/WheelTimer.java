package indl.lixn.tickingelapse;

import indl.lixn.tickingelapse.config.WheelTimerConfig;
import indl.lixn.tickingelapse.cursor.Cursor;
import indl.lixn.tickingelapse.timeout.Timeout;

/**
 * @author listen
 **/
public interface WheelTimer {

    // 这里我把他做成一个public的接口，一开始是为了让下一层可以拨动上一层的时间轮
    // 让上层时间轮走动，但是其实上层时间轮对于下层来说就是一个存放timeout的地方
    // 是不是没有必要让他实现这个接口
    void start();

    void stop();

    void addTimeout(Timeout timeout);

    boolean isRunning();

    Cursor cursor();

    WheelTimerConfig config();

}
