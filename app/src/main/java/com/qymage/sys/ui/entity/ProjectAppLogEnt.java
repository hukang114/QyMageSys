package com.qymage.sys.ui.entity;

/**
 * Created by admin on 2019/8/29.
 * 立项记录
 */

public class ProjectAppLogEnt {


    public String imgpath;
    public String username;
    public String date_time;
    public String shenqing_ly;
    public String shenqin_money;
    public String shiyong_date;
    public int status;


    public ProjectAppLogEnt(String imgpath, String username, String date_time, String shenqing_ly, String shenqin_money, String shiyong_date, int status) {
        this.imgpath = imgpath;
        this.username = username;
        this.date_time = date_time;
        this.shenqing_ly = shenqing_ly;
        this.shenqin_money = shenqin_money;
        this.shiyong_date = shiyong_date;
        this.status = status;
    }
}
