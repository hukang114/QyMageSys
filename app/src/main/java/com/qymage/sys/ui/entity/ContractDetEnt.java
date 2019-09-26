package com.qymage.sys.ui.entity;

import com.qymage.sys.ui.Test2;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：合同详情
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1116:07
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ContractDetEnt implements Serializable {

    /**
     * createTime : null
     * createBy : null
     * updateTime : null
     * updateBy : null
     * id : 1171631419568410625
     * projectId : 1099301100000000001
     * projectNo : 20190901
     * projectName : 省水站
     * contractNo : QR05
     * amount : 1000.0
     * contractType : 04
     * contractTypeName : 现金收入
     * date : 2019-09-11 00:00:00.0
     * contractName : 测试合同
     * creater : null
     * payRemark : 测试备注
     * payName : 胡康
     * payBank : 建设银行卡
     * payAccount : 1234585554555
     * payCreditCode : 25555245555
     * payInvoicePhone : 0851252388
     * payInvoiceAddress : 贵州省贵阳市
     * payContacts :  胡萝卜
     * payPhone : 18786637464
     * colName : 张总
     * colBank : 工商银行
     * colAccount : 12345689
     * colCreditCode : 1111111
     * colInvoicePhone : 18786637464
     * colInvoiceAddress : 贵州省黔南龙里
     * colPhone : 18786637464
     * colContacts : 胡康
     * contractDetails : [{"id":"1171631421661368321","contractId":"1171631419568410625","taxRate":"1","amount":100,"taxes":1}]
     * contractPayscale : [{"id":"1171631421694922754","contractId":"1171631419568410625","date":"2019-09-11 00:00:00.0","payScale":"1","amount":1000}]
     * fileList : []
     * endAmount : null
     * endTicket : null
     * activityVoList : [{"id":"1171631426673561601","processInstanceId":"147514","processDefId":"contract_process:1:145020","assignee":"1099100700000000036","name":null,"status":"","comment":"","actType":"startEvent","actId":"147515","businessKey":"1171631419568410625","createdDate":1568173675000,"updatedDate":1568173675000},{"id":null,"processInstanceId":"147514","processDefId":"contract_process:1:145020","assignee":"1099100700000000033","name":null,"status":null,"comment":null,"actType":"userTask","actId":"147518","businessKey":null,"createdDate":null,"updatedDate":null},{"id":null,"processInstanceId":"147514","processDefId":"contract_process:1:145020","assignee":"1099100700000000036","name":null,"status":null,"comment":null,"actType":"userTask","actId":"147530","businessKey":null,"createdDate":null,"updatedDate":null}]
     * personName : 孔俊智
     * auditor : null
     * copier : null
     * keyword : null
     * stats : null
     * processInstId : 147514
     * sendList : [{"userName":"孔俊智","userId":"1099100700000000036"},{"userName":"孔俊智","userId":"1099100700000000036"}]
     * regarding : 0
     */

    public long createTime;
    public Object createBy;
    public Object updateTime;
    public Object updateBy;
    public String id;
    public String projectId;
    public String projectNo;
    public String projectName;
    public String contractNo;
    public double amount;
    public String contractType;
    public String contractTypeName;
    public String date;
    public String contractName;
    public Object creater;
    public String payRemark;
    public String payName;
    public String payBank;
    public String payAccount;
    public String payCreditCode;
    public String payInvoicePhone;
    public String payInvoiceAddress;
    public String payContacts;
    public String payPhone;
    public String colName;
    public String colBank;
    public String colAccount;
    public String colCreditCode;
    public String colInvoicePhone;
    public String colInvoiceAddress;
    public String colPhone;
    public String colContacts;
    public String endAmount;
    public String endTicket;
    public String personName;
    public Object auditor;
    public Object copier;
    public Object keyword;
    public Object stats;
    public String processInstId;
    public String actStatus;
    public int regarding;
    public List<ContractDetailsBean> contractDetails;
    public List<ContractPayscaleBean> contractPayscale;
    public List<FileListBean> fileList;
    public int canCancelTask;

    public static class FileListBean implements Serializable {
        /**
         * fileName : 12
         * filePath : 12
         */

        public String fileName;
        public String filePath;
    }

    public List<ActivityVoListBean> activityVoList;
    public List<SendListBean> sendList;

    public static class ContractDetailsBean implements Serializable {
        /**
         * id : 1171631421661368321
         * contractId : 1171631419568410625
         * taxRate : 1
         * amount : 100.0
         * taxes : 1.0
         */

        public String id;
        public String contractId;
        public int taxRate;
        public double amount;
        public double taxes;
        public String rateName;
    }

    public static class ContractPayscaleBean implements Serializable {
        /**
         * id : 1171631421694922754
         * contractId : 1171631419568410625
         * date : 2019-09-11 00:00:00.0
         * payScale : 1
         * amount : 1000.0
         */

        public String id;
        public String contractId;
        public String date;
        public int payScale;
        public double amount;
    }

    public static class ActivityVoListBean implements Serializable {
        /**
         * id : 1171631426673561601
         * processInstanceId : 147514
         * processDefId : contract_process:1:145020
         * assignee : 1099100700000000036
         * name : null
         * status :
         * comment :
         * actType : startEvent
         * actId : 147515
         * businessKey : 1171631419568410625
         * createdDate : 1568173675000
         * updatedDate : 1568173675000
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
