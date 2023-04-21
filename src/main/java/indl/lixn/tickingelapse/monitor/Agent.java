package indl.lixn.tickingelapse.monitor;

import java.lang.instrument.Instrumentation;

/**
 * @author listen
 **/
public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        // 将线程池拦截器加到程序中
        inst.addTransformer(new ThreadPoolTransformer());
    }

}
