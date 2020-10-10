package com.tmp.demo.ssh.dag;


import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;

import java.io.*;
import java.util.Map;

public class Test {

    public static void main(String[] args) {

        InputStream inputStream = new ByteArrayInputStream(new String("digraph dagjob {\n" +
                "\ta1 -> a2;\n" +
                "\ta1 -> a3;\n" +
                "\ta2 -> a4;\n" +
                "\ta3 -> a4;\n" +
                "\ta1[style=filled,color=\"#00a65a\", state=\"完成\"];\n" +
                "\ta2[style=filled,color=\"#f39c12\", state=\"进行中\"];\n" +
                "\ta3[style=filled,color=\"#dd4b39\", state=\"失败\"];\n" +
                "\ta4[style=filled,color=\"gray\", state=\"未开始\"];\n" +
                "}").getBytes());

        GraphParser parser = new GraphParser(inputStream);
        Map<String, GraphNode> nodes = parser.getNodes();
        Map<String, GraphEdge> edges = parser.getEdges();

        System.out.println("--- nodes:");
        for (GraphNode node : nodes.values()) {
            System.out.println(node.getId() + " " + node.getAttributes());
        }

        System.out.println("--- edges:");
        for (GraphEdge edge : edges.values()) {
            System.out.println(edge.getNode1().getId() + "->" + edge.getNode2().getId() + " " + edge.getAttributes());
        }

    }

}
