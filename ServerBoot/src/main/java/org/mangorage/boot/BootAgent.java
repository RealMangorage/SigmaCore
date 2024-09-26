package org.mangorage.boot;

import java.lang.instrument.Instrumentation;

public class BootAgent {
    static String args;
    static Instrumentation instrumentation;

    /**
     * The agent premain entrypoint.
     *
     * @param arguments the arguments
     * @param instrumentation the instrumentation
     * @since 1.0.0
     */
    public static void premain(final String arguments, final Instrumentation instrumentation) {
        BootAgent.agentmain(arguments, instrumentation);
    }

    public static void agentmain(final String arguments, final Instrumentation instrumentation) {
        System.out.println("Started Up Boot Agent!");
        if (arguments == null || instrumentation == null)
            throw new IllegalStateException("Failed to get proper Agent...");
        BootAgent.args = arguments;
        BootAgent.instrumentation = instrumentation;
    }

    private BootAgent() {}
}
