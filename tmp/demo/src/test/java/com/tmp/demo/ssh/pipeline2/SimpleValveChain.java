package com.tmp.demo.ssh.pipeline2;

import java.util.List;

public class SimpleValveChain<Context, E extends Exception> extends BaseValveChain<Valve<Context, E>> implements ValveChain<Context, E> {
 
    public SimpleValveChain(List<Valve<Context, E>> valves) {
        super(valves);
    }
 
    @Override
    public void handleNext(Context context) throws E {
        if (index < size) {
            valves.get(index++).handle(context, this);
        }
    }
}
