package com.tmp.demo.ssh;

import java.util.ArrayList;
import java.util.List;

public class Test3 {

    public static void main(String[] args) {
        /**
         * A --> B --> C
         *
         * 拆分：
         * A > B
         * B > C
         */
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");

        List<Step> stepList = new ArrayList<>();
        stepList.add(new Step(a, b));
        stepList.add(new Step(b, c));

        // process
        process(stepList);

        /**
         * A -->
         *      --> C
         * B -->
         *
         * 拆分：
         * A > C
         * B > C
         */

        /**
         *      --> B
         * A -->     --> D
         *      --> C
         * 拆分：
         * A > B
         * A > C
         * B > D
         * C > D
         *
         */
    }

    // dag process
    public static void process(List<Step> stepList){
        // valid
        Node start = null;
        for (Step step:stepList) {

            // parent node
            Node parentNode = null;
            for (Step step2:stepList) {
                if (step2.to == step.getFrom()) {
                    parentNode = step2.from;
                }
            }

            if (parentNode == null) {
                Node currentHead = step.getFrom();
                if (start == null) {
                    start = parentNode;
                } else {
                    if (start!=null && start.name.equals(currentHead.name)) {
                        System.out.println("起始节点冲突：start=" + start + "， currentHead=" + currentHead);
                    }
                }
            }

        }
        System.out.println("start = " + start);
    }

    // base model

    public static class Step{
        private Node from;
        private Node to;
        public Step(Node from, Node to) {
            this.from = from;
            this.to = to;
        }
        public Node getFrom() {
            return from;
        }
        public Node getTo() {
            return to;
        }
    }

    public static class Node{
        private String name;
        public Node(String name) {
            this.name = name;
        }
        public void execute(){
            System.out.println("Node execute:" + name);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
