package com.qymage.sys.ui;

import java.util.List;

/**
 * 类名：
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1011:48
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class Test2 {


    /**
     * success : true
     * code : 200
     * message : 请求成功
     * data : {"id":"1099300900000000021","openType":"102","projectNo":"190903","projectName":"企业管理系统","contractType":"07","contractNo":"YJ01","contractName":"测试合同","endAmount":"300.00","notAmount":"700.00","endTicket":"0.00","notTicket":"0.00","diffMoney":"-300.00","thisAmount":"100.00","fileName":null,"filePath":null,"payName":"张总","payBank":"工商银行","payAccount":"12345689","payInvoicePhone":"18786637464","payInvoiceAddress":"贵州省黔南龙里","payCreditCode":"1111111","payContacts":"好","payPhone":"18786637464","colName":"张总","colBank":"工商银行","colAccount":"12345689","colCreditCode":"1111111","colInvoicePhone":"18786637464","colInvocieAddress":null,"colContacts":"胡","colPhone":"18786637464","createTime":1569150077000,"createBy":null,"updateTime":null,"updateBy":null,"companyMoneyPaymentVOS":null,"companyMoneyTicketVOS":[{"id":"1099301100000000001","companyMoneyId":null,"amount":"100.00","taxeRate":null,"taxes":"7.00","paymentTime":null,"endTicket":null,"taxRate":null}],"thiscompanyMoneyPaymentVOS":[],"thisompanyMoneyTicketVO":[{"id":"1099301100000000001","companyMoneyId":"1099300900000000021","amount":"100.00","taxeRate":"7.0","taxes":"7.00","paymentTime":"2019-09-22 00:00:00.0","total":null}],"activityVo":null,"auditor":null,"copier":null,"sendList":null,"msgId":null,"read":null,"actStatus":null,"userName":null,"processInstanceId":null,"attachment":null,"fileList":[{"id":"1099301300000000214","modeId":"1099300900000000021","fileName":"wx_camera_1569076803791.jpg","filePath":"https://runke-oa.oss-cn-shenzhen.aliyuncs.com/emp/wx_camera_1569076803791.jpg?Expires=1569153668&OSSAccessKeyId=LTAIQEhNlRdvbxee&Signature=352n3VQWm%2FlAvCqyNxnI9CL63F4%3D","createDate":null}]}
     */

    public boolean success;
    public int code;
    public String message;
    public DataBean data;

    public static class DataBean {
        /**
         * id : 1099300900000000021
         * openType : 102
         * projectNo : 190903
         * projectName : 企业管理系统
         * contractType : 07
         * contractNo : YJ01
         * contractName : 测试合同
         * endAmount : 300.00
         * notAmount : 700.00
         * endTicket : 0.00
         * notTicket : 0.00
         * diffMoney : -300.00
         * thisAmount : 100.00
         * fileName : null
         * filePath : null
         * payName : 张总
         * payBank : 工商银行
         * payAccount : 12345689
         * payInvoicePhone : 18786637464
         * payInvoiceAddress : 贵州省黔南龙里
         * payCreditCode : 1111111
         * payContacts : 好
         * payPhone : 18786637464
         * colName : 张总
         * colBank : 工商银行
         * colAccount : 12345689
         * colCreditCode : 1111111
         * colInvoicePhone : 18786637464
         * colInvocieAddress : null
         * colContacts : 胡
         * colPhone : 18786637464
         * createTime : 1569150077000
         * createBy : null
         * updateTime : null
         * updateBy : null
         * companyMoneyPaymentVOS : null
         * companyMoneyTicketVOS : [{"id":"1099301100000000001","companyMoneyId":null,"amount":"100.00","taxeRate":null,"taxes":"7.00","paymentTime":null,"endTicket":null,"taxRate":null}]
         * thiscompanyMoneyPaymentVOS : []
         * thisompanyMoneyTicketVO : [{"id":"1099301100000000001","companyMoneyId":"1099300900000000021","amount":"100.00","taxeRate":"7.0","taxes":"7.00","paymentTime":"2019-09-22 00:00:00.0","total":null}]
         * activityVo : null
         * auditor : null
         * copier : null
         * sendList : null
         * msgId : null
         * read : null
         * actStatus : null
         * userName : null
         * processInstanceId : null
         * attachment : null
         * fileList : [{"id":"1099301300000000214","modeId":"1099300900000000021","fileName":"wx_camera_1569076803791.jpg","filePath":"https://runke-oa.oss-cn-shenzhen.aliyuncs.com/emp/wx_camera_1569076803791.jpg?Expires=1569153668&OSSAccessKeyId=LTAIQEhNlRdvbxee&Signature=352n3VQWm%2FlAvCqyNxnI9CL63F4%3D","createDate":null}]
         */

        public String id;
        public String openType;
        public String projectNo;
        public String projectName;
        public String contractType;
        public String contractNo;
        public String contractName;
        public String endAmount;
        public String notAmount;
        public String endTicket;
        public String notTicket;
        public String diffMoney;
        public String thisAmount;
        public Object fileName;
        public Object filePath;
        public String payName;
        public String payBank;
        public String payAccount;
        public String payInvoicePhone;
        public String payInvoiceAddress;
        public String payCreditCode;
        public String payContacts;
        public String payPhone;
        public String colName;
        public String colBank;
        public String colAccount;
        public String colCreditCode;
        public String colInvoicePhone;
        public Object colInvocieAddress;
        public String colContacts;
        public String colPhone;
        public long createTime;
        public Object createBy;
        public Object updateTime;
        public Object updateBy;
        public Object companyMoneyPaymentVOS;
        public Object activityVo;
        public Object auditor;
        public Object copier;
        public Object sendList;
        public Object msgId;
        public Object read;
        public Object actStatus;
        public Object userName;
        public Object processInstanceId;
        public Object attachment;
        public List<CompanyMoneyTicketVOSBean> companyMoneyTicketVOS;
        public List<?> thiscompanyMoneyPaymentVOS;
        public List<ThisompanyMoneyTicketVOBean> thisompanyMoneyTicketVO;
        public List<FileListBean> fileList;

        public static class CompanyMoneyTicketVOSBean {
            /**
             * id : 1099301100000000001
             * companyMoneyId : null
             * amount : 100.00
             * taxeRate : null
             * taxes : 7.00
             * paymentTime : null
             * endTicket : null
             * taxRate : null
             */

            public String id;
            public Object companyMoneyId;
            public String amount;
            public Object taxeRate;
            public String taxes;
            public Object paymentTime;
            public Object endTicket;
            public Object taxRate;
        }

        public static class ThisompanyMoneyTicketVOBean {
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
            public String taxeRate;
            public String taxes;
            public String paymentTime;
            public String total;
        }

        public static class FileListBean {
            /**
             * id : 1099301300000000214
             * modeId : 1099300900000000021
             * fileName : wx_camera_1569076803791.jpg
             * filePath : https://runke-oa.oss-cn-shenzhen.aliyuncs.com/emp/wx_camera_1569076803791.jpg?Expires=1569153668&OSSAccessKeyId=LTAIQEhNlRdvbxee&Signature=352n3VQWm%2FlAvCqyNxnI9CL63F4%3D
             * createDate : null
             */

            public String id;
            public String modeId;
            public String fileName;
            public String filePath;
            public Object createDate;
        }
    }
}
