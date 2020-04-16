package com.xxl.push.broker.controller;

import com.xxl.push.broker.controller.annotation.PermessionLimit;
import com.xxl.push.broker.core.constant.XxlPushBiz;
import com.xxl.push.broker.core.util.ReturnT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 连接管理
 * @author xuxueli 2016-10-09 19:36:29
 */
@Controller
@RequestMapping("/channel")
public class ChannelController {


	@RequestMapping("")
	@PermessionLimit
	public String index(Model model){
		model.addAttribute("XxlPushBiz", XxlPushBiz.values());
		return "channel/channel.index";
	}


	/**
	 * create/update
	 * @return
	 */
	@RequestMapping("/test")
	@ResponseBody
	@PermessionLimit
	public ReturnT<String> test(){
		return ReturnT.SUCCESS;
	}
	
}
