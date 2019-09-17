package com.qymage.sys.ui.entity;

import java.io.Serializable;

/**
 * 类名：报销记录
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1711:01
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ReimbursementLogEnt implements Serializable {

    public String id; //主键
    public String amountType;  // 费用类型（枚举名称：ExpenseType）
    public String reimburserName; // 报销人名称
    public String departmentName;  //部门名称
    public String projectNo; // 项目编号
    public String projectName; // 项目名称
    public String contractNo; //合同编号
    public String contractName; // 合同名称
    public String amount; // 报销金额
    public String date; // 报销日期
    public String remark;//备注
    public String actStatus; //审批状态


}
