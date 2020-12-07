package nju.iser;

import java.lang.instrument.Instrumentation;

public class MTraceAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new MTraceTransformer(), true);
    }
}
