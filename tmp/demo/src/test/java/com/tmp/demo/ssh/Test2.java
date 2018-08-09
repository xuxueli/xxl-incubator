package com.tmp.demo.ssh;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.Strand;
import co.paralleluniverse.strands.SuspendableRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class Test2 {
    private static Logger logger = LoggerFactory.getLogger(FiberHelper.class);


    public static void main(String[] args) throws InterruptedException {

        int count = 100 * 10000;
        final CountDownLatch latch = new CountDownLatch(count);

        long start = System.currentTimeMillis();
        for (int i =0; i< count; i++) {
            final int finalI = i;
            Fiber fiber = new Fiber<Void>("Caller", new SuspendableRunnable() {
                @Override
                public void run() throws SuspendExecution, InterruptedException {

                    /*try {
                        // park
                        Fiber.park(2L, TimeUnit.SECONDS);
                        System.out.println("i = " + finalI);
                    } catch (SuspendExecution suspendExecution) {
                        logger.error(suspendExecution.getMessage(), suspendExecution);
                    } finally {
                        // unpark（invoke by future）
                        Fiber currentFiber = Fiber.currentFiber();
                        currentFiber.unpark();
                    }*/


                    // do future invoke
                    rpcInvoke();

                    latch.countDown();
                }
            }).start();
        }
        latch.await();

        long cost = System.currentTimeMillis() - start;
        System.out.println("fiber cost: " + cost + " ms");

    }

    @Suspendable
    static void rpcInvoke() throws InterruptedException, SuspendExecution {
        Strand.sleep(3000);
    }



}
