package com.qymage.sys.ui.entity;

/**
 * Created by admin on 2019/8/25.
 * 工作通知
 */

public class WorkNoticeEnt {


    public String user_pic;
    public String username;
    public String type_name;
    public String proj_name;
    public String app_money;
    public String date_time;

    public WorkNoticeEnt(String user_pic, String username, String type_name, String proj_name, String app_money, String date_time) {
        this.user_pic = user_pic;
        this.username = username;
        this.type_name = type_name;
        this.proj_name = proj_name;
        this.app_money = app_money;
        this.date_time = date_time;
    }
}
