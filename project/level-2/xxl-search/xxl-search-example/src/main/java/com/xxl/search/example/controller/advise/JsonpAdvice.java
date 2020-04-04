package com.xxl.search.example.controller.advise;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * 支持JSONP接口调用（ResponseBody启动后，当存在callback参数时，返回JSONP格式结果；否则返回JSON结果；）
 * @author xuxueli 2017-04-06 15:07:27
 */
@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {

    public JsonpAdvice() {
        super("callback");
    }

}