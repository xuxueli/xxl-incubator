package com.xxl.fiber;

import co.paralleluniverse.common.monitoring.MonitorType;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberForkJoinScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.strands.SuspendableRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * fiber helper
 *
 * @author xuxueli 2018-07-08 19:07:46
 */
public class FiberHelper {
    private static Logger logger = LoggerFactory.getLogger(FiberHelper.class);

    private static final FiberScheduler instance;

    static {
        instance = new FiberForkJoinScheduler("fiber-helper-pool", 20, null, MonitorType.JMX, false);
    }

    private static FiberScheduler defaultScheduler() {
        return instance;
    }

    /**
     * submit fiber task
     *
     * @param fiberTaskList  task list
     * @param maxWaitSeconds sync wait max seconds
     * @return
     */
    public static boolean submit(List<SuspendableRunnable> fiberTaskList, long maxWaitSeconds) {
        if (fiberTaskList == null || fiberTaskList.size() == 0) {
            return false;
        }
        if (maxWaitSeconds < 1) {
            throw new RuntimeException("maxWaitSeconds is invalid.");
        }

        final CountDownLatch countDownLatch = new CountDownLatch(fiberTaskList.size());
        List<Fiber> fiberList = new ArrayList<Fiber>();
        for (final SuspendableRunnable fiberTask : fiberTaskList) {
            Fiber fiber = new Fiber<Void>("FiberTask", defaultScheduler(), fiberTask) {
                @Override
                protected void onCompletion() {
                    countDownLatch.countDown();
                    super.onCompletion();
                }

                @Override
                protected void onException(Throwable t) {
                    countDownLatch.countDown();
                    logger.error(t.getMessage(), t);
                    super.onException(t);
                }
            }.start();
            fiberList.add(fiber);
        }

        // fibers wait until count down
        try {
            countDownLatch.await(maxWaitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            for (Fiber fiber : fiberList) {
                if (fiber.isAlive()) {
                    try {
                        fiber.cancel(true);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
        return true;
    }

}
