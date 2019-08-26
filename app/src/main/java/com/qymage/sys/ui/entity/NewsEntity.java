package com.qymage.sys.ui.entity;

/**
 * Created by admin on 2019/8/24.
 * 通知类型列表
 */

public class NewsEntity {


    public int ioc;
    public String title;
    public String date;
    public String content;
    public int num;

    public NewsEntity(int ioc, String title, String date, String content, int num) {
        this.ioc = ioc;
        this.title = title;
        this.date = date;
        this.content = content;
        this.num = num;
    }
}
