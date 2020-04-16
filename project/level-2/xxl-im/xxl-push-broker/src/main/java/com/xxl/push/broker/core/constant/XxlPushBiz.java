package com.xxl.push.broker.core.constant;

/**
 * Created by xuxueli on 16/10/9.
 */
public enum XxlPushBiz {
    SYS_MSG_BIZ("系统消息业务线"),
    CHAT_BIZ("聊天业务线");
    public final String title;
    private XxlPushBiz(String title){
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
