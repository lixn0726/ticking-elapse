package indl.lixn.tickingelapse.exception;

/**
 * @author listen
 **/
public class StopException extends RuntimeException {

    public static final StopException instance = new StopException();

    private StopException() {}

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
