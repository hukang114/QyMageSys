package com.qymage.sys.common.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.gyf.barlibrary.ImmersionBar;
import com.qymage.sys.R;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.tools.ImageManager;
import com.qymage.sys.common.tools.ToastUtil;
import com.qymage.sys.common.tools.Tools;
import com.qymage.sys.common.util.AppSignUtil;
import com.qymage.sys.common.util.SPUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.qymage.sys.common.widget.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;


public abstract class BaseActivity extends AppCompatActivity {
    public FragmentManager fm;

    Locale locale;
    protected BaseActivity mActivity;

    protected DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    protected DateFormat DEFAULT_NYR = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static Handler mHandler = null;
    private boolean isDispatchTouchEvent = true;
    ImageManager imageManager;

    class myHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 404) {
                if (getToken() != null && !getToken().equals("")) {
                    if (!isFinishing()) {
                        Log.d("myHandler", "被执行");
                    }
                }
            }
        }
    }

    protected void showToast(String msg) {
        ToastUtil.showToast(this, msg);
    }

    protected void showToast(int resId) {
        ToastUtil.showToast(this, resId);
    }

    /**
     * 初始化 ImmersionBar
     */
    protected void initImmersionBar() {
        // 在BaseActivity里初始化
        // 使用该属性,必须指定状态栏颜色
        //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true, 0.2f).statusBarColor(R.color.app_color).init();
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        // 初始化沉浸式
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        imageManager = new ImageManager(this);
        mActivity = this;
    }

    //注册EventBus 必须要重写他的Subscribe
    @Subscribe
    public void publicMethod(String str) {

    }

    public PostRequest postData(String url, Map<String, String> params) {
        if (getToken().equals("") || getToken() == null) {
            params.put("token", "null");
        } else {
            params.put("token", getToken());
        }
        params.put("nonce", AppSignUtil.randomChar());
        params.put("timestamp", System.currentTimeMillis() + "");

        params.put("sign", AppSignUtil.getgenAppSignValue(params));
        return OkGo.post(Constants.base_url + url)//
                .params(AppSignUtil.genAppSign(params))
                .tag(this);
    }

    public PostRequest postData(Map<String, String> params) {
        if (getToken().equals("") || getToken() == null) {
            params.put("token", "null");
        } else {
            params.put("token", getToken());//
        }
        params.put("sign", AppSignUtil.getgenAppSignValue(params));
        return OkGo.post(Constants.base_url)//
                .params(AppSignUtil.genAppSign(params))
                .tag(this);

    }


    protected void onFinish() {
    }

    //获取 Locale 对象的正确姿势：
    public boolean isen_US() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        //获取语言的正确姿势：
        String lang = locale.getLanguage() + "-" + locale.getCountry();
        if (lang.contains("en-US")) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mHandler = new myHandler();
        setStatusBar();
    }

    /**
     * 获取String 文字
     *
     * @param res_id
     * @return
     */
    public String getString_tx(int res_id) {
        if (!BaseActivity.this.isFinishing()) {
            String name = this.getResources().getString(res_id);
            return name;
        } else {
            return "";
        }
    }

    protected void setStatusBar() {

    }


    /**
     * 没有登录启动登录页面
     *
     * @return
     */
    public boolean isLogin(Class aClass) {
        if (getUserId() == null || getUserId().equals("")) {
            openActivity(aClass);
            return false;
        } else {
            return true;
        }
    }

    protected String getUserId() {
        return (String) SPUtils.get(mActivity, Constants.userid, "");
    }

    /**
     * 跳转Activity
     *
     * @param activityClass 目标Activity类名称
     * @param bundle        Bundle 对象
     */
    public void intoActivity(Class<?> activityClass, Bundle bundle) {
        Intent into = new Intent(this, activityClass);
        if (bundle != null) {
            into.putExtra("bundle", bundle);
        }
        startActivity(into);
    }

    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(BaseActivity.this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivityForResult(intent, 200);
//        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && isDispatchTouchEvent) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否应隐藏键盘
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 设置发送触摸事件
     *
     * @param dispatchTouchEvent
     */
    public void setDispatchTouchEvent(boolean dispatchTouchEvent) {
        isDispatchTouchEvent = dispatchTouchEvent;
    }


    public void snackBar(View v, String msg) {
        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
    }


    private ProgressDialog mProgressDialog;

    public void showLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.show();
    }

    public void closeLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

    }

    public void switchFadeContent(Fragment from, Fragment to, int layout) {
        FragmentTransaction transaction = fm.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (!to.isAdded()) { // 先判断是否被add过
            transaction.hide(from).add(layout, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
//			from.onDestroy();
        }
    }


    private AlertDialog msgDialog;

    protected void msgDialog(String msg) {
        dismissMsgDialog();
        msgDialog = new AlertDialog.Builder(this).setTitle("提示!")
                .setMessage(msg)
                .setPositiveButton("确定", (dialog, which) -> {
                    dismissMsgDialog();
                })
                .create();
        msgDialog.show();
    }

    protected AlertDialog.Builder msgDialogBuilder(String msg) {
        dismissMsgDialog();
        return new AlertDialog.Builder(this).setTitle("提示!")
                .setMessage(msg);
    }

    protected AlertDialog.Builder msgDialogBuilder(String msg, DialogInterface.OnClickListener lis) {
        dismissMsgDialog();
        return new AlertDialog.Builder(this).setTitle("提示!")
                .setPositiveButton("确定", lis)
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                .setMessage(msg);
    }

    protected AlertDialog.Builder msgDialogBuilderPay(String msg, DialogInterface.OnClickListener lis) {
        dismissMsgDialog();
        return new AlertDialog.Builder(this).setTitle("订单提示!")
                .setPositiveButton("决然离去", lis)
                .setNegativeButton("再想想", (dialogInterface, i) -> dialogInterface.dismiss())
                .setMessage(msg);
    }

    protected AlertDialog.Builder msgNocanseDialogBuilder(String msg, DialogInterface.OnClickListener lis) {
        dismissMsgDialog();
        return new AlertDialog.Builder(this).setTitle("提示!")
                .setPositiveButton("确定", lis)
                .setMessage(msg);
    }

    protected void dismissMsgDialog() {
        if (msgDialog != null) {
            msgDialog.dismiss();
            msgDialog = null;
        }
    }

    public String getToken() {
        return (String) SPUtils.get(this, Constants.token, "");
    }

    public String getUID() {
        return (String) SPUtils.get(this, Constants.userid, "");
    }

    public String getOpenId() {
        return (String) SPUtils.get(this, Constants.openid, "");
    }

    public boolean IsLogin() {
        return !getToken().equals("");
    }


    //========================================================================


    //======================================================================================


    /**
     * 禁止EditText输入空格
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
        OkGo.getInstance().cancelTag(this);
        if (isImmersionBarEnabled()) {
            ImmersionBar.with(this).destroy();
        }
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
        mActivity = null;
    }

    /**
     * 重写 getResource 方法，防止系统字体影响
     * 禁止app字体大小跟随系统字体大小调节
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


}
