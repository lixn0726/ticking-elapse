package indl.lixn.tickingelapse.entity.dataobject;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author listen
 **/
@Data
public class TimingWheelConfig {

    private long tickMs;

    private int bucketSize;

    public TimingWheelConfig(long tickMs, int bucketSize, TimeUnit unit) {
        this.tickMs = unit.toMillis(tickMs);
        this.bucketSize = bucketSize;
    }
}
