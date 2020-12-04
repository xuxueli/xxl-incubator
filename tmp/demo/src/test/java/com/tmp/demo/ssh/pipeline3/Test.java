package com.tmp.demo.ssh.pipeline3;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {

        Pipeline pipeline = initPipeline(
                Arrays.asList(
                        new DemoPipeline("1"),
                        new DemoPipeline("2"),
                        new DemoPipeline("action"),
                        new DemoPipeline("3")
                ));
        System.out.println("pipeline : " +  pipeline.toString());

        pipeline.process(new PipelineContext());
    }

    public static Pipeline initPipeline(List<? extends Pipeline> pipelines) {
        if (pipelines == null || pipelines.isEmpty()) {
            throw new IllegalArgumentException("empty pipelines!");
        }

        DemoPipeline[] a = (DemoPipeline[]) pipelines.toArray(new DemoPipeline[0]);
        DemoPipeline p = a[0];
        for (int i = 1; i < a.length; ++i) {
            p.setNext(a[i]);
            p = a[i];
        }
        p.setNext(null);
        return a[0];
    }


    public static class DemoPipeline extends Pipeline {

        public DemoPipeline(String name) {
            this.setName(name);
        }

        @Override
        public void process(PipelineContext ctx) {
            // etc
            System.out.println("name=" + getName());

            this.forward(ctx);
        }

        @Override
        public void cancel(PipelineContext context) {
            // etc
        }

    }

    public static abstract class Pipeline {

        private String name;
        private Pipeline next;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Pipeline getNext() {
            return next;
        }

        public void setNext(Pipeline next) {
            this.next = next;
        }

        public abstract void process(PipelineContext context);
        public abstract void cancel(PipelineContext context);

        public void forward(PipelineContext context){
            if (next != null) {
                next.process(context);
            }
        }
        public String toString() {
            if (next != null) {
                return name + "->" + next.toString();
            }
            return name;
        }

    }

    public static class PipelineContext {

        private Map<String,String> contextData;

        public Map<String, String> getContextData() {
            return contextData;
        }
        public void setContextData(Map<String, String> contextData) {
            this.contextData = contextData;
        }

    }

}
