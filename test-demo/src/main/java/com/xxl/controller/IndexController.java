package com.xxl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ehcache 进程缓存, 虽然目前ehcache提供集群方案，但是分布式缓存还是使用memcached/redis比较好;
 * memcached 分布式缓存, 分布是通常通过分片方式实现, 多核, 数据结构单一, key250k和value1M容量首限;
 * redis 分布式缓存, 单核, 支持数据结构丰富, key和value512M容量巨大;
 * mongodb Nosql, 非关系型数据库(平级于mysql)，存储海量数据;
 * 
 * ehcache本地对象缓存：EhcacheUtil (ehcacheObj)
 * ehcache页面缓存：SimplePageCachingFilter (ehcacheUrl)
 * 
 * @author xuxueli 2015-12-5 20:56:04
 */
@Controller
public class IndexController {
	
	@RequestMapping("/asd")
	@ResponseBody
	private String index() {
		return "300";
	}
	
}
