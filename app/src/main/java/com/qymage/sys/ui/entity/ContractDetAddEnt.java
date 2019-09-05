package com.qymage.sys.ui.entity;

import java.io.Serializable;

/**
 * Created by admin on 2019/9/4.
 * 添加合同明细的实体类
 */

public class ContractDetAddEnt implements Serializable {


//    amount:Double,//金额
//    taxRate：Double，//税率
//    taxes：Double//税金

    public String amount;
    public int taxRate;
    public String taxes;


    public ContractDetAddEnt(String amount, int taxRate, String taxes) {
        this.amount = amount;
        this.taxRate = taxRate;
        this.taxes = taxes;
    }
}
