package com.qymage.sys.common.tools;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qymage.sys.R;


/**
 * 作者：hk on 2016/8/8 15:04
 * 邮箱：123456789@163.com
 * Version 3.0.0
 * 类说明： 自定义
 */
public class CustomToast {
    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context mContext, String text) {
        mHandler.removeCallbacks(r);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.toast_view, null);
        TextView tv= (TextView) view.findViewById(R.id.chaptername);
        if (mToast != null)
            tv.setText(text);
        else
//            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mToast=new Toast(mContext);
        tv.setText(text);
        mToast.setView(view);
        mToast.setGravity(Gravity.BOTTOM, 0,Tools.getScreenHeight(mContext)*1/7);
        mHandler.postDelayed(r, 2000);
        mToast.show();
    }

    public static void showToast(Context mContext, int resId) {
        showToast(mContext, mContext.getResources().getString(resId));
    }



}
