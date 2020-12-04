package com.tmp.demo.ssh.pipeline2;

public interface Valve<Context, E extends Exception> {
    /**
     * 阀门处理接口。根据context中的信息，进行处理。ValveChain用于控制
     * 是否需要执行下一个阀门.
     *
     * @param context 阀门上下文
     * @param chain   阀门控制链,控制是否需要继续执行管道中的阀门.
     */
    public void handle(Context context, ValveChain<Context, E> chain)
            throws E;
}
