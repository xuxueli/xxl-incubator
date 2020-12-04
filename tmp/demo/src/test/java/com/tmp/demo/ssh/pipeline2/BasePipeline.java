package com.tmp.demo.ssh.pipeline2;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePipeline<V> {
    private List<V> valves;
 
    public BasePipeline(int initSize) {
        valves = new ArrayList<>(initSize);
    }
 
    public BasePipeline() {
        this(2);
    }
 
    /**
     * 增加阀门
     *
     * @param valve 阀门
     */
    public void addValve(V valve) {
        valves.add(valve);
    }
 
    /**
     * 删除任务阀门
     *
     * @param valve 任务阀门
     */
    public void removeValve(V valve) {
        valves.remove(valve);
    }
 
    /**
     * 返回所有阀门
     *
     * @return 管道中的阀门
     */
    public List<V> getValves() {
        return valves;
    }
 
    public void setValves(List<V> valves) {
        this.valves = valves;
    }
}
