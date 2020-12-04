package com.tmp.demo.ssh.pipeline2;

public interface Pipeline<Context, E extends Exception> extends Valve<Context, E> {

    public void handle(Context context) throws E;

}