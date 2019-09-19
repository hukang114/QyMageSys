package com.qymage.sys.ui.entity;

import java.util.List;

/**
 * 类名：
 * 类描述：投标支收 履约收支记录
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/129:42
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class BidLvYueSZListEnt {


  /*  {
        id:string //主键
        projectId:string //项目id
        companyId:string //公司名称
        amountName:string //保证金名称
        name:string //付款单位名称
        bank:string //开户行/行号
        account:string //银行账号
        amount:double//保证金金额（元）
        date: Date //开始日期
        endDate: Date //截止日期
        fileList: [{
        fileName: String//  文件名称
        filePath：String// 文件url
    }...]
    */


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
    public int stats;
    public String processInstId;
    public String msgId;// 消息id
    public int read;// 消息状态  0未读  1已读
    public String actStatus;////审批状态
}
