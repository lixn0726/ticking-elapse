package indl.lixn.tickingelapse.support;

import indl.lixn.tickingelapse.common.Performer;
import indl.lixn.tickingelapse.timeout.Timeout;
import indl.lixn.tickingelapse.util.TimeUnitUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author listen
 **/
public class TimeoutImpl implements Timeout {

    private long delayMs;

    private TimeUnit unit;

    private int delayOfUnit;

    private Performer performer;

    public TimeoutImpl(TimeUnit unit, int delayOfUnit, Performer performer) {
        this.unit = unit;
        this.delayOfUnit = delayOfUnit;
        this.performer = performer;
        this.delayMs = TimeUnitUtil.scaleToMs(this.unit) * this.delayOfUnit;
    }

    @Override
    public long getDelayed() {
        return this.delayMs;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public void perform() {
        performer.perform();
    }
}
