package com.tmp.demo.ssh.pipeline2;

public interface ValveChain<Context, E extends Exception> {
    public void handleNext(Context context) throws E;
}
