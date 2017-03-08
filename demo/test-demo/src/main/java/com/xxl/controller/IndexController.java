package com.xxl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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

	// ----------- redirect 方式（URL）
	@RequestMapping("/redirect")
	private String redirect(String word, RedirectAttributes redirectAttributes) {
		// URL拼参数（动态传参）
		redirectAttributes.addAttribute("word", "redirect:" + word);
		// URL不品参数（结合@ModelAttribute）
		//redirectAttributes.addFlashAttribute("word2", "redirect:" + word);
		return "redirect:/newUrl";
	}

	@RequestMapping(value = "/newUrl")
	@ResponseBody
	private String newUrl(String word) {
		return word;
	}

	// ----------- redirect 方式（addFlashAttribute / Session）(POST-Redirect-GET)
	@RequestMapping(value = "/redirect1", method = RequestMethod.POST)
	private String redirect1(String word, RedirectAttributes redirectAttributes) {
		// URL不品参数（结合@ModelAttribute）
		redirectAttributes.addFlashAttribute("word", "redirect1:" + word);
		return "redirect:/newUrl1";
	}

	@RequestMapping(value = "/newUrl1", method = RequestMethod.GET)
	@ResponseBody
	private String newUrl1(String word) {
		return word;
	}

	// ----------- forward 方式
	@RequestMapping("/forward")
	private String forward(String word, Model model) {
		model.addAttribute("tim", new Date());
		return "forward:/forwardUrl";
	}

	@RequestMapping("/forwardUrl")
	@ResponseBody
	private String forwardUrl(String word, HttpServletRequest request, Date tim) {
		Date tim2 = (Date) request.getAttribute("tim");
		return ">>>" + word + ":" + tim2.getTime();
	}

}
