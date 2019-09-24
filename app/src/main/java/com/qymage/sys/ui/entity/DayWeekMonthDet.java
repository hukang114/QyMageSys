package com.qymage.sys.ui.entity;

import com.qymage.sys.ui.Test;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：日报详情 ，月报详情 周报详情
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1621:59
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class DayWeekMonthDet implements Serializable{

    // 日报====、
    public String processInstanceId;
    public String createTime;// 日期  2019-09-09 9:00
    public String dayWork;//昨日工作计划
    public String wordplay;// 明日工作计划
    public String jobWord;//  本周工作总结
    public String nextWork;//  下周工作计划
    public String actStatus; //审批状态
    public String id;
    public int canCancelTask;

    // 周报===================
    public String userCode;//工号
    public String name;//姓名
    public String date;//  日期  2019-09-09 9:00
    public String lastWeek;//上周工作计划
    public String weekDay;//本周工作总结
    public String nextWeek;// 下周工作计划

    // 月报==================
    public String monthWork;// 本月工作计划
    public String submonthWork;//本月工作总结
    public String nextMonthWeek;//   下月工作计划
    public String selg;//自我评级
    public String leadComment;//  领导评语
    public String leadGrade;//  领导打分
    public String Id;
    public String userName;

    public List<LogListBean> logList;

    public static class LogListBean implements Serializable {
        /**
         * contractNo : z测试-合同编号
         * contractName : z测试-合同名称
         * projectNo : z测试-项目编号
         * projectName : z测试-项目名称
         * manHours : 22.0
         * workContent : null
         * subMoney : 333
         * subMoneyList : [{"amount":"333","type":"1","detailed":"z测试-报销明细","photo":null}]
         */

        public String contractNo;
        public String contractName;
        public String projectNo;
        public String projectName;
        public String manHours;
        public String workContent;
        public String subMoney;
        public List<LogListBean.SubMoneyListBean> subMoneyList;

        public static class SubMoneyListBean implements Serializable {
            /**
             * amount : 333
             * type : 1
             * detailed : z测试-报销明细
             * photo : null
             */

            public String amount;
            public String type;
            public String typeName;
            public String detailed;
            public String photo;
        }
    }


    public List<ActivityVoBean> activityVo;

    public static class ActivityVoBean implements Serializable{
        /**
         * id : 1171709251275427842
         * processInstanceId : 150077
         * processDefId : projcet_process:3:127512
         * assignee : 1099100700000000036
         * name : null
         * status :
         * comment :
         * actType : startEvent
         * actId : 150078
         * businessKey : 1171709245101412354
         * createdDate : 1568192229000
         * updatedDate : 1568192229000
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

}
