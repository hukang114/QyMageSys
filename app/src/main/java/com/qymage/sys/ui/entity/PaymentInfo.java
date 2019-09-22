package com.qymage.sys.ui.entity;

import java.io.Serializable;

/**
 * Created by admin on 2019/9/5.
 * <p>
 * 付款方信息
 */

public class PaymentInfo implements Serializable {

    //付款方信息↓
    public String payName; // 付款方名称
    public String payBank; // 卡户行
    public String payAccount; // 银行账号
    public String payCreditCode; //信用代码
    public String payInvoicePhone; // 开票电话
    public String payInvoiceAddress; // 开票地址
    public String payContacts; // 联系人
    public String payPhone; // 联系人

    public PaymentInfo(String payName, String payBank, String payAccount, String payCreditCode, String payInvoicePhone, String payInvoiceAddress, String payContacts, String payPhone) {
        this.payName = payName;
        this.payBank = payBank;
        this.payAccount = payAccount;
        this.payCreditCode = payCreditCode;
        this.payInvoicePhone = payInvoicePhone;
        this.payInvoiceAddress = payInvoiceAddress;
        this.payContacts = payContacts;
        this.payPhone = payPhone;
    }

    public PaymentInfo() {

    }
}
