package com.qymage.sys.ui.entity;

import java.io.Serializable;

/**
 * Created by admin on 2019/9/7.
 * <p>
 * //开  -收  -票明细
 */

public class CompanyMoneyTicketVOS implements Serializable {

    public String amount;//金额
    public int taxRate;//税率
    public double taxes;//税金
    public String paymentTime;//日期
    public String rateName;// 税率名称

    public CompanyMoneyTicketVOS() {
    }

    public CompanyMoneyTicketVOS(String amount, int taxRate, double taxes, String paymentTime, String rateName) {
        this.amount = amount;
        this.taxRate = taxRate;
        this.taxes = taxes;
        this.paymentTime = paymentTime;
        this.rateName = rateName;
    }
}
