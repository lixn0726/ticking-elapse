package indl.lixn.tickingelapse.facade;

import indl.lixn.tickingelapse.impl.TimeoutExecuteWheelTimer;
import indl.lixn.tickingelapse.WheelTimer;
import indl.lixn.tickingelapse.config.ConfigImpl;
import indl.lixn.tickingelapse.config.WheelTimerConfig;
import indl.lixn.tickingelapse.cursor.Cursor;
import indl.lixn.tickingelapse.cursor.CursorImpl;
import indl.lixn.tickingelapse.exception.StopException;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author listen
 **/
@Data
public class WheelTimerBuilder {

    private Cursor cursor;

    private WheelTimerConfig config;

    public static WheelTimerBuilder single() {
        return new WheelTimerBuilder();
    }

    public static WheelTimerBuilder single(TimeUnit unit, long tickDuration, int tickCount) {
        return single(new ConfigImpl(unit, tickDuration, tickCount));
    }

    public static WheelTimerBuilder single(WheelTimerConfig config) {
        WheelTimerBuilder builder = new WheelTimerBuilder();
        builder.setConfig(config);
        return builder;
    }

    public WheelTimer forOneMinute() {
        return new TimeoutExecuteWheelTimer();
    }

    public WheelTimerBuilder config(WheelTimerConfig config) {
        this.config = config;
        this.cursor = new CursorImpl(0, config.tickCount());
        return this;
    }

    public WheelTimer build() {
        if (this.cursor == null || this.config == null) {
            throw StopException.instance;
        }
        return new TimeoutExecuteWheelTimer(this.cursor, this.config);
    }
}
