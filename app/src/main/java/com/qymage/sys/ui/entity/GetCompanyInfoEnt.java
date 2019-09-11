package com.qymage.sys.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：
 * 类描述：2.8按条件模糊查询单位信息
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1120:21
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetCompanyInfoEnt implements Serializable {


  /*  companyName:string //公司名称
                 account：string 账号                
                             bank：String 开户行/行号                 
    creditCode  String  信用代码                 
    invoicePhone   String  开票电话               
                          invoiceAddress String 开票地址                  
    contacts String  联系人                  
    phone：String  联系电话*/


    public String companyName;
    public String account;
    public String bank;
    public String creditCode;
    public String invoicePhone;
    public String invoiceAddress;
    public String contacts;
    public String phone;
    //----------3.2投标/履约保证金-收查询对应支数据的接口------------


   /* id:string //主键
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
    }...]*/

    public String projectId;
    public String companyId;
    public String amountName;
    public String name;
    public String amount;
    public String date;
    public String endDate;
    public List<FileListBean> fileList;

    public static class FileListBean {
        /**
         * fileName : 12
         * filePath : 12
         */
        public String fileName;
        public String filePath;
    }


}
