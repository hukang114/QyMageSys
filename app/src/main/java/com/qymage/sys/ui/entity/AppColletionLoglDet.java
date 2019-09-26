package com.qymage.sys.ui.entity;

import com.qymage.sys.ui.Test2;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：
 * 类描述：收款申请详情-付款申请详情-开票申请详情-收票申请详情
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/131:09
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class AppColletionLoglDet implements Serializable {


    public long createTime;
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
    public String userName;
    public String auditor;
    public Object copier;
    public Object keyword;
    public String stats;
    public String processInstId;
    public String processInstanceId;
    public String actStatus;
    public String contractNo;
    public String contractName;
    public List<AppColletionLoglDet.ActivityVoBean> activityVo;
    public List<AppColletionLoglDet.SendListBean> sendList;
    public String thisMoney;
    public String thisAmount;
    public String notMoney;
    public String notTicket;
    public String notAmount;
    public String diffMoney;
    public String contractType;
    public String contractTypeName;
    // 收款方信息
    public String payName;
    public String payRemark;
    public String payBank;
    public String payAccount;
    public String payCreditCode;
    public String payInvoicePhone;
    public String payInvoiceAddress;
    public String payContacts;
    public String payPhone;
    // 付款方信息
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
    public int canCancelTask; // 是否可以撤销任务:1-可以 0-不可以

    public List<CompanyMoneyTicketVOSBean> companyMoneyTicketVOS;
    public List<CompanyMoneyPaymentVOSBean> companyMoneyPaymentVOS;
    public List<ThiscompanyMoneyPaymentVOSBean> thiscompanyMoneyPaymentVOS;
    public List<ThisompanyMoneyTicketVOBean> thisompanyMoneyTicketVO;


    // 本次开票本次收票
    public static class ThisompanyMoneyTicketVOBean implements Serializable {
        /**
         * id : 1099301100000000001
         * companyMoneyId : 1099300900000000021
         * amount : 100.00
         * taxeRate : 7.0
         * taxes : 7.00
         * paymentTime : 2019-09-22 00:00:00.0
         * total : null
         */

        public String id;
        public String companyMoneyId;
        public String amount;
        public double taxeRate;
        public double taxes;
        public String paymentTime;
        public String total;
        public String rateName;
    }

    /**
     * 本次收款 本次付款
     */
    public static class ThiscompanyMoneyPaymentVOSBean implements Serializable {
        /**
         * id : 1099301000000000030
         * companyMoneyId : 1099300900000000018
         * amount : 100.00
         * paymentTime : 2019-09-22 00:00:00.0
         * remarks : 测试
         * total : null
         */

        public String id;
        public String companyMoneyId;
        public String amount;
        public String paymentTime;
        public String remarks;
        public Object total;
    }

    //收付款明细
    public static class CompanyMoneyPaymentVOSBean implements Serializable {
        /**
         * Id : 1212
         * amount : 20
         */
        public String Id;
        public String amount;// 金额
        public String paymentTime;// 收付款日期
        public String remarks;// 备注
    }

    //收票开票明细
    public static class CompanyMoneyTicketVOSBean implements Serializable {
        public String Id;//
        public String amount;//金额
        public String taxRate;// 税率
        public String taxes;// 税金
        public String paymentTime;//  日期
        public String rateName;//税率名称
    }


    public List<AppColletionLoglDet.FileListBean> fileList;

    public static class FileListBean implements Serializable {
        /**
         * fileName : 12
         * filePath : 12
         */

        public String fileName;
        public String filePath;
    }

    public static class ActivityVoBean implements Serializable {
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
