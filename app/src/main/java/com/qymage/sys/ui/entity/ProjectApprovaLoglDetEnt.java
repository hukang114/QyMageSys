package com.qymage.sys.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2019/9/8.
 * <p>
 * 立项记录详情
 */

public class ProjectApprovaLoglDetEnt implements Serializable {

    public long createTime;
    public String createBy;
    public String updateTime;
    public String updateBy;
    public String id;
    public String projectType;
    public String projectTypeName;
    public String projectNo;
    public String projectName;
    public String persion;
    public String amount;
    public String date;
    public String Stringroduction;
    public String processInstId;
    public String personName;
    public Object startDate;
    public Object endDate;
    public Object keyword;
    public Object stats;
    public Object auditor;
    public Object copier;
    public String regarding;
    public List<ActivityVoListBean> activityVoList;
    public List<SendListBean> sendList;
    public String actStatus;
    public String introduction;
    public int canCancelTask;

    public static class ActivityVoListBean implements Serializable {
        /**
         * id : 1171709251275427842
         * processInstanceId : 150077
         * processDefId : projcet_process:3:127512
         * assignee : 1099100700000000036
         * name : null
         * status :
         * comment :
         * actType : startEvent
         * actId : 150078
         * businessKey : 1171709245101412354
         * createdDate : 1568192229000
         * updatedDate : 1568192229000
         * userId : 1099100700000000036
         * userName : 孔俊智
         */

        public String id;
        public String processInstanceId;
        public String processDefId;
        public String assignee;
        public String name;
        public String status;
        public String comment;
        public String actType;
        public String actId;
        public String businessKey;
        public long createdDate;
        public long updatedDate;
        public String userId;
        public String userName;
    }

    public static class SendListBean implements Serializable {
        /**
         * userName : 孔俊智
         * userId : 1099100700000000036
         */

        public String userName;
        public String userId;
    }
}
