package com.qymage.sys.ui.entity;

import java.util.List;

/**
 * Created by admin on 2019/8/28.
 * 我的借款列表
 */

public class MyLoanEnt {


    public String TaskId;//   任务ID
    public String userCode;//  工号
    public String name;// 姓名
    public String orgName;// 所属部门名称
    public String cause;//  事由
    public String amount;//  金额
    public String useDate;//  使用日期
    public String pay;//  还款日期
    public String actStatus;// //审批状态
    public String portrait;
    public String personName;
    public String createTime;
    public String deptName;
    public String id;
    public String processInstId;
    public String msgId;// 消息id
    public int read;// 消息状态  0未读  1已读
    public int canCancelTask;
    public int returnStatus;
    public String assignees;
    public String type;


    /**
     * processList : [{"date":"name","node":" 审核状态","name":"战士","remarks":"备注"}]
     */
    public List<ProcessListEntity> processList;

    public class ProcessListEntity {
        /**
         * date : name
         * node :  审核状态
         * name : 战士
         * remarks : 备注
         */
        public String date;
        public String node;
        public String name;
        public String remarks;
    }


}
