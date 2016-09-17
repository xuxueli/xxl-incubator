package com.xxl.search.example.controller;

import com.xxl.search.example.core.enums.ShopEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xuxueli on 16/7/30.
 */

@Controller
public class ESController {
    private static Logger logger = LogManager.getLogger(ESController.class.getName());

    @RequestMapping()
    public String index (Model model){
        model.addAttribute("shoplist", ShopEnum.shoplist);
        return "index";
    }

    @RequestMapping("/search")
    @ResponseBody
    public String search (Model model){
        model.addAttribute("total", 0);
        return "index";
    }

}
