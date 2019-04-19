package com.xuxueli.demo.quartz;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {
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

                    // 任务排序，逐个等待执行
                    Collections.sort(waitJob, new Comparator<JobInfo>() {
                        @Override
                        public int compare(JobInfo o1, JobInfo o2) {
                            return (int)(o1.getNextTriggerTime() - o2.getNextTriggerTime());
                        }
                    });

                    for (JobInfo jobItem: waitJob) {
                        long waiTime = jobItem.getNextTriggerTime() - System.currentTimeMillis();
                        if (waiTime > 0) {
                            TimeUnit.MILLISECONDS.sleep(waiTime);
                        }
                        jobItem.setLastTriggerTime(System.currentTimeMillis());
                        jobItem.setNextTriggerTime(jobItem.generateNextTriggerTime().getTime());
                        jobItem.setStatus(0);   // 触发 + 释放

                        System.out.println("job[" + jobItem.getCron() + "] run as " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
