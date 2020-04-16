package com.xxl.push.broker.controller;

import com.xxl.push.broker.controller.annotation.PermessionLimit;
import com.xxl.push.broker.core.constant.XxlPushBiz;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xuxueli 2016-10-09 19:36:29
 */
@Controller
@RequestMapping("/chat")
public class ChatController {


	@RequestMapping("")
	@PermessionLimit
	public String index(Model model){
		model.addAttribute("XxlPushBiz", XxlPushBiz.values());
		return "chat/chat.index";
	}


	@RequestMapping("/new")
	@PermessionLimit
	public String newchat(Model model){
		model.addAttribute("XxlPushBiz", XxlPushBiz.values());
		return "chat/chat.new";
	}
	
}
