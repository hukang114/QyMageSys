package com.qymage.sys.ui.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1221:50
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetReceivedInfoEnt implements Serializable {


  /*  endAmount String  已收款 已付款
    notAmount String  未收款  未付款
    endTicket  String  已开票  未开票
    notTicket:String  未开票  未收票
    companyMoneyPaymentVO：[{//收付款明细
        Id:String
        amount:String 金额
        paymentTime String 收付款日期
        remarks String  备注
    }],
    companyMoneyTicketVO：[{//收票开票明细
        Id:String
        amount:String 金额
        taxeRate String 税率
        taxes String  税金
        paymentTime String  日期
    }]*/

    public double notTicket;
    public double notAmount;
    public double endTicket;
    public double endAmount;

    public List<CompanyMoneyTicketVOBean> companyMoneyTicketVO;
    public List<CompanyMoneyPaymentVOBean> companyMoneyPaymentVO;

    //收付款明细
    public static class CompanyMoneyPaymentVOBean implements Serializable {
        /**
         * Id : 1212
         * amount : 20
         */
        public String Id;
        public String amount;// 金额
        public String paymentTime;// 收付款日期
        public String remarks;// 备注
    }

    //收票开票明细
    public static class CompanyMoneyTicketVOBean implements Serializable {
        public String Id;//
        public String amount;//金额
        public String taxRate;// 税率
        public String taxes;// 税金
        public String paymentTime;//  日期
    }


}
