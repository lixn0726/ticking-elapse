package indl.lixn.tickingelapse.cursor;

import static indl.lixn.tickingelapse.common.Constants.LOG;

/**
 * @author listen
 **/
public class CursorImpl implements Cursor {

    private final int initial;

    private int step;

    private final int maxStep;

    public CursorImpl(int initial, int maxStep) {
        this.initial = initial;
        this.maxStep = maxStep;
    }

    @Override
    public void move() {
        this.step++;
        ensureStep();
    }

    private void ensureStep() {
        if (this.step == this.maxStep) {
            this.step = this.initial;
        }
    }

    @Override
    public int maxStep() {
        return this.maxStep;
    }

    @Override
    public int currentStep() {
        return this.step;
    }

    @Override
    public void reset() {
        this.step = this.initial;
    }
}
