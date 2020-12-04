package com.tmp.demo.ssh.pipeline2;

import java.util.List;

public abstract class BaseValveChain<V> {
    protected List<V> valves = null;
 
    protected int index = 0;
 
    protected int size;
 
    public BaseValveChain(List<V> valves) {
        this.valves = valves;
        this.size = valves.size();
    }
 
    public BaseValveChain(List<V> valves, int index) {
        this(valves);
        this.index = index;
    }
}
