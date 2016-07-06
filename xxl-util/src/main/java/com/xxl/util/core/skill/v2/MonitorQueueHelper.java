package com.xxl.util.core.skill.v2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 阻塞队列-LinkedBlockingQueue实现方式
 * @author xuxueli 2015-9-1 18:05:56
 */
public class MonitorQueueHelper {
	private static Logger logger = LoggerFactory.getLogger(MonitorThreadHelper.class);
	
	public static MonitorQueueHelper helper = new MonitorQueueHelper();
	private ExecutorService executor = Executors.newCachedThreadPool();
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>(0xfff8);
	
	public MonitorQueueHelper(){
		for (int i = 0; i < 5; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						String originData = queue.poll();
						logger.info("Queue:"+originData);
						if (originData == null) {
							try {
								TimeUnit.SECONDS.sleep(5);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
		}
	}
	
	public static void pushData(final String originData){
		boolean status = helper.queue.offer(originData);
		logger.info("push data:{}, status:{}", originData, status);
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 100000; i++) {
			pushData("data" + i);
		}
	}
	
}
