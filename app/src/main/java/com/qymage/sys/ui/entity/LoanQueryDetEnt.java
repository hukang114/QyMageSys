package com.qymage.sys.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：
 * 类描述：借款详情
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1516:22
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class LoanQueryDetEnt {

    /**
     * id : 1099302100000000016
     * type : 1
     * borrower : 1099100700000000036
     * organ : 1099100700000000023
     * cause : 公司采购需要钱
     * amount : 1000.0
     * useDate : 2019-09-15 00:00:00.0
     * createTime : 2019-09-15 15:22:07.0
     * createBy : null
     * updateTime : null
     * updateBy : null
     * status : 03
     * deptName : 交付中心
     * personName : 孔俊智
     * auditor : null
     * copier : null
     * keyword : null
     * stats : null
     * processInstId : 197505
     * activityVoList : [{"id":"1173134876833968129","processInstanceId":"197505","processDefId":"loan_process:1:180004","assignee":"1099100700000000036","name":null,"status":"","comment":"","actType":"startEvent","actId":"197507","businessKey":"1099302100000000016","createdDate":1568532125000,"updatedDate":1568532125000,"userId":"1099100700000000036","userName":"孔俊智"},{"id":null,"processInstanceId":"197505","processDefId":"loan_process:1:180004","assignee":"1099100700000000033","name":"经理审批","status":null,"comment":null,"actType":"userTask","actId":"197512","businessKey":null,"createdDate":null,"updatedDate":null,"userId":"1099100700000000033","userName":"冯秋"},{"id":null,"processInstanceId":"197505","processDefId":"loan_process:1:180004","assignee":"1099100700000000036","name":"经理审批","status":null,"comment":null,"actType":"userTask","actId":"197533","businessKey":null,"createdDate":null,"updatedDate":null,"userId":"1099100700000000036","userName":"孔俊智"}]
     * sendList : [{"userName":"孔俊智","userId":"1099100700000000036"},{"userName":"孔俊智","userId":"1099100700000000036"}]
     */

    public String id;
    public String type;
    public String borrower;
    public String organ;
    public String cause;
    public double amount;
    public String useDate;
    public String createTime;
    public String createBy;
    public String updateTime;
    public String updateBy;
    public String status;
    public String deptName;
    public String personName;
    public String stats;
    public String processInstId;
    public List<ActivityVoListBean> activityVoList;
    public List<SendListBean> sendList;
    public int actStatus;

    public static class ActivityVoListBean implements Serializable {
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
        public String createdDate;
        public String updatedDate;
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
