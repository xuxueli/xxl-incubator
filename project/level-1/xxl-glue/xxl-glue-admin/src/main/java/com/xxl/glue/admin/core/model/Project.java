package com.xxl.glue.admin.core.model;

/**
 * Created by xuxueli on 17/5/28.
 */
public class Project {

    private int id;
    private String appname;     // 项目AppName
    private String name;        // 项目名称

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
