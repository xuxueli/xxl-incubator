package com.xuxueli.demo.quartz;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {
        new Thread(new QuartzAdminRing()).start();
        for (int i = 0; i < 1; i++) {
            QuartzAdminNode adminNode = new QuartzAdminNode();
            new Thread(adminNode).start();
        }
    }

    public volatile static List<JobInfo> jobList = Collections.synchronizedList(Arrays.asList(
            new JobInfo("0/5 * * * * ? *"),
            new JobInfo("0/3 * * * * ? *")));

    public static class JobInfo{
        private String cron;
        private long lastTriggerTime;
        private long nextTriggerTime;
        private int status;     // 0-free, 1-lock

        public JobInfo(String cron) {
            this.cron = cron;
            this.lastTriggerTime = 0L;
            this.nextTriggerTime = generateNextTriggerTime().getTime();
            this.status = 0;
        }

        public Date generateNextTriggerTime(){
            try {
                return new CronExpression(cron).getNextValidTimeAfter(new Date());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        public String getCron() {
            return cron;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }

        public long getLastTriggerTime() {
            return lastTriggerTime;
        }

        public void setLastTriggerTime(long lastTriggerTime) {
            this.lastTriggerTime = lastTriggerTime;
        }

        public long getNextTriggerTime() {
            return nextTriggerTime;
        }

        public void setNextTriggerTime(long nextTriggerTime) {
            this.nextTriggerTime = nextTriggerTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public volatile static Object lock = new Object();

    public static class QuartzAdminNode implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    // 加锁，获取待运行JOB，更新下次触发时间
                    List<JobInfo> waitJob = new ArrayList<>();
                    synchronized (lock) {
                        long nowTime = System.currentTimeMillis();

                        // 30s 内job
                        for (JobInfo jobItem:jobList) {
                            if ((jobItem.getStatus()==0 && nowTime + 30*1000 >= jobItem.getNextTriggerTime())
                                    /*|| (nowTime >= jobItem.getNextTriggerTime())*/) {    // 空闲&下次调度在30s内 || 超过下次调度时间

                                jobItem.setStatus(1);   // 锁定
                                waitJob.add(jobItem);
                            }
                        }
                    }
                    if (waitJob.size() == 0) {
                        continue;
                    }

                    // 推送时间轮，异步触发
                    for (JobInfo jobInfo: waitJob) {
                        int second = (int)((jobInfo.nextTriggerTime/1000)%60);
                        List<JobInfo> ringItemData = ringData.get(second);
                        if (ringItemData == null) {
                            ringItemData = new ArrayList<>();
                            ringData.put(second, ringItemData);
                        }
                        ringItemData.add(jobInfo);
                    }

                    // 随机休眠1s内
                    TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private volatile static Map<Integer, List<JobInfo>> ringData = new ConcurrentHashMap<>();
    public static class QuartzAdminRing implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);

                    int second = (int)((System.currentTimeMillis()/1000)%60);   // 记录 pre_second，避免处理耗时太长，跨过刻度；
                    List<JobInfo> ringItemData = ringData.get(second);

                    if (ringItemData==null || ringItemData.size() ==0) {
                        continue;
                    }

                    for (JobInfo jobItem: ringItemData) {
                        jobItem.setLastTriggerTime(System.currentTimeMillis());
                        jobItem.setNextTriggerTime(jobItem.generateNextTriggerTime().getTime());
                        jobItem.setStatus(0);   // 触发 + 释放
                        System.out.println("[" + jobItem.getCron() + "]:" + DateFormatUtils.format(new Date(), "HH:mm:ss"));
                    }
                    ringItemData.clear();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
