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
     * data : [{"createTime":null,"createBy":null,"updateTime":null,"updateBy":null,"id":"1179012623849881601","projectType":null,"projectTypeName":null,"projectNo":"101011","projectName":"汇盛通财务费用","persion":null,"amount":null,"date":null,"introduction":null,"processInstId":null,"activityVoList":null,"personName":null,"actStatus":null,"canCancelTask":null,"startDate":null,"endDate":null,"keyword":null,"stats":null,"msgId":null,"read":null,"auditor":null,"copier":null,"sendList":null,"contractList":[{"contractType":"02","contractNo":"BT04","contractName":"汇盛通财务费用补贴","contractTypeName":"补贴","contractDetal":[{"contractNo":"BT04","contractName":"汇盛通财务费用补贴"}]},{"contractType":"03","contractNo":"TS04","contractName":"汇盛通财务费用退税","contractTypeName":"退税","contractDetal":[{"contractNo":"TS04","contractName":"汇盛通财务费用退税"}]},{"contractType":"04","contractNo":"QR04","contractName":"汇盛通财务费用现金收入","contractTypeName":"现金收入","contractDetal":[{"contractNo":"QR04","contractName":"汇盛通财务费用现金收入"}]},{"contractType":"05","contractNo":"CG03","contractName":"测试_吴昌强","contractTypeName":"外部采购","contractDetal":[{"contractNo":"CG03","contractName":"测试_吴昌强"}]},{"contractType":"11","contractNo":"SW04","contractName":"汇盛通财务费用商务","contractTypeName":"商务费用","contractDetal":[{"contractNo":"SW04","contractName":"汇盛通财务费用商务"}]},{"contractType":"12","contractNo":"QC04","contractName":"汇盛通财务费用现金支出","contractTypeName":"现金支出","contractDetal":[{"contractNo":"QC04","contractName":"汇盛通财务费用现金支出"},{"contractNo":"QR042","contractName":"汇盛通财务费用现金收入"}]}],"regarding":0}]
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
         * id : 1179012623849881601
         * projectType : null
         * projectTypeName : null
         * projectNo : 101011
         * projectName : 汇盛通财务费用
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
         * contractList : [{"contractType":"02","contractNo":"BT04","contractName":"汇盛通财务费用补贴","contractTypeName":"补贴","contractDetal":[{"contractNo":"BT04","contractName":"汇盛通财务费用补贴"}]},{"contractType":"03","contractNo":"TS04","contractName":"汇盛通财务费用退税","contractTypeName":"退税","contractDetal":[{"contractNo":"TS04","contractName":"汇盛通财务费用退税"}]},{"contractType":"04","contractNo":"QR04","contractName":"汇盛通财务费用现金收入","contractTypeName":"现金收入","contractDetal":[{"contractNo":"QR04","contractName":"汇盛通财务费用现金收入"}]},{"contractType":"05","contractNo":"CG03","contractName":"测试_吴昌强","contractTypeName":"外部采购","contractDetal":[{"contractNo":"CG03","contractName":"测试_吴昌强"}]},{"contractType":"11","contractNo":"SW04","contractName":"汇盛通财务费用商务","contractTypeName":"商务费用","contractDetal":[{"contractNo":"SW04","contractName":"汇盛通财务费用商务"}]},{"contractType":"12","contractNo":"QC04","contractName":"汇盛通财务费用现金支出","contractTypeName":"现金支出","contractDetal":[{"contractNo":"QC04","contractName":"汇盛通财务费用现金支出"},{"contractNo":"QR042","contractName":"汇盛通财务费用现金收入"}]}]
         * regarding : 0
         */

        public Object createTime;
        public Object createBy;
        public Object updateTime;
        public Object updateBy;
        public String id;
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
             * contractType : 02
             * contractNo : BT04
             * contractName : 汇盛通财务费用补贴
             * contractTypeName : 补贴
             * contractDetal : [{"contractNo":"BT04","contractName":"汇盛通财务费用补贴"}]
             */

            public String contractType;
            public String contractNo;
            public String contractName;
            public String contractTypeName;
            public List<ContractDetalBean> contractDetal;

            public static class ContractDetalBean {
                /**
                 * contractNo : BT04
                 * contractName : 汇盛通财务费用补贴
                 */

                public String contractNo;
                public String contractName;
            }
        }
    }
}
