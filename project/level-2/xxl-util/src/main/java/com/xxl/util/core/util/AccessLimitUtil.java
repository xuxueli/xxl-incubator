package com.xxl.util.core.util;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * access limit for throttle
 *
 * @author xuxueli 2017-08-13 20:06:43
 */
public class AccessLimitUtil {


    public static void main(String[] args) throws InterruptedException {
        final LinkedBlockingQueue<Double> costList = new LinkedBlockingQueue<Double>();
        final RateLimiter rateLimiter = RateLimiter.create(200);
        final AtomicInteger successNum = new AtomicInteger();

        //TimeUnit.MILLISECONDS.sleep(3000);

        final int type = 2;
        int runNum = 1000;

        final long[] allStart = {0};
        final long[] allStop = {0};

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < runNum; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {

                    try {
                        TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    /**
                     * Tips:
                     *      RateLimiter令牌初始化：sleep大于1s会初始化1s的令牌数，sleep小于1s会按比例初始化对应的令牌数；
                     *      RateLimiter生成令牌：会平滑的生成令牌，二位坐标情况下是条斜直线，获取令牌速度不可高度生成速度，保证QPS不可高于限制；
                     *
                     * 1、阻塞方式/acquire()：超过permits会被阻塞，直到获取到请求，返回阻塞时间/second
                     *      限制PQS-200/s，200线程并发请求一次，耗时96.96s，平均阻塞耗时0.48s
                     *      限制PQS-200/s，500线程并发请求一次，耗时610.86s，平均阻塞耗时1.22s
                     *      限制PQS-200/s，1000线程并发请求一次，耗时2445.50s，平均阻塞耗时2.45s
                     *      限制PQS-200/s，2000线程并发请求一次，耗时9730.34s，平均阻塞耗时4.87s
                     *
                     * 2、无延迟下获取许可，成功返回true；否则false；
                     *      限制PQS-200/s，200线程并发请求一次，耗时0.029s，平均阻塞耗时0.145ms，命中4次；
                     *      限制PQS-200/s，500线程并发请求一次，耗时0.034s，平均阻塞耗时68ms，命中8次
                     *      限制PQS-200/s，1000线程并发请求一次，耗时0.045s，平均阻塞耗时45ms，命中9次
                     *      限制PQS-200/s，2000线程并发请求一次，耗时0.041s，平均阻塞耗时0.021ms，命中19次
                     *
                     * 3、指定Timeout/100ms下获取许可，成功返回true；否则false；
                     *      限制PQS-200/s，200线程并发请求一次，耗时1.37s，平均阻塞耗时0.0068s，命中24次；
                     *      限制PQS-200/s，500线程并发请求一次，耗时1.47s，平均阻塞耗时0.0029s，命中25次；
                     *      限制PQS-200/s，1000线程并发请求一次，耗时1.789s，平均阻塞耗时0.0018s，命中28次；
                     *      限制PQS-200/s，2000线程并发请求一次，耗时2.187s，平均阻塞耗时0.0011ms，命中32次；
                     *
                     * 4、指定Timeout/150ms下获取许可，成功返回true；否则false；
                     *      限制PQS-200/s，200线程并发请求一次，耗时2.7929s，平均阻塞耗时0.0139s，命中34次；
                     *      限制PQS-200/s，500线程并发请求一次，耗时3.2140s，平均阻塞耗时0.0064s，命中37次；
                     *      限制PQS-200/s，1000线程并发请求一次，耗时3.40s，平均阻塞耗时0.0034s，命中38次；
                     *      限制PQS-200/s，2000线程并发请求一次，耗时4.32s，平均阻塞耗时0.0022s，命中44次；
                     */


                    if (type == 1) {
                        double cost = rateLimiter.acquire();
                        costList.add(cost);
                        System.out.println(cost);
                    } else if (type == 2) {
                        Long start = System.currentTimeMillis();
                        boolean acqRet = rateLimiter.tryAcquire();
                        Long end = System.currentTimeMillis();
                        double cost = ((double)(end-start))/1000;
                        System.out.println(cost);

                        costList.add(cost);
                        if (acqRet) successNum.incrementAndGet();

                        if (allStart[0] == 0) {
                            allStart[0] = start.longValue();
                            allStop[0] = start.longValue();
                        } else {
                            if (allStart[0] > start.longValue()) {
                                allStart[0] = start.longValue();
                            }
                            if (allStop[0] < start.longValue()) {
                                allStop[0] = start.longValue();
                            }
                        }


                    } else if (type == 3) {
                        Long start = System.currentTimeMillis();
                        boolean acqRet = rateLimiter.tryAcquire(100, TimeUnit.MILLISECONDS);
                        Long end = System.currentTimeMillis();
                        double cost = ((double)(end-start))/1000;
                        System.out.println(cost);

                        costList.add(cost);
                        if (acqRet) successNum.incrementAndGet();
                    } else if (type == 4) {
                        Long start = System.currentTimeMillis();
                        boolean acqRet = rateLimiter.tryAcquire(150, TimeUnit.MILLISECONDS);
                        Long end = System.currentTimeMillis();
                        double cost = ((double)(end-start))/1000;
                        System.out.println(cost);

                        costList.add(cost);
                        if (acqRet) successNum.incrementAndGet();
                    }


                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);

        System.out.println("tryAcquire-Bwtween=" + (allStop[0] - allStart[0]));

        double costAll = 0;
        for (Double cost: costList) {
            costAll += cost;
        }
        System.out.println("costAll="+ costAll);
        System.out.println("cost each="+ costAll/runNum);
        System.out.println("successNum="+ successNum);

    }

}
