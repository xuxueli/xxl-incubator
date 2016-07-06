package com.xxl.util.core.skill.v2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 阻塞队列-ThreadPoolExecutor实现方式
 * @author xuxueli 2015-9-1 16:57:16
 */
public class MonitorThreadHelper {
	private static Logger logger = LoggerFactory
			.getLogger(MonitorThreadHelper.class);

	private static MonitorThreadHelper helper = new MonitorThreadHelper();
	private ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 200L,
			TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(3000), 
			new ThreadPoolExecutor.CallerRunsPolicy());

	public static void pushData(final String originData) {
		helper.executor.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("Thread:" + originData);
			}
		});
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100000; i++) {
			pushData("data" + i);
		}
	}

}
