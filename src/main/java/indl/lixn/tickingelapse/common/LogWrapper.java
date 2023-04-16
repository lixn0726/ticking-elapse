package indl.lixn.tickingelapse.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author listen
 **/
public class LogWrapper {

    private static final String DOT = ".";

    private static final String DEFAULT_SEPARATOR = " >>> ";

    private final Logger logger;

    private final String simpleName;

    private final String separator;

    public LogWrapper(Class<?> clazz) {
        this(clazz, DEFAULT_SEPARATOR);
    }

    public LogWrapper(Class<?> clazz, String separator) {
        this.logger = LoggerFactory.getLogger(clazz);
        this.simpleName = clazz.getSimpleName();
        this.separator = separator;
    }

    public void info(String format, Object...args) {
        StackTraceElement caller = getCurrentCaller();
        logger.info(simpleName + DOT + caller.getMethodName() + separator + format, args);
    }

    public void error(String format, Object... args) {
        StackTraceElement caller = getCurrentCaller();
        logger.error(simpleName + DOT + caller.getMethodName() + separator + format, args);
    }

    public void warn(String format, Object... args) {
        StackTraceElement caller = getCurrentCaller();
        logger.warn(simpleName + DOT + caller.getMethodName() + separator + format, args);
    }

    private StackTraceElement getCurrentCaller() {
        // [0]: getStackTrace();
        // [1]: info(String format, Object...args)
        return Thread.currentThread().getStackTrace()[3];
    }



}
