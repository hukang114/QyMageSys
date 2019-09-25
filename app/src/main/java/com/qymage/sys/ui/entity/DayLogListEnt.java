package com.qymage.sys.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：日报提交里面的合同明细
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/914:43
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class DayLogListEnt implements Serializable {

    public String contractNo; //合同编号
    public String contractName;//合同名称
    public String projectNo;//项目编号
    public String projectName;//项目名称
    public String manHours;//工时比例
    public String workContent;// 工作内容
    public String subMoney;//  报销费用

    public DayLogListEnt() {

    }

    public List<SubMoneyListEntity> subMoneyList;

    public DayLogListEnt(String contractNo, String contractName, String projectNo, String projectName, String manHours, String workContent, String subMoney, List<SubMoneyListEntity> subMoneyList) {
        this.contractNo = contractNo;
        this.contractName = contractName;
        this.projectNo = projectNo;
        this.projectName = projectName;
        this.manHours = manHours;
        this.workContent = workContent;
        this.subMoney = subMoney;
        this.subMoneyList = subMoneyList;
    }

    public static class SubMoneyListEntity implements Serializable {

        public SubMoneyListEntity() {
        }

        public String amount;//  报销金额
        public String type;//报销类别  1-交通费、2-市内交通、3-过路费用、4-加油费用、5-停/洗车费、6-住宿费用、7-业务餐费、8-礼品费用、9-快递费用
        public String detailed;// 明细
        public String photo;
        public String typeName;
        public List<String> stringList;

        public SubMoneyListEntity(String amount, String type, String detailed, String photo, List<String> stringList, String typeName) {
            this.amount = amount;
            this.type = type;
            this.detailed = detailed;
            this.photo = photo;
            this.stringList = stringList;
            this.typeName = typeName;
        }
    }


}
