package com.xxl.util.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.util.core.skill.threadpool.ThreadPoolLinkedHelper;
import com.xxl.util.core.skill.threadpool.ThreadPoolQueueHelper;

/**
 * skill demo
 * @author xuxueli 2016-7-6 16:27:44
 */
@Controller
@RequestMapping("/skill")
public class SkillDemoController {

	/**
	 * <pre>
	1、ThreadPoolQueueHelper.java/ThreadPoolLinkedHelper.java
		功能简介：
			生产消费者模型，FIFO队列，线程池，异步
		方式A：“Executors.newCachedThreadPool() + LinkedBlockingQueue”方式实现
			特点：启动时初始化指定数量Producer、Consumer线程；Producer负责push数据到queue中，Consumer负责从queue中pull数据并处理；
		方式B：“ThreadPoolExecutor”方式实现
			特点：启动时初始化ThreadPool(内部线程使用LinkedBlockingQueue维护)，以及Producer线程；Producer负责为每条数据在ThreadPool中创建新的线程，每个线程run方法即消费者逻辑方法；
	   </pre>
	 * @return
	 */
	@RequestMapping("/producerConsumer")
	@ResponseBody
	public String producerConsumer(){
		/*
		ThreadPoolQueueHelper.pushData("producerConsumer-" + System.currentTimeMillis());	
		ThreadPoolLinkedHelper.pushData("producerConsumer-" + System.currentTimeMillis());
		*/
		ThreadPoolQueueHelper.getInstance();	// 使用时，线程池需要主动触发；如实例化bean方式，或者listener方式；
		ThreadPoolLinkedHelper.getInstance();
		
		return "200";
	} 
	
}
