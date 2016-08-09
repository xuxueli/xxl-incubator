package com.xxl.cache.controller;

import com.xxl.cache.controller.annotation.PermessionLimit;
import com.xxl.cache.core.model.MemcachedTemplate;
import com.xxl.cache.core.util.CacheKeyUtil;
import com.xxl.cache.core.util.ReturnT;
import com.xxl.cache.service.IMemcachedService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by xuxueli on 16/8/6.
 */
@Controller
@RequestMapping("/memcached")
public class MemcachedController {

    @Resource
    private IMemcachedService memcachedService;


    @RequestMapping("")
    @PermessionLimit
    public String toLogin(Model model, HttpServletRequest request) {
        return "memcached/memcached.index";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermessionLimit
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
        @RequestParam(required = false, defaultValue = "10") int length, String key) {
        return memcachedService.pageList(start, length, key);
    }

    @RequestMapping("/save")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> save(MemcachedTemplate memcachedTemplate) {
        return memcachedService.save(memcachedTemplate);
    }

    @RequestMapping("/update")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> update(MemcachedTemplate memcachedTemplate) {
        return memcachedService.update(memcachedTemplate);
    }

    @RequestMapping("/delete")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> delete(int id) {
        return memcachedService.delete(id);
    }


    @RequestMapping("/getCacheInfo")
    @ResponseBody
    @PermessionLimit
    public ReturnT<Map<String, Object>> getCacheInfo(String finalKey) {
        return memcachedService.getCacheInfo(finalKey);
    }

    @RequestMapping("/removeCache")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> removeCache(String finalKey) {
        return memcachedService.removeCache(finalKey);
    }

    @RequestMapping("/getFinalKey")
    @ResponseBody
    @PermessionLimit
    public ReturnT<String> getFinalKey(String key, String params) {
        String finalKey = CacheKeyUtil.getFinalKey(key, params);
        return new ReturnT<String>(finalKey);
    }

}
