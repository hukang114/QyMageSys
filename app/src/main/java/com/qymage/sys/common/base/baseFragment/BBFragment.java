package com.qymage.sys.common.base.baseFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.tools.CustomToast;
import com.qymage.sys.common.tools.ImageManager;
import com.qymage.sys.common.util.SPUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.qymage.sys.common.widget.ProgressDialog;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class BBFragment<VB extends ViewDataBinding> extends Fragment {

    public final String TAG = BBFragment.class.getSimpleName();


    protected VB mBinding;
    protected Context mContext;
    protected View mRootView;
    ImageManager ImageManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageManager=new ImageManager(getActivity());
    }


    protected String getUserId(){
        return (String) SPUtils.get(mContext, Constants.userid,"");

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        if (null == mRootView) {
            mBinding = DataBindingUtil.inflate(inflater,this.getContentLayout(), container, false);
            mRootView = mBinding.getRoot();
            baseInitView();
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        }
        return mRootView;
    }

    protected void baseInitView() {

    }



    public PostRequest postData(String url, Map<String, String> params){
        if (getToken().equals("")||getToken()==null){
        }else{
            params.put("token",getToken());
        }
        return OkGo.post(Constants.base_url+url)//
                .params(params)
                .tag(this);
    }



    protected abstract int getContentLayout();

    protected  void showToast(String msg){
        CustomToast.showToast(getActivity(),msg);
    }

    protected  void showToast(int msg){
        CustomToast.showToast(getActivity(),msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        startActivityForResult(intent,200);
//        startActivity(intent);
    }

    public String getToken(){
        return (String) SPUtils.get(mContext, Constants.token,"");
    }
    public String getOpenId(){
        return (String) SPUtils.get(mContext, Constants.openid,"");
    }
    public String getGesture_Pwd(){
        return (String) SPUtils.get(mContext, getOpenId()+Constants.gesture_pwd,"");
    }

    public boolean IsLogin(){
        return !getToken().equals("");
    }

    /**
     * 没有登录启动登录页面
     * @return
     */
    public boolean isLogin(Class tClass){
        if (getToken().equals("")||getToken()==null){
            openActivity(tClass);
            return false;
        }else{
            return true;
        }
    }



    private ProgressDialog mProgressDialog;

    public void showLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.show();
    }

    public void closeLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
    private AlertDialog msgDialog;
    protected void msgDialog(String msg) {
        dismissMsgDialog();
        msgDialog = new AlertDialog.Builder(mContext).setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定",(dialog, which) -> {
                    dismissMsgDialog();
                })
                .create();
        msgDialog.show();
    }
    protected AlertDialog.Builder msgDialogBuilder(String msg, DialogInterface.OnClickListener lis) {
        dismissMsgDialog();
        return new AlertDialog.Builder(mContext).setTitle("提示")
                .setPositiveButton("确定",lis)
                .setNegativeButton("取消",(dialogInterface, i) -> dialogInterface.dismiss())
                .setMessage(msg);
    }
    protected void dismissMsgDialog(){
        if (msgDialog != null) {
            msgDialog.dismiss();
            msgDialog = null;
        }
    }


}
