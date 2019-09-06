package com.qymage.sys.ui.entity;

import java.io.Serializable;

/**
 * Created by admin on 2019/9/7.
 * <p>
 * /收-付款明细
 */

public class CompanyMoneyPaymentVOS implements Serializable {


    public String amount;//金额
    public String date;//日期
    public String remarks;//备注

    public CompanyMoneyPaymentVOS(String amount, String date, String remarks) {
        this.amount = amount;
        this.date = date;
        this.remarks = remarks;
    }
}
