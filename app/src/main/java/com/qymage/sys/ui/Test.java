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
     * data : {"id":"1175637489378725890","userId":null,"userName":null,"dayWork":null,"wordPlay":"测试-02","jobWord":null,"nextWork":null,"auditor":null,"copier":null,"logList":[{"contractNo":"z测试-合同编号","contractName":"z测试-合同名称","projectNo":"z测试-项目编号","projectName":"z测试-项目名称","manHours":"22.0","workContent":null,"subMoney":"333","subMoneyList":[{"amount":"333","type":"1","detailed":"z测试-报销明细","photo":null}]}],"processInstanceId":"290001","createDate":null,"actStatus":null,"activityVo":[{"id":"1175637494827126785","processInstanceId":"290001","processDefId":"log_approve_process:3:287508","assignee":"1099100700000000039","name":"开始","status":null,"comment":null,"actType":"startEvent","actId":"290003","businessKey":"1175637489378725890","createdDate":1569128796000,"updatedDate":1569128796000,"userId":"1099100700000000039","userName":"张富荣"},{"id":null,"processInstanceId":"290001","processDefId":"log_approve_process:3:287508","assignee":"1099100700000000031","name":"多人审批","status":null,"comment":null,"actType":"userTask","actId":"290008","businessKey":null,"createdDate":null,"updatedDate":null,"userId":"1099100700000000031","userName":"吴贵钰"},{"id":null,"processInstanceId":"290001","processDefId":"log_approve_process:3:287508","assignee":"1099100700000000036","name":"多人审批","status":null,"comment":null,"actType":"userTask","actId":"290020","businessKey":null,"createdDate":null,"updatedDate":null,"userId":"1099100700000000036","userName":"孔俊智"}],"sendList":[{"userName":"孔俊智","userId":"1099100700000000036"}],"msgId":null,"read":null,"canCancelTask":"1"}
     */

    public boolean success;
    public int code;
    public String message;
    public DataBean data;

    public static class DataBean {
        /**
         * id : 1175637489378725890
         * userId : null
         * userName : null
         * dayWork : null
         * wordPlay : 测试-02
         * jobWord : null
         * nextWork : null
         * auditor : null
         * copier : null
         * logList : [{"contractNo":"z测试-合同编号","contractName":"z测试-合同名称","projectNo":"z测试-项目编号","projectName":"z测试-项目名称","manHours":"22.0","workContent":null,"subMoney":"333","subMoneyList":[{"amount":"333","type":"1","detailed":"z测试-报销明细","photo":null}]}]
         * processInstanceId : 290001
         * createDate : null
         * actStatus : null
         * activityVo : [{"id":"1175637494827126785","processInstanceId":"290001","processDefId":"log_approve_process:3:287508","assignee":"1099100700000000039","name":"开始","status":null,"comment":null,"actType":"startEvent","actId":"290003","businessKey":"1175637489378725890","createdDate":1569128796000,"updatedDate":1569128796000,"userId":"1099100700000000039","userName":"张富荣"},{"id":null,"processInstanceId":"290001","processDefId":"log_approve_process:3:287508","assignee":"1099100700000000031","name":"多人审批","status":null,"comment":null,"actType":"userTask","actId":"290008","businessKey":null,"createdDate":null,"updatedDate":null,"userId":"1099100700000000031","userName":"吴贵钰"},{"id":null,"processInstanceId":"290001","processDefId":"log_approve_process:3:287508","assignee":"1099100700000000036","name":"多人审批","status":null,"comment":null,"actType":"userTask","actId":"290020","businessKey":null,"createdDate":null,"updatedDate":null,"userId":"1099100700000000036","userName":"孔俊智"}]
         * sendList : [{"userName":"孔俊智","userId":"1099100700000000036"}]
         * msgId : null
         * read : null
         * canCancelTask : 1
         */

        public String id;
        public Object userId;
        public Object userName;
        public Object dayWork;
        public String wordPlay;
        public Object jobWord;
        public Object nextWork;
        public Object auditor;
        public Object copier;
        public String processInstanceId;
        public Object createDate;
        public Object actStatus;
        public Object msgId;
        public Object read;
        public String canCancelTask;
        public List<LogListBean> logList;
        public List<ActivityVoBean> activityVo;
        public List<SendListBean> sendList;

        public static class LogListBean {
            /**
             * contractNo : z测试-合同编号
             * contractName : z测试-合同名称
             * projectNo : z测试-项目编号
             * projectName : z测试-项目名称
             * manHours : 22.0
             * workContent : null
             * subMoney : 333
             * subMoneyList : [{"amount":"333","type":"1","detailed":"z测试-报销明细","photo":null}]
             */

            public String contractNo;
            public String contractName;
            public String projectNo;
            public String projectName;
            public String manHours;
            public Object workContent;
            public String subMoney;
            public List<SubMoneyListBean> subMoneyList;

            public static class SubMoneyListBean {
                /**
                 * amount : 333
                 * type : 1
                 * detailed : z测试-报销明细
                 * photo : null
                 */

                public String amount;
                public String type;
                public String detailed;
                public String photo;
            }
        }

        public static class ActivityVoBean {
            /**
             * id : 1175637494827126785
             * processInstanceId : 290001
             * processDefId : log_approve_process:3:287508
             * assignee : 1099100700000000039
             * name : 开始
             * status : null
             * comment : null
             * actType : startEvent
             * actId : 290003
             * businessKey : 1175637489378725890
             * createdDate : 1569128796000
             * updatedDate : 1569128796000
             * userId : 1099100700000000039
             * userName : 张富荣
             */

            public String id;
            public String processInstanceId;
            public String processDefId;
            public String assignee;
            public String name;
            public Object status;
            public Object comment;
            public String actType;
            public String actId;
            public String businessKey;
            public long createdDate;
            public long updatedDate;
            public String userId;
            public String userName;
        }

        public static class SendListBean {
            /**
             * userName : 孔俊智
             * userId : 1099100700000000036
             */

            public String userName;
            public String userId;
        }
    }
}
