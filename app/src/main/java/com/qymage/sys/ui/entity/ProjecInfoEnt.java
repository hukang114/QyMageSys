package com.qymage.sys.ui.entity;

import com.qymage.sys.ui.Test;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：
 * 类描述：2.6根据合同编号/合同名称查询
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/918:30
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ProjecInfoEnt implements Serializable {


    public String id;// //合同主键
    public String projectNo; // 项目编号
    public String projectName; //项目名称
    public String contractType; // 合同类型
    public String projectId;//项目主键 
    public String contractNo;//合同编号
    public String contractName; // 合同名称
    public String company_name;// 公司名称
    public String contractTypeName;// 合同类型名称

    public List<ContractListBean> contractList;

    public static class ContractListBean implements Serializable{
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
