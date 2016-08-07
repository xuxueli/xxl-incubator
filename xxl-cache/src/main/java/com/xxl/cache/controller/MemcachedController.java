package com.xxl.cache.controller;

import com.xxl.cache.controller.annotation.PermessionLimit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 16/8/6.
 */
@Controller
@RequestMapping("/memcached")
public class MemcachedController {


    @RequestMapping("")
    @PermessionLimit
    public String toLogin(Model model, HttpServletRequest request) {
        return "memcached/memcached.index";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
        @RequestParam(required = false, defaultValue = "10") int length,
        String jobGroup, String executorHandler, String filterTime) {

        int list_count = 0;
        List<String> data = null;

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", list_count);		// 总记录数
        maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
        maps.put("data", "");  					// 分页列表
        return maps;
    }

}
