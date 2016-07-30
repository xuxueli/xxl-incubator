package com.xxl.search.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuxueli on 16/7/30.
 */

@Controller
public class IndexController {
    private static Logger logger = LogManager.getLogger(IndexController.class.getName());

    @RequestMapping("/")
    @ResponseBody
    public Map<String, Object> demo(){
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("code", 200);
        temp.put("msg", "终于成功了");

        try {
            int asd = 9/0;
        }catch (Exception e) {
            logger.error("", e);
            temp.put("error", e.getMessage());
        }

        return temp;
    }

    /**
     * JSONP (SpringMVC方式)
     * @param callback
     * @return
     */
    @RequestMapping("/jsonp")
    @ResponseBody
    public MappingJacksonValue jsonp(String callback) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("code", 200);
        temp.put("msg", "终于成功了");

        // 封装JSONP
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(temp);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }

    /**
     * JSONP (Jackson之JSONPObject方式)
     * @param callback
     * @return
     */
    @RequestMapping(value = "/jsonp2" )
    @ResponseBody
    public JSONPObject jsonp2(String callback) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aaa", "I'm Dreamlu！");
        return new JSONPObject(callback, map);
    }


}
