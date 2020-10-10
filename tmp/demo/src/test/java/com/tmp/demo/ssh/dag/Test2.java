package com.tmp.demo.ssh.dag;


import com.google.common.graph.*;
import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test2 {

    public static void main(String[] args) {

        // init
        MutableGraph<Integer> graph1 = GraphBuilder.directed() //指定为有向图
                .nodeOrder(ElementOrder.<Integer>insertion()) //节点按插入顺序输出  //(还可以取值无序unordered()、节点类型的自然顺序natural())
                .expectedNodeCount(50) //预期节点数
                .allowsSelfLoops(false) //允许自环
                .build();

        /*graph1.putEdge(1, 2);
        graph1.putEdge(1, 3);
        graph1.putEdge(2, 4);
        graph1.putEdge(3, 4);*/

        graph1.putEdge(1, 2);
        graph1.putEdge(2, 3);
        graph1.putEdge(3, 1);
        System.out.println("initlized graph1: " + graph1);

        // dag run
        Set<Integer> allNodes = graph1.nodes();
        Set<Integer> invokedNode = new HashSet<>();

        for (Integer nodeItem: allNodes) {
            System.out.println(nodeItem + " - 入度 - " + graph1.inDegree(nodeItem) );
            System.out.println(nodeItem + " - 出度- " + graph1.outDegree(nodeItem) );
        }

        Boolean hasNewNode = null;
        while (hasNewNode==null || hasNewNode) {
            hasNewNode = false;
            boolean deadCycle = true;
            for (Integer nodeItem: allNodes) {
                // 入口任务
                if (graph1.inDegree(nodeItem) == 0) {
                    invokedNode.add(nodeItem);
                    System.out.println(nodeItem + " - 入口任务， Run - " + nodeItem);
                    deadCycle = false;
                    continue;
                }

                // 前置任务运行情况
                boolean preAllRuned = true;
                Set<Integer> preNodes = graph1.predecessors(1);
                if (preNodes!=null && preNodes.size()>0) {
                    for (Integer preNode:preNodes) {
                        if (!invokedNode.contains(preNode)) {
                            preAllRuned = false;
                        }
                    }
                }
                if (preAllRuned) {
                    System.out.println(nodeItem + " - 依赖任务全部执行， Run - " + nodeItem);
                    deadCycle = false;
                    continue;
                }

                hasNewNode = true;
            }
            if (deadCycle) {
                System.out.println("死循环，跳出");
                break;
            }
        }

        System.out.println("是否有环：" + Graphs.hasCycle(graph1) );

    }

}
