package com.qymage.sys.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：
 * 类描述：请假详情
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1623:43
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ASkForDetEnt {

    public String processInstanceId;
    public String actStatus;
    public String endDate;
    public String photo;
    public String cause;
    public double ofTime;
    public String userCode;
    public String leaveType;
    public String leaveName;
    public String name;
    public String Id;
    public String startDate;
    public String createDate;
    public String personName;
    public String createTime;
    public List<ActivityVoBean> activityVo;
    public String processInstId;
    public int canCancelTask;
    public String id;


    public static class ActivityVoBean implements Serializable {
        /**
         * id : 1173134876833968129
         * processInstanceId : 197505
         * processDefId : loan_process:1:180004
         * assignee : 1099100700000000036
         * name : null
         * status :
         * comment :
         * actType : startEvent
         * actId : 197507
         * businessKey : 1099302100000000016
         * createdDate : 1568532125000
         * updatedDate : 1568532125000
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
