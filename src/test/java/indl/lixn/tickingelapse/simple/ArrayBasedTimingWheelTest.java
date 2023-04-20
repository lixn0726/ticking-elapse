package indl.lixn.tickingelapse.simple;

import indl.lixn.tickingelapse.common.LogWrapper;
import indl.lixn.tickingelapse.entity.dataobject.TimingWheelConfig;
import indl.lixn.tickingelapse.support.TaskImpl;
import indl.lixn.tickingelapse.wheel.simple.ArrayBasedTimingWheel;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author listen
 **/
public class ArrayBasedTimingWheelTest {

    private static final LogWrapper LOG = LogWrapper.instance;

    @Test
    public void test_ticking() {
        ArrayBasedTimingWheel timingWheel = new ArrayBasedTimingWheel(new TimingWheelConfig(1, 10, TimeUnit.SECONDS));

        timingWheel.start();

        timingWheel.add(new TaskImpl(1, TimeUnit.SECONDS));  // 5
//        timingWheel.add(new TaskImpl(10, TimeUnit.SECONDS)); // 10

        sleepForSecond(3);
//
//        timingWheel.add(new TaskImpl(3, TimeUnit.SECONDS)); // 6
//
//        timingWheel.add(new TaskImpl(13, TimeUnit.SECONDS)); // 16

        sleepOneDay();
    }

    private void sleepOneDay() {
        try {
            TimeUnit.DAYS.sleep(1);
        } catch (InterruptedException ex) {
            System.out.println("app stop running");
        }
    }

    private void sleepForSecond(int num) {
        try {
            TimeUnit.SECONDS.sleep(num);
        } catch (InterruptedException ex) {
            System.out.println("app stop running");
        }
    }

}
