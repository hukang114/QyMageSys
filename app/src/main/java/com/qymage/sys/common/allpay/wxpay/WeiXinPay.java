package com.qymage.sys.common.allpay.wxpay;

/**
 * Created by HK on 2018/1/22.
 */

public class WeiXinPay {


    /**
     * code : 200
     * message : 数据返回成功
     * data : {"appid":"wxaf911714d6b5305b","mch_id":"1467114402","nonce_str":"DjTpjEbhoPNvZ071","prepay_id":"wx201801221741145102d11b180637605304","result_code":"SUCCESS","return_code":"SUCCESS","return_msg":"OK","sign":"30024B076D8D80BB055D99AE8A2B47AA","trade_type":"APP"}
     */

    public int code;
    public String message;
    public DataBean data;

    public static class DataBean {
        /**
         * appid : wxaf911714d6b5305b
         * mch_id : 1467114402
         * nonce_str : DjTpjEbhoPNvZ071
         * prepay_id : wx201801221741145102d11b180637605304
         * result_code : SUCCESS
         * return_code : SUCCESS
         * return_msg : OK
         * sign : 30024B076D8D80BB055D99AE8A2B47AA
         * trade_type : APP
         */
        public String appid;
        public String mch_id;
        public String nonce_str;
        public String prepay_id;
        public String result_code;
        public String return_code;
        public String return_msg;
        public String sign;
        public String trade_type;
    }
}
