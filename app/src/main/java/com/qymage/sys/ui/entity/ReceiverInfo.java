package com.qymage.sys.ui.entity;

import java.io.Serializable;

/**
 * Created by admin on 2019/9/5.
 * 收款方信息
 */

public class ReceiverInfo implements Serializable {

    //收款方信息↓
//    colName: String, // 付款方名称
//    colBank: String, // 卡户行
//    colAccount: String, // 银行账号
//    colCreditCode: String, //信用代码
//    colInvoicePhone: String, // 开票电话
//    colInvoiceAddress: String, // 开票地址
//    colContacts：String, // 联系人
//    colPhone：String, // 联系人

    public String colName;
    public String colBank;
    public String colAccount;
    public String colCreditCode;
    public String colInvoicePhone;
    public String colInvoiceAddress;
    public String colContacts;
    public String colPhone;

    public ReceiverInfo(String colName, String colBank, String colAccount, String colCreditCode, String colInvoicePhone, String colInvoiceAddress, String colContacts, String colPhone) {
        this.colName = colName;
        this.colBank = colBank;
        this.colAccount = colAccount;
        this.colCreditCode = colCreditCode;
        this.colInvoicePhone = colInvoicePhone;
        this.colInvoiceAddress = colInvoiceAddress;
        this.colContacts = colContacts;
        this.colPhone = colPhone;
    }

    public ReceiverInfo() {
    }
}
