package indl.lixn.tickingelapse.config;

import java.util.concurrent.TimeUnit;

/**
 * @author listen
 **/
public interface WheelTimerConfig {

    TimeUnit unit();

    long tickDuration();

    long maxDuration();

    long tickDurationMs();

    long maxDurationMs();

    int tickCount();

    default boolean illegal() {
        return maxDuration() % tickDuration() != tickCount();
    }

}
