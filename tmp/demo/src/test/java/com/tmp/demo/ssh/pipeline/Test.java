package com.tmp.demo.ssh.pipeline;

import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        Pipeline<?> pipeline = OrdinaryPipeline.getInstance(
                Arrays.asList(
                        new DemoPipeline("1"),
                        new DemoPipeline("2"),
                        new DemoPipeline("action"),
                        new DemoPipeline("3")
                ));
        System.out.println("pipeline : " +  pipeline.toString());

        pipeline.process(new PipelineContext(), null);
    }

    private static final class DemoPipeline extends OrdinaryPipeline<String> {
        public DemoPipeline(String name) {
            super(name);
        }

        @Override
        public void process(PipelineContext ctx, String s) {
            // TODO
            System.out.println("name=" + getName());

            this.forward(ctx, s);
        }
    }

}
