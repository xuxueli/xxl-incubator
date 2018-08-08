package com.tmp.demo.ssh;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.Strand;
import co.paralleluniverse.strands.SuspendableRunnable;
import jsr166e.LongAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    static public void main(String[] args) throws ExecutionException, InterruptedException {
        int count = 1000;  // 10000
        testThreadpool(count);
        testFiber(count);
    }


    @Suspendable
    static void m1() throws InterruptedException, SuspendExecution {
        String m = "m1";
        //System.out.println("m1 begin");
        m = m2();
        //System.out.println("m1 end");
        //System.out.println(m);
    }
    static String m2() throws SuspendExecution, InterruptedException {
        String m = m3();
        Strand.sleep(1000);
        return m;
    }
    //or define in META-INF/suspendables
    @Suspendable
    static String m3() {
        List<Integer> list = new ArrayList<>();
        for (int item:Arrays.asList(1,2,3)) {
            if (item%2 == 0) {
                list.add(item);
            }
        }
        return list.toString();
    }

    static void testThreadpool(int count) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(count);
        ExecutorService es = Executors.newFixedThreadPool(200);
        final LongAdder latency = new LongAdder();
        long t = System.currentTimeMillis();
        for (int i =0; i< count; i++) {
            es.submit(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    try {
                        m1();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SuspendExecution suspendExecution) {
                        suspendExecution.printStackTrace();
                    }
                    start = System.currentTimeMillis() - start;
                    latency.add(start);
                    latch.countDown();
                }
            });
        }
        latch.await();
        t = System.currentTimeMillis() - t;
        long l = latency.longValue() / count;
        System.out.println("thread pool took: " + t + ", latency: " + l + " ms");
        es.shutdownNow();
    }
    static void testFiber(int count) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(count);
        final LongAdder latency = new LongAdder();
        long t = System.currentTimeMillis();
        for (int i =0; i< count; i++) {
            new Fiber<Void>("Caller", new SuspendableRunnable() {
                @Override
                public void run() throws SuspendExecution, InterruptedException {
                    long start = System.currentTimeMillis();
                    m1();
                    start = System.currentTimeMillis() - start;
                    latency.add(start);
                    latch.countDown();
                }
            }).start();
        }
        latch.await();
        t = System.currentTimeMillis() - t;
        long l = latency.longValue() / count;
        System.out.println("fiber took: " + t  + ", latency: " + l + " ms");
    }

}
