package com.njnu.kai.plugin.practice;

public class HelloworldMsg {
    private String mTitle;
    private String mMsg;

    public HelloworldMsg() {
        mTitle = "掌柜默认Title";
        mMsg = "掌柜默认详情数据, 好长啊哈哈, 果真是挺长的, 无线开发工程师 UI设计师 码农";
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }
}