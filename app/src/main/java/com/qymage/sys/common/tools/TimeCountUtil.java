package com.qymage.sys.common.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
/**
 * 
 * @author 胡康
 *  验证码按钮的倒计时控制
 *
 */
public class TimeCountUtil extends CountDownTimer {
	
	private Activity mActivity;
	private TextView btn;//按钮
	// 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个是，就把这个按钮传过来就可以了
	public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, TextView btn) {
	super(millisInFuture, countDownInterval);
	this.mActivity = mActivity;
	this.btn =btn;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		try {

			btn.setClickable(false);//设置不能点击
			btn.setText(millisUntilFinished / 1000 + "秒后可重发");//设置倒计时时间

			//设置按钮为灰色，这时是不能点击的
		//	btn.setBackground(mActivity.getResources().getDrawable(R.drawable.msg_sendselector));
//			btn.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.selector_login_btn));
			Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字

			if((millisUntilFinished / 1000) < 10){
			span.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
			}else if((millisUntilFinished / 1000) < 100){
				span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
			}else {
				span.setSpan(new ForegroundColorSpan(Color.RED), 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
			}
			
			btn.setText(span);
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("TimeCountUtil", e.toString());
		}
	}
	

	public void onFinish() {
		btn.setText("获取验证码");
		btn.setClickable(true);//重新获得点击
//		btn.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.selector_login_btn));
		
	}
	
}
