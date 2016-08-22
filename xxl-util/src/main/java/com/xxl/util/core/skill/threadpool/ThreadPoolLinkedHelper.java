package com.xxl.util.core.skill.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 生产消费者模型，“FIFO队列，线程池，异步”，“ThreadPoolExecutor”方式实现
 * 特点：启动时初始化ThreadPool(内部线程使用LinkedBlockingQueue维护)，以及Producer线程；Producer负责为每条数据在ThreadPool中创建新的线程，每个线程run方法即消费者逻辑方法；
 * @author xuxueli 2015-9-1 16:57:16
 */
public class ThreadPoolLinkedHelper {
	private static Logger logger = LoggerFactory.getLogger(ThreadPoolLinkedHelper.class);

	private ThreadPoolExecutor executor = new ThreadPoolExecutor(3, Integer.MAX_VALUE, 60L,
			TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE),
			new ThreadPoolExecutor.CallerRunsPolicy());

	public ThreadPoolLinkedHelper(){
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
		// consumer thread, is replaced by each thread's run method
	}
	
	private static ThreadPoolLinkedHelper helper = new ThreadPoolLinkedHelper();
	public static ThreadPoolLinkedHelper getInstance(){
		return helper;
	}

	public static void pushData(final String originData) {
		logger.info("producer data:" + originData);
		getInstance().executor.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("consumer data:" + originData);
			}
		});
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			pushData("data" + i);
		}
	}
	
}
