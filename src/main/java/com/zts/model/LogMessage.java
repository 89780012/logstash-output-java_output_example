package com.zts.model;

import java.io.Serializable;

public class LogMessage {

    private String token; // 用户标识
    private String type; //类型: td md strategy 三种
    private String strategy_name;//若类型为strategy，则需要知道策略名称
    private String create_time; //创建时间
    private String state;      //状态
    private String content;    //内容
    private String logpath;    //日志路径

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStrategy_name() {
        return strategy_name;
    }

    public void setStrategy_name(String strategy_name) {
        this.strategy_name = strategy_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLogpath() {
        return logpath;
    }

    public void setLogpath(String logpath) {
        this.logpath = logpath;
    }
}
