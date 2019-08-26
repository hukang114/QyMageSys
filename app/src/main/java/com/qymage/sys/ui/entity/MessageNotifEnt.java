package com.qymage.sys.ui.entity;

/**
 * Created by admin on 2019/8/25.
 * 消息通知类型的数据模型
 */

public class MessageNotifEnt {


    public String msg_pic;
    public String msg_title;
    public String date_time;

    public MessageNotifEnt(String msg_pic, String msg_title, String date_time) {
        this.msg_pic = msg_pic;
        this.msg_title = msg_title;
        this.date_time = date_time;
    }
}
