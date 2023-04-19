package indl.lixn.tickingelapse.by_kafka;

/**
 * @author listen
 **/
public interface TimerTask {

    void cancel();

    void perform();

}
