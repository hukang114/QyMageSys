package com.qymage.sys.ui.entity;

/**
 * Created by admin on 2019/8/24.
 * <p>
 * 日报，周报 月报
 */

public class JournalEntity {

    public String headimg;
    public String username;
    public String datetime;
    public String today_plan;
    public String today_content;
    public String tomorrow_plan;

    public JournalEntity(String headimg, String username, String datetime, String today_plan, String today_content, String tomorrow_plan) {
        this.headimg = headimg;
        this.username = username;
        this.datetime = datetime;
        this.today_plan = today_plan;
        this.today_content = today_content;
        this.tomorrow_plan = tomorrow_plan;
    }


}
