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

    public List<Timeout> getExpireTimeouts() {
        return new ArrayList<Timeout>(timeouts);
    }

}
