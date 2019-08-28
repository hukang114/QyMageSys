package com.qymage.sys.ui.entity;

/**
 * Created by admin on 2019/8/28.
 * 我的借款列表
 */

public class MyLoanEnt {

    public String imgpath;
    public String username;
    public String date_time;
    public String shenqing_ly;
    public String shenqin_money;
    public String shiyong_date;
    public String remake_text;
    public int status;


    public MyLoanEnt(String imgpath, String username, String date_time, String shenqing_ly, String shenqin_money, String shiyong_date, String remake_text, int status) {
        this.imgpath = imgpath;
        this.username = username;
        this.date_time = date_time;
        this.shenqing_ly = shenqing_ly;
        this.shenqin_money = shenqin_money;
        this.shiyong_date = shiyong_date;
        this.remake_text = remake_text;
        this.status = status;
    }
}
