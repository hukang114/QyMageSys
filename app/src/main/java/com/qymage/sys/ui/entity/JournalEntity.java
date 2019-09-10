package com.qymage.sys.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2019/8/24.
 * <p>
 * 日报，周报 月报
 */

public class JournalEntity implements Serializable {

    /**
     * id : 1099300500000000028
     * dayWork : 明日工作计划
     * wordPlay : null
     * jobWord : 本周工作计划
     * nextWork : 下周工作计划
     * auditor : null
     * copier : null
     * logList : null
     * processInstId : 125041
     * createDate : 2019-09-09
     * actStatus : null
     * activityVo : null
     * sendList : null
     */
    public String id;
    public String dayWork;
    public String wordPlay;
    public String jobWord;
    public String nextWork;
    public String processInstId;
    public String createDate;
    public String actStatus;
    public String headimg;
    public String userName;
    //周报--------------------------
    public String date;
    public String lastWeek;
    public String weekDay;
    public String nextWeek;


}
