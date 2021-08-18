package com.tmp.demo.ssh;

public class Test5 {

    /**
     *
     * 场景：一次调度
     *      1、单机：1条log
     *      2、重试：1条log + N条重试log
     *      3、分片：N条分片Log
     *      4、分片：N条分片Log + N条重试log
     *
     * 概念：
     *      分片：概念统一，单机是单节点的重试；
     *          - 0/1
     *          - 0/2 + 1/2
     *      重试：概念统一，单次是重试此时为0
     *
     *  日志结构：
     *      日志组：
     *          分片1：
     *              重试1
     *              重试2
     *          分片2：
     *              重试1
     *  日志表：
     *      日志组：
     *
     */

}
