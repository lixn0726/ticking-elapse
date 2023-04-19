package indl.lixn.tickingelapse.bucket;

import indl.lixn.tickingelapse.timeout.Timeout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author listen
 **/
public class TimeoutBucket {

    private final List<Timeout> timeouts = new ArrayList<Timeout>();

    public void addTimeout(Timeout timeout) {
        this.timeouts.add(timeout);
    }

    /// 这里直接return所有 否则如果还需要整个遍历的话就没必要搞成层次的了
    public List<Timeout> getExpireTimeouts() {
        return new ArrayList<Timeout>(timeouts);
    }

}
