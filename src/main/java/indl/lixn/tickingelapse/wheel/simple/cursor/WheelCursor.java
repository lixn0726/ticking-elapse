package indl.lixn.tickingelapse.wheel.simple.cursor;

/**
 * @author listen
 **/
public class WheelCursor {

    private int pointer;

    private final int limit;

    public WheelCursor(int limit) {
        this.limit = limit;
    }

    public boolean advance() {
        this.pointer++;
        return makeSureNotExceedLimit();
    }

    public int current() {
        return this.pointer;
    }

    private boolean makeSureNotExceedLimit() {
        if (this.pointer == this.limit - 1) {
            this.pointer = 0;
            return true;
        }
        return false;
    }


}
