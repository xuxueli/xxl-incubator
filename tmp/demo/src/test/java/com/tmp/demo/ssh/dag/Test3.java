package com.tmp.demo.ssh.dag;

import java.util.Arrays;

public class Test3 {

    public static void main(String[] args) {

        String dot = "digraph dagjob {\n" +
                "\ta1 -> a2;\n" +
                "\ta1 -> a3;\n" +
                "\ta2 -> a4;\n" +
                "\ta3 -> a4;\n" +
                "\ta1[style=filled,color=\"#00a65a\", state=\"完成\"];\n" +
                "\ta2[style=filled,color=\"#f39c12\", state=\"进行中\"];\n" +
                "\ta3[style=filled,color=\"#dd4b39\", state=\"失败\"];\n" +
                "\ta4[style=filled,color=\"gray\", state=\"未开始\"];\n" +
                "}";

        String temp = dot.substring(dot.indexOf("{"), dot.lastIndexOf("}"));
        String[] edges = temp.split(";");
        for (String edge: edges) {
            if (edge.indexOf("->") == -1) {
                continue;
            }
            if (edge.indexOf("[") != -1) {
                edge = edge.substring(0, edge.indexOf("["));
            }
            edge = edge.replace(";", "");
            String[] nodeForEdge = edge.split("->");

            for (int i = 0; i < nodeForEdge.length; i++) {
                if (i+1 < nodeForEdge.length) {
                    System.out.println(nodeForEdge[i] + " -> " +nodeForEdge[i+1]);
                }
            }

            //System.out.println(Arrays.asList(nodeForEdge));

        }





    }
}
