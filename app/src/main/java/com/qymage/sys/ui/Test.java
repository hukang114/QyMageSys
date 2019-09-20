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
     * data : {"processInstanceId":"240085","actStatus":"审批中","weekPalyList":{},"submonthWork":"高灿","user0code":"1099100700000000036","userName":"孔俊智","leadGrade":"","sendList":[{"userName":"孔俊智","userId":"1099100700000000036"}],"activityVo":[{"processInstanceId":"240085","createdDate":1568863659000,"businessKey":"1099301000000000016","name":"月报审批流程","comment":null,"userName":null,"userId":"240087","status":null},{"processInstanceId":"240085","createdDate":1568864349000,"businessKey":"1099301000000000016","name":"经理审批","comment":"","userName":null,"userId":"240091","status":"1"},{"processInstanceId":"240085","createdDate":1568863964000,"businessKey":"1099301000000000016","name":"经理审批","comment":"","userName":null,"userId":"240103","status":"1"},{"processInstanceId":"240085","createdDate":null,"businessKey":null,"name":"管理层审批","comment":null,"userName":null,"userId":"240106","status":null}],"leadComment":"","createTime":"2019-09-19 11:27:33.0","selg":"好","monthWork":"","nextMonthWeek":"","Id":"1099301000000000016","weekTatalkList":{}}
     */

    public boolean success;
    public int code;
    public String message;
    public DataBean data;

    public static class DataBean {
        /**
         * processInstanceId : 240085
         * actStatus : 审批中
         * weekPalyList : {}
         * submonthWork : 高灿
         * user0code : 1099100700000000036
         * userName : 孔俊智
         * leadGrade :
         * sendList : [{"userName":"孔俊智","userId":"1099100700000000036"}]
         * activityVo : [{"processInstanceId":"240085","createdDate":1568863659000,"businessKey":"1099301000000000016","name":"月报审批流程","comment":null,"userName":null,"userId":"240087","status":null},{"processInstanceId":"240085","createdDate":1568864349000,"businessKey":"1099301000000000016","name":"经理审批","comment":"","userName":null,"userId":"240091","status":"1"},{"processInstanceId":"240085","createdDate":1568863964000,"businessKey":"1099301000000000016","name":"经理审批","comment":"","userName":null,"userId":"240103","status":"1"},{"processInstanceId":"240085","createdDate":null,"businessKey":null,"name":"管理层审批","comment":null,"userName":null,"userId":"240106","status":null}]
         * leadComment :
         * createTime : 2019-09-19 11:27:33.0
         * selg : 好
         * monthWork :
         * nextMonthWeek :
         * Id : 1099301000000000016
         * weekTatalkList : {}
         */

        public String processInstanceId;
        public String actStatus;
        public WeekPalyListBean weekPalyList;
        public String submonthWork;
        public String user0code;
        public String userName;
        public String leadGrade;
        public String leadComment;
        public String createTime;
        public String selg;
        public String monthWork;
        public String nextMonthWeek;
        public String Id;
        public WeekTatalkListBean weekTatalkList;
        public List<SendListBean> sendList;
        public List<ActivityVoBean> activityVo;

        public static class WeekPalyListBean {
        }

        public static class WeekTatalkListBean {
        }

        public static class SendListBean {
            /**
             * userName : 孔俊智
             * userId : 1099100700000000036
             */

            public String userName;
            public String userId;
        }

        public static class ActivityVoBean {
            /**
             * processInstanceId : 240085
             * createdDate : 1568863659000
             * businessKey : 1099301000000000016
             * name : 月报审批流程
             * comment : null
             * userName : null
             * userId : 240087
             * status : null
             */

            public String processInstanceId;
            public long createdDate;
            public String businessKey;
            public String name;
            public Object comment;
            public Object userName;
            public String userId;
            public Object status;
        }
    }
}
