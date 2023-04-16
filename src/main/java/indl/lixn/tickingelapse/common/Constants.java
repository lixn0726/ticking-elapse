package indl.lixn.tickingelapse.common;

import java.time.Clock;

/**
 * @author listen
 **/
public class Constants {

    public static final Clock CLOCK = Clock.systemUTC();

    public static final LogWrapper LOG = new LogWrapper(Constants.class);

}
