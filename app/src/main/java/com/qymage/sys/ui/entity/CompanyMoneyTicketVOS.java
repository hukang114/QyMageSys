package com.qymage.sys.ui.entity;

/**
 * Created by admin on 2019/9/7.
 * <p>
 * //开  -收  -票明细
 */

public class CompanyMoneyTicketVOS {

    public double amount;//金额
    public double taxRate;//税率
    public double taxes;//税金
    public String paymentTime;//日期

    public CompanyMoneyTicketVOS(double amount, double taxRate, double taxes, String paymentTime) {
        this.amount = amount;
        this.taxRate = taxRate;
        this.taxes = taxes;
        this.paymentTime = paymentTime;
    }
}