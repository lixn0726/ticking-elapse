package indl.lixn.tickingelapse.config;

import indl.lixn.tickingelapse.exception.StopException;
import indl.lixn.tickingelapse.util.TimeUnitUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author listen
 **/
public class ConfigImpl implements WheelTimerConfig {

    private final TimeUnit unit;

    private final int tickCount;

    private final long tickDuration;

    private final long maxDuration;

    private long tickDurationMs;

    private long maxDurationMs;

    public ConfigImpl(TimeUnit unit, long tickDuration, int tickCount) {
        this(unit, tickDuration, tickCount, tickDuration * tickCount);
    }


    public ConfigImpl(TimeUnit unit, long tickDuration, int tickCount, long maxDuration) {
        if (maxDuration % tickDuration != 0) {
            throw StopException.instance;
        }
        this.unit = unit;
        this.tickDuration = tickDuration;
        this.tickCount = tickCount;
        this.maxDuration = maxDuration;
        transferToMs();
    }

    private void transferToMs() {
        long scaleToMs = TimeUnitUtil.scaleToMs(this.unit);
        this.tickDurationMs = this.tickDuration * scaleToMs;
        this.maxDurationMs = this.maxDuration * scaleToMs;
    }

    @Override
    public TimeUnit unit() {
        return this.unit;
    }

    @Override
    public long tickDuration() {
        return this.tickDuration;
    }

    @Override
    public long maxDuration() {
        return this.maxDuration;
    }

    @Override
    public long tickDurationMs() {
        return this.tickDurationMs;
    }

    @Override
    public long maxDurationMs() {
        return this.maxDurationMs;
    }

    @Override
    public int tickCount() {
        return this.tickCount;
    }

    @Override
    @Deprecated
    public boolean illegal() {
        return true;
    }
}
