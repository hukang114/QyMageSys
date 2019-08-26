package com.qymage.sys.common.base.baseFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qymage.sys.BuildConfig;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.tools.ImageManager;
import com.qymage.sys.common.util.AppSignUtil;
import com.qymage.sys.common.util.SPUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.qymage.sys.common.widget.ProgressDialog;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;


public abstract class BaseFragment extends Fragment {

    protected DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    protected DateFormat DEFAULT_NYR = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public final String TAG = BaseFragment.class.getSimpleName();

    ImageManager imageManager;
    protected View mRootView;
    Locale locale;


    public FragmentActivity mContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageManager = new ImageManager(mContext);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            if (null == mRootView) {
                mRootView = inflater.inflate(getContentLayout(), container, false);
                baseInitView();
                baseInit();
            } else {
                ViewGroup parent = (ViewGroup) mRootView.getParent();
                if (null != parent) {
                    parent.removeView(mRootView);
                }
            }
        } catch (Exception e) {
        }

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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


    /**
     * 获取String 文字
     *
     * @param res_id
     * @return
     */
    public String getString_tx(int res_id) {
        if (getActivity() == null) {
            return "";
        } else {
            String name = getActivity().getResources().getString(res_id);
            return name;
        }

    }


    protected void baseInitView() {

    }

    protected void baseInit() {

    }

    public PostRequest postData(String url, Map<String, String> params) {

        if (getToken().equals("") || getToken() == null) {
            params.put("token", "null");
        } else {
            params.put("token", getToken());
        }
        params.put("sign", AppSignUtil.getgenAppSignValue(params));
        return OkGo.post(Constants.base_url + url)//
                .params(AppSignUtil.genAppSign(params))
                .tag(this);
    }


    public PostRequest postData(Map<String, String> params) {
        if (getToken() == null || getToken().equals("")) {
            params.put("token", "null");
        } else {
            params.put("token", getToken() + ",az," + BuildConfig.VERSION_NAME);
        }
        params.put("sign", AppSignUtil.getgenAppSignValue(params));
        return OkGo.post(Constants.base_url)//
                .params(AppSignUtil.genAppSign(params))
                .tag(this);
    }


    public String getLat() {
        String lat = (String) SPUtils.get(mContext, "lat", "");
        if (lat == null || lat.equals("")) {
            return "";
        } else {
            return lat;
        }
    }

    public String getLng() {
        String lng = (String) SPUtils.get(mContext, "lng", "");
        if (lng == null || lng.equals("")) {
            return "";
        } else {
            return lng;
        }
    }


    protected abstract int getContentLayout();


    protected void onBackClick() {

    }

    protected View getLoadingTargetView() {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(mContext, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivityForResult(intent, 200);
//        startActivity(intent);
    }

    public String getToken() {
        return (String) SPUtils.get(mContext, Constants.token, "");
    }

    public String getOpenId() {
        return (String) SPUtils.get(mContext, Constants.openid, "");
    }

    public String getGesture_Pwd() {
        return (String) SPUtils.get(mContext, getOpenId() + Constants.gesture_pwd, "");
    }

    public boolean IsLogin() {
        return !getToken().equals("");
    }

    /**
     * 没有登录启动登录页面
     *
     * @return
     */
    public boolean isLogin() {
        if (getToken().equals("") || getToken() == null) {
//            openActivity(LoginActivity.class);
            return false;
        } else {
            return true;
        }
    }

    private ProgressDialog mProgressDialog;

    public void showProgressDialog(String msg) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.show();
    }

    public void showProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private AlertDialog msgDialog;

    protected void msgDialog(String msg) {
        dismissMsgDialog();
        msgDialog = new AlertDialog.Builder(mContext).setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定", (dialog, which) -> {
                    dismissMsgDialog();
                })
                .create();
        msgDialog.show();
    }

    protected AlertDialog.Builder msgDialogBuilder(String msg, DialogInterface.OnClickListener lis) {
        dismissMsgDialog();
        return new AlertDialog.Builder(mContext).setTitle("温馨提示")
                .setPositiveButton("确定", lis)
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                .setMessage(msg);
    }

    protected AlertDialog.Builder msgDialog3Btn(String msg, DialogInterface.OnClickListener zfbtn, DialogInterface.OnClickListener cxbtn) {
        dismissMsgDialog();
        return new AlertDialog.Builder(mContext).setTitle("温馨提示")
                .setPositiveButton("支付", zfbtn)
                .setNegativeButton("撤销", cxbtn)
                .setNeutralButton("关闭", (dialogInterface, i) -> dialogInterface.dismiss())
                .setMessage(msg);
    }


    protected AlertDialog.Builder msgDialogBuilder1(String msg, DialogInterface.OnClickListener lis) {
        dismissMsgDialog();
        return new AlertDialog.Builder(mContext).setTitle("温馨提示")
                .setPositiveButton("确定", lis)
                .setMessage(msg);
    }

    protected AlertDialog.Builder msgNocanseDialogBuilder(String msg, DialogInterface.OnClickListener lis) {
        dismissMsgDialog();
        return new AlertDialog.Builder(mContext).setTitle("提示")
                .setPositiveButton("确定", lis)
                .setMessage(msg);
    }

    protected void dismissMsgDialog() {
        if (msgDialog != null) {
            msgDialog.dismiss();
            msgDialog = null;
        }
    }

    //========================================================================


    //======================================================================================

    // 图片加载相关
    public boolean canDisplay() {
        return true;
    }


}
