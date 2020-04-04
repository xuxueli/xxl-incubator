package com.xxl.web.core.response.impl;


import com.xxl.web.core.response.XxlWebResponse;
import com.xxl.web.core.response.annotation.XxlWebContentType;

/**
 * Created by xuxueli on 17/5/25.
 */
public class HtmlResponse extends XxlWebResponse {

    private String content;
    public HtmlResponse(String content){
        this.content = content;
    }

    @Override
    public XxlWebContentType contentType() {
        return XxlWebContentType.HTML;
    }

    @Override
    public String content() {
        return content;
    }

}
