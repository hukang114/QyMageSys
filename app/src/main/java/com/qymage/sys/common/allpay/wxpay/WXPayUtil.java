package com.qymage.sys.common.allpay.wxpay;

import android.content.Context;
import android.util.Log;

import com.qymage.sys.BuildConfig;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付 on 2016/7/19.
 */
public class WXPayUtil {

    private static WXPayUtil mWXPay;
    private IWXAPI mWXApi;
    private String mPayParam;
    private WXPayResultCallBack mCallback;


    public static final int NO_OR_LOW_WX = 1;   //未安装微信或微信版本过低
    public static final int ERROR_PAY_PARAM = 2;  //支付参数错误
    public static final int ERROR_PAY = 3;  //支付失败


    public interface WXPayResultCallBack {
        //        void onSuccess(); //支付成功
        void onError(int error_code);   //未安装微信或不支持微信支付

        //        void onCancel();    //支付取消
        void onRespResult(int resultStatus);
    }

    public WXPayUtil(Context context, String wx_appid) {
        mWXApi = WXAPIFactory.createWXAPI(context, null);
        mWXApi.registerApp(wx_appid);
    }

    public static void init(Context context, String wx_appid) {
        if (mWXPay == null) {
            mWXPay = new WXPayUtil(context, wx_appid);
        }
    }

    public static WXPayUtil getInstance() {
        return mWXPay;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    /**
     * 发起微信支付
     */
    public void doPay(String preid, WXPayResultCallBack callback) { // WXPayResultCallBack callback
        mCallback = callback;
        if (!check()) {
            if (mCallback != null) {
                mCallback.onError(NO_OR_LOW_WX);
            }
            return;
        }
        PayReq req = new PayReq();
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = preid;
        req.packageValue = "Sign=WXpay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(System.currentTimeMillis());

        SortedMap<Object, Object> st = new TreeMap<>();
        st.put("appid", req.appId);
        st.put("noncestr", req.nonceStr);
        st.put("package", req.packageValue);
        st.put("partnerid", req.partnerId);
        st.put("prepayid", req.prepayId);
        st.put("timestamp", req.timeStamp);
        req.sign = genAppSign(st);

        mWXApi.sendReq(req);

    }


    /**
     * 第一步中生成预支付需要生成随机字符串
     *
     * @return
     */
    public String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    private String genAppSign(SortedMap<Object, Object> params) {
        StringBuffer sb = new StringBuffer();
        Set es = params.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);//AppSecret

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        Log.d("WXPayUtil", appSign);
        if (BuildConfig.DEBUG) Log.d("WXPayUtil", sb.toString());
        return appSign;
    }

    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    //支付回调响应
    public void onResp(int error_code) {
        if (mCallback == null) {
            return;
        }
        mCallback.onRespResult(error_code);
//        if (error_code == 0) {   //成功
//            mCallback.onSuccess();
//        } else if (error_code == -1) {   //错误
//            mCallback.onError(ERROR_PAY);
//        } else if (error_code == -2) {   //取消
//            mCallback.onCancel();
//        }
        mCallback = null;
    }

    //检测是否支持微信支付
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    public void doPays(String app_id, String preid, String mch_id, String noncestr, String sign, WXPayResultCallBack callback) {
        mCallback = callback;
        if (!check()) {
            if (mCallback != null) {
                mCallback.onError(NO_OR_LOW_WX);
            }
            return;
        }
        PayReq req = new PayReq();
        req.appId = app_id;
        req.partnerId = mch_id;
        req.prepayId = preid;
        req.packageValue = "Sign=WXpay";
        req.nonceStr = noncestr;
        req.timeStamp = String.valueOf(System.currentTimeMillis());
        req.sign = sign;

        mWXApi.sendReq(req);

    }


}