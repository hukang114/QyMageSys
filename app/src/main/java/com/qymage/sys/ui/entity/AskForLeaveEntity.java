package com.qymage.sys.ui.entity;

/**
 * Created by admin on 2019/8/24.
 * <p>
 * 请假记录
 */

public class AskForLeaveEntity {

    public String headimg;
    public String username;
    public String afl_type;
    public String start_time;
    public String end_time;
    public String result;
    public int result_type;

    public AskForLeaveEntity(String headimg, String username,String afl_type, String start_time, String end_time, String result, int result_type) {
        this.headimg = headimg;
        this.username = username;
        this.afl_type = afl_type;
        this.start_time = start_time;
        this.end_time = end_time;
        this.result = result;
        this.result_type = result_type;
    }


}
