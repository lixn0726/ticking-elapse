package indl.lixn.tickingelapse.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author listen
 **/
public class LogWrapper {

    public static final LogWrapper instance = new LogWrapper();

    private static final String SHARP = "#";

    private final Logger logger = LoggerFactory.getLogger(LogWrapper.class);

    private LogWrapper() {}

    public void info(String format, Object...args) {
        StackTraceElement caller = getCurrentCaller();
        logger.info(caller.getClassName().substring(caller.getClassName().lastIndexOf(".") + 1) + SHARP + caller.getMethodName() + " >>> " + format, args);
    }

    public void error(String format, Object... args) {
        StackTraceElement caller = getCurrentCaller();
        logger.error(caller.getClassName().substring(caller.getClassName().lastIndexOf(".") + 1) + SHARP + caller.getMethodName() + " >>> " + format, args);
    }

    public void warn(String format, Object... args) {
        StackTraceElement caller = getCurrentCaller();
        logger.warn(caller.getClassName().substring(caller.getClassName().lastIndexOf(".") + 1) + SHARP + caller.getMethodName() + " >>> " + format, args);
    }

    private StackTraceElement getCurrentCaller() {
        // [0]: getStackTrace();
        // [1]: getCurrentCaller();
        // [2]: info/error/warn();
        ////////// 主要是把握好堆栈的长度
        return Thread.currentThread().getStackTrace()[3];
    }
    
}
