package com.qymage.sys.ui.entity;

import java.io.Serializable;

/**
 * Created by admin on 2019/9/5.
 * 添加付款比列
 */

public class ContractPayEnt implements Serializable{

//    date:Date，//付款日期
//    payScale:Double,//比例（百分比）
//    amount：Double//金额


    public String date;
    public int payScale;
    public String amount;

    public ContractPayEnt( String date, int payScale, String amount) {
        this.date = date;
        this.payScale = payScale;
        this.amount = amount;
    }
}
