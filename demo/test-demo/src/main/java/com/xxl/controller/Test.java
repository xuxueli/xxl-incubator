package com.xxl.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuxueli on 16/10/31.
 */
public class Test {


    public static void main(String[] args) {

        final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();

        Executor executor = Executors.newCachedThreadPool();
        executor.execute(new Runnable() {
            public void run() {
                int i = 0;
                while (true) {
                    queue.add(i++);
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        executor.execute(new Runnable() {
            public void run() {
                while (true) {
                    List<Integer> descList = new ArrayList<Integer>();
                    for (int i = 0; i < 10; i++) {
                        Integer integer = queue.poll();
                        if (integer == null) {
                            break;
                        }
                        descList.add(integer);
                    }
                    System.out.println(descList);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

}
