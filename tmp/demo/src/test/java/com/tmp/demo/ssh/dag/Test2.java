package com.tmp.demo.ssh.dag;


import com.google.common.graph.*;
import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class Test2 {

    public static void main(String[] args) {

        MutableGraph<Integer> graph1 = GraphBuilder.directed() //指定为有向图
                .nodeOrder(ElementOrder.<Integer>insertion()) //节点按插入顺序输出  //(还可以取值无序unordered()、节点类型的自然顺序natural())
                .expectedNodeCount(50) //预期节点数
                .allowsSelfLoops(false) //允许自环
                .build();

        graph1.putEdge(1, 2);
        graph1.putEdge(1, 3);
        graph1.putEdge(2, 4);
        graph1.putEdge(3, 1);

        //返回图中所有的节点(顺序依赖nodeOrder)
        Set<Integer> nodes = graph1.nodes();
        System.out.println("graph1 nodes count:" + nodes.size() + ", nodes value:" + nodes);

        //返回图中所有的边集合
        Set<EndpointPair<Integer>> edges = graph1.edges();
        System.out.println("graph1 edge count:" + edges.size() + ", edges value:" + edges);

        System.out.println("initlized graph1: " + graph1);

        System.out.println( "前驱结点："+graph1.predecessors(1) );;
        System.out.println("是否有环：" + Graphs.hasCycle(graph1) );

    }

}
