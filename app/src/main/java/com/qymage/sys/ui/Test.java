package com.qymage.sys.ui;

import java.util.List;

/**
 * Created by admin on 2019/8/23.
 */

public class Test {


    /**
     * success : true
     * code : 200
     * message : 请求成功
     * data : [{"id":"1099300500000000028","dayWork":"明日工作计划","wordPlay":null,"jobWord":"本周工作计划","nextWork":"下周工作计划","auditor":null,"copier":null,"logList":null,"processInstId":"125041","createDate":"2019-09-09","actStatus":null,"activityVo":null,"sendList":null},{"id":"1099300500000000027","dayWork":null,"wordPlay":null,"jobWord":"本周工作计划","nextWork":"下周工作计划","auditor":null,"copier":null,"logList":null,"processInstId":"125021","createDate":"2019-09-09","actStatus":null,"activityVo":null,"sendList":null}]
     */

    public boolean success;
    public int code;
    public String message;
    public List<DataBean> data;

    public static class DataBean {
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
        public Object wordPlay;
        public String jobWord;
        public String nextWork;
        public Object auditor;
        public Object copier;
        public Object logList;
        public String processInstId;
        public String createDate;
        public Object actStatus;
        public Object activityVo;
        public Object sendList;
    }
}
