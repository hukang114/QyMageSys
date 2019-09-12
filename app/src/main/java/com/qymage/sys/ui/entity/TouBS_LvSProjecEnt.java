package com.qymage.sys.ui.entity;

import com.qymage.sys.ui.Test2;

import java.util.List;

/**
 * 类名：
 * 类描述：投标/履约保证金-收查询对应支数据的接口
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1211:16
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class TouBS_LvSProjecEnt {



  /*  id:string //主键
    projectId:string //项目id
    companyId:string //公司名称
    amountName:string //保证金名称
    name:string //付款单位名称
    bank:string //开户行/行号
    account:string //银行账号
    amount:double//保证金金额（元）
    date: Date //开始日期
    endDate: Date //截止日期  */

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
    // 附件
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
