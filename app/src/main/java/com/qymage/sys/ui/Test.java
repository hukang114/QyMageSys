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
     * data : [{"createTime":null,"createBy":null,"updateTime":null,"updateBy":null,"id":null,"projectType":null,"projectTypeName":null,"projectNo":"160818","projectName":"晴隆气站","persion":null,"amount":null,"date":null,"introduction":null,"processInstId":null,"activityVoList":null,"personName":null,"actStatus":null,"canCancelTask":null,"startDate":null,"endDate":null,"keyword":null,"stats":null,"msgId":null,"read":null,"auditor":null,"copier":null,"sendList":null,"contractList":[{"contractType":"11","contractNo":"SW17","contractName":"晴隆气站商务","contractTypeName":"商务费用"},{"contractType":"02","contractNo":"BT17","contractName":"晴隆气站补贴","contractTypeName":"补贴"},{"contractType":"03","contractNo":"TS17","contractName":"晴隆气站退税","contractTypeName":"退税"},{"contractType":"04","contractNo":"QR17","contractName":"晴隆气站现金收入","contractTypeName":"现金收入"},{"contractType":"12","contractNo":"QC17","contractName":"晴隆气站现金支出","contractTypeName":"现金支出"}],"regarding":0}]
     */

    public boolean success;
    public int code;
    public String message;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * createTime : null
         * createBy : null
         * updateTime : null
         * updateBy : null
         * id : null
         * projectType : null
         * projectTypeName : null
         * projectNo : 160818
         * projectName : 晴隆气站
         * persion : null
         * amount : null
         * date : null
         * introduction : null
         * processInstId : null
         * activityVoList : null
         * personName : null
         * actStatus : null
         * canCancelTask : null
         * startDate : null
         * endDate : null
         * keyword : null
         * stats : null
         * msgId : null
         * read : null
         * auditor : null
         * copier : null
         * sendList : null
         * contractList : [{"contractType":"11","contractNo":"SW17","contractName":"晴隆气站商务","contractTypeName":"商务费用"},{"contractType":"02","contractNo":"BT17","contractName":"晴隆气站补贴","contractTypeName":"补贴"},{"contractType":"03","contractNo":"TS17","contractName":"晴隆气站退税","contractTypeName":"退税"},{"contractType":"04","contractNo":"QR17","contractName":"晴隆气站现金收入","contractTypeName":"现金收入"},{"contractType":"12","contractNo":"QC17","contractName":"晴隆气站现金支出","contractTypeName":"现金支出"}]
         * regarding : 0
         */

        public Object createTime;
        public Object createBy;
        public Object updateTime;
        public Object updateBy;
        public Object id;
        public Object projectType;
        public Object projectTypeName;
        public String projectNo;
        public String projectName;
        public Object persion;
        public Object amount;
        public Object date;
        public Object introduction;
        public Object processInstId;
        public Object activityVoList;
        public Object personName;
        public Object actStatus;
        public Object canCancelTask;
        public Object startDate;
        public Object endDate;
        public Object keyword;
        public Object stats;
        public Object msgId;
        public Object read;
        public Object auditor;
        public Object copier;
        public Object sendList;
        public int regarding;
        public List<ContractListBean> contractList;

        public static class ContractListBean {
            /**
             * contractType : 11
             * contractNo : SW17
             * contractName : 晴隆气站商务
             * contractTypeName : 商务费用
             */

            public String contractType;
            public String contractNo;
            public String contractName;
            public String contractTypeName;
        }
    }
}
