package com.xxl.search.example.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuxueli on 16/7/30.
 */

@Controller
public class Client1Controller {
    private static Logger logger = LogManager.getLogger(Client1Controller.class.getName());

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



}
