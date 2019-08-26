package com.qymage.sys.common.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;


import com.qymage.sys.common.tools.Tools;
import com.qymage.sys.common.util.AppManager;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


public abstract class BBActivity<VB extends ViewDataBinding> extends BaseActivity {
    protected VB mBinding;

    protected  int getHight;
    protected int getWidth;

    protected DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    protected  DateFormat DEFAULT_NYR = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    protected  DecimalFormat df = new DecimalFormat("0.00");
    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        AppManager.getInstance().addActivity(this); //添加到栈中
        mBinding = DataBindingUtil.inflate(getLayoutInflater(),this.getLayoutId(), null, false);
        super.setContentView(mBinding.getRoot());
        setStatusBar();
        initView();
        initData();
    }
    protected void setStatusBar() {


    }
    protected void initView() {
        getWidth = Tools.getScreenWidth(this);
        getHight=Tools.getScreenHeight(this);
    }
    @LayoutRes
    protected abstract int getLayoutId();

    protected void  initData(){};


//    /**
//     * 显示提示框
//     *
//     * @param str 内容
//     */
//    protected void showHintDialog(String str) {
//        final XAlertDialog xfAlertDialog = XAlertDialog.getInstance(this);
//        xfAlertDialog.setOnCancelButton("确定", new XAlertDialog.onButtonListener() {
//            @Override
//            public void OnClick() {
//                xfAlertDialog.dismiss();
//            }
//        });
//        xfAlertDialog.showDialog("温馨提示", str, false);
//    }




}
