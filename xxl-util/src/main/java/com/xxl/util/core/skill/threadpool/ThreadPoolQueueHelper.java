package com.xxl.util.core.skill.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生产消费者模型，“FIFO队列，线程池，异步”，“Executors.newCachedThreadPool() + LinkedBlockingQueue”方式实现
 * 特点：启动时初始化指定数量Producer、Consumer线程；Producer负责push数据到queue中，Consumer负责从queue中pull数据并处理；
 * @author xuxueli 2015-9-1 18:05:56
 */
public class ThreadPoolQueueHelper {
	private static Logger logger = LoggerFactory.getLogger(ThreadPoolLinkedHelper.class);
	
	private ExecutorService executor = Executors.newCachedThreadPool();
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>(0xfff8);
	
	public ThreadPoolQueueHelper(){
		// producer thread, can be replaced by method "pushData()"
		executor.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					pushData("data-" + System.currentTimeMillis());
					try {
						TimeUnit.SECONDS.sleep(3L);
					} catch (InterruptedException e) {
						logger.info("ThreadPoolQueueHelper producer error：", e);
					}
				}
			}
		});
		// consumer thread
		for (int i = 0; i < 2; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						String originData;
						try {
							originData = queue.poll(5L, TimeUnit.SECONDS);
							logger.info("poll:{}", originData);
							if (originData == null) {
								TimeUnit.SECONDS.sleep(5L);
							}
						} catch (InterruptedException e) {
							logger.info("ThreadPoolQueueHelper consumer error：", e);
						}
					}
				}
			});
		}
	}
	
	private static ThreadPoolQueueHelper helper = new ThreadPoolQueueHelper();
	public static ThreadPoolQueueHelper getInstance(){
		return helper;
	}
	
	public static boolean pushData(String originData){
		boolean status = false;
		if (originData != null && originData.trim().length()>0) {
			status = getInstance().queue.offer(originData);
		}
		logger.info("offer data:{}, status:{}", originData, status);
		return status;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			pushData("data" + i);
		}
	}
	
}
