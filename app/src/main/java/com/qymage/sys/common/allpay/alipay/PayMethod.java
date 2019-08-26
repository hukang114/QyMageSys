package com.qymage.sys.common.allpay.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


/** 
 * @author  作者
 * @date 创建时间：2015年6月12日 上午11:59:21 
 * @version 1.0 
 * @parameter  
 * 外部支付时候调用
 */
public class PayMethod {
	private static final int SDK_PAY_FLAG = 5;
	private Context mcontext;
	private ZFBPayResultCallBack mCallback;

	public PayMethod(Context mcontext) {
		this.mcontext = mcontext;
	}

	/**
	 *
	 * @param subject 订单的名称
	 * @param body 订单内容 传递时间戳
	 * @param price 订单金额
	 * @param Pay_OrderNo 订单编号
	 * @param notify_url 修改订单状态的url
     * @return
     */
	public String getOrderInfo(String subject, String body, String price, String Pay_OrderNo, String notify_url ) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + Key.PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + Key.SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + Pay_OrderNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + notify_url
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";



		return orderInfo;
	}
	
	
	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public static String sign(String content) {
		return SignUtils.sign(content, Key.RSA_PRIVATE);
	}
	
	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	
	/**
	 *  生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 *
	 * @param //  上下文对象 act
	 * @param
     */
	public  void  post_Pay(String subject, String body, String price, String Pay_OrderNo, String notify_url, ZFBPayResultCallBack callback){
		mCallback = callback;
		final String orderinfo=getOrderInfo(subject,body,price,Pay_OrderNo,notify_url);
		 Log.d("PayMethod", orderinfo);

		// 对订单做RSA 签名
		String sign = sign(orderinfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 Log.d("PayMethod", sign);
		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderinfo + "&sign=\"" + sign + "\"&"
				+ getSignType();
		Log.d("PayMethod", payInfo);
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity) mcontext);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo,true);
				Message msg= Message.obtain();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	@SuppressLint("HandlerLeak")
	public Handler mHandler=new Handler(){
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case SDK_PAY_FLAG:
					@SuppressWarnings("unchecked")
					PayResult payResult = new PayResult((Map<String, String>) msg.obj);
					// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息
					final String resultStatus = payResult.getResultStatus();
					mCallback.onRespResult(resultStatus);
					break;
			}
		}
	};


	public interface ZFBPayResultCallBack {
		//        void onSuccess(); //支付成功
		//        void onError(int error_code);//支付失败
		//        void onCancel();    //支付取消
		void  onRespResult(String resultStatus);
	}


	// 后台签名所有信息
	public void doPay(String payInfo, ZFBPayResultCallBack callback){
		mCallback = callback;
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity) mcontext);
				// 调用支付接口，获取支付结果
				Map<String, String> result = alipay.payV2(payInfo, true);
				Message msg= Message.obtain();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
				Log.d("PayMethod", result.toString());
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();

	}



}
