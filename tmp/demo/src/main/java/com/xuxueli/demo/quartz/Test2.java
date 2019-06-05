package com.xuxueli.demo.quartz;

import java.util.concurrent.*;

public class Test2 {

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());


        final DelayQueue<Message> queue = new DelayQueue<Message>();
        for (int i = 0; i < 10; i++) {
            queue.offer(new Message(i, System.nanoTime() + TimeUnit.NANOSECONDS.convert(1000 * i, TimeUnit.MILLISECONDS)));
            if (i%3 == 0) {
                queue.offer(new Message(i, System.nanoTime() + TimeUnit.NANOSECONDS.convert(1000 * i, TimeUnit.MILLISECONDS)));
            }
        }


        ExecutorService exec = Executors.newFixedThreadPool(1);
        exec.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Message take = queue.take();
                        System.out.println(System.currentTimeMillis() + "消费消息id：" + take.getId() );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        exec.shutdown();

    }

    public static class Message implements Delayed {

        private int id;
        private long excuteTime;

        public int getId() {
            return id;
        }

        public long getExcuteTime() {
            return excuteTime;
        }

        public Message(int id, long delayTime) {
            this.id = id;
            this.excuteTime = delayTime;
        }

        @Override
        public int compareTo(Delayed delayed) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) -delayed.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.excuteTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

    }


}
