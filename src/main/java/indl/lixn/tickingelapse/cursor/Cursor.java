package indl.lixn.tickingelapse.cursor;

/**
 * @author listen
 **/
public interface Cursor {

    void move();

    int maxStep();

    int currentStep();

    void reset();

}
