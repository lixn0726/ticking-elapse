package indl.lixn.tickingelapse.common;

import lombok.Data;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author listen
 **/
public class TimeUnitSupport {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Calendar unit mapping
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static final Map<TimeUnit, Integer> CALENDER_UNIT_BY_TIME_UNIT = new HashMap<>();

    static {
        CALENDER_UNIT_BY_TIME_UNIT.put(TimeUnit.SECONDS, Calendar.SECOND);
        CALENDER_UNIT_BY_TIME_UNIT.put(TimeUnit.MINUTES, Calendar.MINUTE);
        CALENDER_UNIT_BY_TIME_UNIT.put(TimeUnit.HOURS, Calendar.HOUR);
        CALENDER_UNIT_BY_TIME_UNIT.put(TimeUnit.MILLISECONDS, Calendar.MILLISECOND);
        CALENDER_UNIT_BY_TIME_UNIT.put(TimeUnit.DAYS, Calendar.DATE);
    }

    public static int calenderUnitOf(TimeUnit unit) {
        return CALENDER_UNIT_BY_TIME_UNIT.get(unit);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * up and low unit mapping
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private static final Map<TimeUnit, TimeUnitInfo> INFO_BY_TIMEUNIT = new HashMap<>();

    static {
        INFO_BY_TIMEUNIT.put(TimeUnit.MILLISECONDS, new TimeUnitInfo(TimeUnit.SECONDS, null, 1000L, Long.MIN_VALUE));
        INFO_BY_TIMEUNIT.put(TimeUnit.SECONDS, new TimeUnitInfo(TimeUnit.MINUTES, TimeUnit.MILLISECONDS, 60L, 1000L));
        INFO_BY_TIMEUNIT.put(TimeUnit.MINUTES, new TimeUnitInfo(TimeUnit.HOURS, TimeUnit.SECONDS, 60L, 60L));
        INFO_BY_TIMEUNIT.put(TimeUnit.HOURS, new TimeUnitInfo(TimeUnit.DAYS, TimeUnit.MINUTES, 24L, 60L));
        INFO_BY_TIMEUNIT.put(TimeUnit.DAYS, new TimeUnitInfo(null, TimeUnit.HOURS, Long.MAX_VALUE, 24L));
    }

    public static int scaleToUpper(TimeUnit unit) {
        return (int)INFO_BY_TIMEUNIT.get(unit).getScaleToUpper();
    }

    public static long scaleToMs(TimeUnit unit) {
        long res = 1L;
        if (TimeUnit.MILLISECONDS.equals(unit)) {
            return res;
        }
        TimeUnitInfo curInfo = INFO_BY_TIMEUNIT.get(unit);
        while (curInfo.getLower() != null) {
            res *= curInfo.getScaleToLower();
            curInfo = INFO_BY_TIMEUNIT.get(curInfo.getLower());
        }
        return res;
    }

    public static TimeUnit upperOf(TimeUnit unit) {
        return INFO_BY_TIMEUNIT.get(unit).getUpper();
    }

    @Data
    private static class TimeUnitInfo {

        private TimeUnit upper;

        private TimeUnit lower;

        private long scaleToUpper;

        private long scaleToLower;

        public TimeUnitInfo() {}

        public TimeUnitInfo(TimeUnit upper, TimeUnit lower, long scaleToUpper, long scaleToLower) {
            this.upper = upper;
            this.lower = lower;
            this.scaleToUpper = scaleToUpper;
            this.scaleToLower = scaleToLower;
        }
    }

}
