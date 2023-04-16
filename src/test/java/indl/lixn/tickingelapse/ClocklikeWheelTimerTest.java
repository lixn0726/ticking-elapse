package indl.lixn.tickingelapse;

import indl.lixn.tickingelapse.common.Constants;
import indl.lixn.tickingelapse.common.LogWrapper;
import indl.lixn.tickingelapse.config.ConfigImpl;
import indl.lixn.tickingelapse.facade.WheelTimerBuilder;
import indl.lixn.tickingelapse.impl.TimeoutExecuteWheelTimer;
import indl.lixn.tickingelapse.support.TimeoutImpl;
import indl.lixn.tickingelapse.timeout.Timeout;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author listen
 **/
public class ClocklikeWheelTimerTest {

    private static final LogWrapper LOG = Constants.LOG;

    private void sleepADay() {
        try {
            TimeUnit.HOURS.sleep(24);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void test_common_wheel_timer() throws Exception {
        TimeoutExecuteWheelTimer one = new TimeoutExecuteWheelTimer();

        one.start();

        one.addTimeout(new Timeout() {
            @Override
            public long getDelayed() {
                return 5 * 1000L;
            }

            @Override
            public boolean isRepeatable() {
                return false;
            }

            @Override
            public void perform() {
                LOG.info("I'm supposed to be executed after 5 secs");
            }
        });

        TimeUnit.HOURS.sleep(24);
    }

    @Test
    public void test_builder_common() throws Exception {
        WheelTimer defaultTimer = WheelTimerBuilder.single().forOneMinute();

        defaultTimer.start();

        defaultTimer.addTimeout(new TimeoutImpl(TimeUnit.SECONDS, 10, () -> {
            LOG.info("I'm the second timeout");
        }));

        defaultTimer.addTimeout(new TimeoutImpl(TimeUnit.MILLISECONDS, 3000, () -> {
            LOG.info("I'm the first timeout");
        }));

        TimeUnit.HOURS.sleep(24);

    }

    @Test
    public void test_builder_custom() {
        WheelTimer defaultTimer = WheelTimerBuilder.single()
                .config(new ConfigImpl(TimeUnit.MILLISECONDS, 300, 10))
                .build();

        defaultTimer.start();

        sleepADay();
    }


}
