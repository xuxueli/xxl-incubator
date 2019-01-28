package com.xuxueli.demo.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPoolTest {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkjoinPool = new ForkJoinPool();

        // prepare task
        CountTask task = new CountTask(1, 100);

        // submit task
        ForkJoinTask<Integer> forkJoinTask = forkjoinPool.submit(task);
        forkjoinPool.shutdown();

        // get task
        System.out.println(forkJoinTask.get());

    }


    public static class CountTask extends RecursiveTask<Integer> {

        public static final int threshold = 10;
        private int start;
        private int end;

        public CountTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int sum = 0;

            if ((end - start) <= threshold) {
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
                System.out.println("count item, "+start+"-"+end+", result = " + sum);
            } else {
                // 任务超过阈值，分裂子任务；
                int middle = (start + end) / 2;
                CountTask leftTask = new CountTask(start, middle);
                CountTask rightTask = new CountTask(middle + 1, end);

                // 执行子任务
                leftTask.fork();
                rightTask.fork();

                // 等待子任务结束
                int leftResult = leftTask.join();
                int rightResult = rightTask.join();

                // 合并子任务结束
                sum = leftResult + rightResult;
            }

            return sum;
        }


    }
}
