package indl.lixn.tickingelapse.timeout;

/**
 * @author listen
 **/
public interface Timeout {

    long getDelayed();

    boolean isRepeatable();

    void perform();

}
