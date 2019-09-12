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
     * data : [{"createTime":null,"createBy":null,"updateTime":null,"updateBy":null,"id":"1171771703866380289","projectId":"1171709245101412354","projectNo":"20190906","projectName":"企业管理系统","companyId":"1099100700000000020","companyName":"贵州润可信息科技有限公司","bidType":"01","name":"贵州省黔南龙里工商局","bank":"中国银行","account":"123456789","amount":"1500.00","amountName":"1","date":"2019-09-11 00:00:00.0","remark":"测试修改测试","endDate":"2019-09-12 00:00:00.0","activityVoList":null,"personName":"孔俊智","fileList":null,"auditor":null,"copier":null,"sendList":null,"keyword":null,"stats":null,"processInstId":null}]
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
         * id : 1171771703866380289
         * projectId : 1171709245101412354
         * projectNo : 20190906
         * projectName : 企业管理系统
         * companyId : 1099100700000000020
         * companyName : 贵州润可信息科技有限公司
         * bidType : 01
         * name : 贵州省黔南龙里工商局
         * bank : 中国银行
         * account : 123456789
         * amount : 1500.00
         * amountName : 1
         * date : 2019-09-11 00:00:00.0
         * remark : 测试修改测试
         * endDate : 2019-09-12 00:00:00.0
         * activityVoList : null
         * personName : 孔俊智
         * fileList : null
         * auditor : null
         * copier : null
         * sendList : null
         * keyword : null
         * stats : null
         * processInstId : null
         */

        public Object createTime;
        public Object createBy;
        public Object updateTime;
        public Object updateBy;
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
        public Object activityVoList;
        public String personName;
        public Object fileList;
        public Object auditor;
        public Object copier;
        public Object sendList;
        public Object keyword;
        public Object stats;
        public Object processInstId;
    }
}
