package indl.lixn.tickingelapse.wheel.simple;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author listen
 **/
public class TimeUnitSupport {

    private static final Map<TimeUnit, Integer> CALENDER_UNIT_BY_TIME_UNIT = new HashMap<>();

    static {
        CALENDER_UNIT_BY_TIME_UNIT.put(TimeUnit.SECONDS, Calendar.SECOND);
        CALENDER_UNIT_BY_TIME_UNIT.put(TimeUnit.MINUTES, Calendar.MINUTE);
        CALENDER_UNIT_BY_TIME_UNIT.put(TimeUnit.HOURS, Calendar.HOUR);
        CALENDER_UNIT_BY_TIME_UNIT.put(TimeUnit.MILLISECONDS, Calendar.MILLISECOND);
        CALENDER_UNIT_BY_TIME_UNIT.put(TimeUnit.DAYS, Calendar.DATE);
    }

    public static int toCalendarUnit(TimeUnit unit) {
        return CALENDER_UNIT_BY_TIME_UNIT.get(unit);
    }

}
