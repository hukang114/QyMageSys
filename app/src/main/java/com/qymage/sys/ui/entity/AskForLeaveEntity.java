package com.qymage.sys.ui.entity;

/**
 * Created by admin on 2019/8/24.
 * <p>
 * 请假记录
 */

public class AskForLeaveEntity {

    public String processInstanceId;
    public String actStatus;
    public ProcessListBean processList;
    public String endDate;
    public String photo;
    public String cause;
    public double ofTime;
    public String userCode;
    public SendListBean sendList;
    public String leaveType;
    public String leaveName;
    public String name;
    public String id;
    public String startDate;
    public String createDate;
    public String msgId;
    public String read;//消息状态  0未读  1已读

    public static class ProcessListBean {
        /**
         * date : null
         * node : null
         * processInstanceId : 197527
         * createdDate : null
         * comment  : null
         * name : 部门经理
         * businessKey : null
         * userName : null
         * userId : null
         * status : null
         */

        public String date;
        public String node;
        public String processInstanceId;
        public String createdDate;
        public String comment;
        public String name;
        public String businessKey;
        public String userName;
        public String userId;
        public String status;
    }

    public static class SendListBean {
        /**
         * name : 孔俊智
         * userId : 1099100700000000036
         */

        public String name;
        public String userId;
    }
}
