package com.qymage.sys.ui.entity;

import java.util.List;

/**
 * 类名：
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1216:35
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class BidPerFormDetEnt {


    public String createTime;
    public String createBy;
    public String updateTime;
    public String updateBy;
    public String id;
    public String projectId;
    public String projectNo;
    public String projectName;
    public String companyId;
    public String companyName;
    public String bidType;
    public String name;
    public String bank;
    public String account;
    public String amount;
    public String amountName;
    public String date;
    public String remark;
    public String endDate;
    public String personName;
    public String auditor;
    public Object copier;
    public Object keyword;
    public String stats;
    public String processInstId;
    public int actStatus;
    public List<ActivityVoListBean> activityVoList;
    public List<SendListBean> sendList;

    public List<FileListBean> fileList;

    public static class FileListBean {
        /**
         * fileName : 12
         * filePath : 12
         */

        public String fileName;
        public String filePath;
    }

    public static class ActivityVoListBean {
        /**
         * id : 1172064669692579842
         * processInstanceId : 177597
         * processDefId : performance_come_process:1:140032
         * assignee : 1099100700000000036
         * name : null
         * status :
         * comment :
         * actType : startEvent
         * actId : 177598
         * businessKey : 1172064665821237249
         * createdDate : 1568276968000
         * updatedDate : 1568276968000
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
        public String createdDate;
        public String updatedDate;
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
