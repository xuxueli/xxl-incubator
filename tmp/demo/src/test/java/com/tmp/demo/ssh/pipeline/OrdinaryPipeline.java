package com.tmp.demo.ssh.pipeline;

import java.util.List;

public abstract class OrdinaryPipeline<T> implements Pipeline<T> {
    private final String name;
    private Pipeline<? super T> next;

    public OrdinaryPipeline(String name) {
        this.name = name;
    }

    public void setNext(Pipeline<? super T> next) {
        this.next = next;
    }

    public String getName() {
        return name;
    }

    @Override
    public abstract void process(PipelineContext ctx, T t);

    @Override
    public final void forward(PipelineContext ctx, T t) {
        if (next != null) {
            next.process(ctx, t);
        }
    }

    @Override
    public String toString() {
        if (next != null) {
            return name + "->" + next.toString();
        }
        return name;
    }

    public static <T> Pipeline<T> getInstance(List<? extends OrdinaryPipeline<? extends T>> pipelines) {
        if (pipelines == null || pipelines.isEmpty()) {
            throw new IllegalArgumentException("empty pipelines!");
        }
        @SuppressWarnings("unchecked")
        OrdinaryPipeline<T>[] a = (OrdinaryPipeline<T>[]) pipelines.toArray(new OrdinaryPipeline[0]);
        OrdinaryPipeline<T> p = a[0];
        for (int i = 1; i < a.length; ++i) {
            p.setNext(a[i]);
            p = a[i];
        }
        p.setNext(null);
        return a[0];
    }

}