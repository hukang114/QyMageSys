package com.qymage.sys.common.base.baseFragment;

/**
 * Created by HK on 2018/4/10.
 */

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
import com.qymage.sys.common.tools.ImageManager;
import com.qymage.sys.common.tools.ToastUtil;
import com.qymage.sys.common.util.AppSignUtil;
import com.qymage.sys.common.util.SPUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.qymage.sys.common.widget.ProgressDialog;

import java.lang.reflect.Field;
import java.util.Map;

/**
 *
 * 若需要采用Lazy方式加载的Fragment，初始化内容放到initData实现
 * 若不需要Lazy加载则initData方法内留空,初始化内容放到initViews即可
 *
 * 注意事项 1:
 * 如果是与ViewPager一起使用，调用的是setUserVisibleHint。
 *
 * 注意事项 2:
 * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
 * 针对初始就show的Fragment 为了触发onHiddenChanged事件 达到lazy效果 需要先hide再show
 *
 * @author vondear
 * @date 2015/11/21.
 */
public abstract class FragmentLazy <VB extends ViewDataBinding> extends Fragment {

    /**
     * 是否可见状态
     */
    private boolean isVisible;

    /**
     * 标志位，View已经初始化完成。
     */
    private boolean isPrepared;

    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;


    public final String TAG = BBFragment.class.getSimpleName();


    protected VB mBinding;
    protected Context mContext;
    protected View mRootView;

    ImageManager mManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        mManager =new ImageManager(mContext);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        // 若 viewpager 不设置 setOffscreenPageLimit 或设置数量不够
        // 销毁的Fragment onCreateView 每次都会执行(但实体类没有从内存销毁)
        // 导致initData反复执行,所以这里注释掉
        // isFirstLoad = true;
        // 取消 isFirstLoad = true的注释 , 因为上述的initData本身就是应该执行的
        // onCreateView执行 证明被移出过FragmentManager initData确实要执行.
        // 如果这里有数据累加的Bug 请在initViews方法里初始化您的数据 比如 list.clear();
        if (mRootView==null){
            mContext = getActivity();
            isFirstLoad = true;
            mBinding = DataBindingUtil.inflate(inflater,this.getContentLayout(), container, false);
            mRootView = mBinding.getRoot();
            baseInit();
            isPrepared = true;
            lazyLoad();
        }else{
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        }
        return mRootView;
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     *               visible.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {

    }

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        initData();
    }

    protected abstract int getContentLayout();

    protected void baseInit() {

    }

    protected abstract void initData();



    public PostRequest postData(String url, Map<String, String> params){
        if (getToken().equals("")||getToken()==null){
        }else{
            params.put("token",getToken());
        }
        params.put("sign", AppSignUtil.getgenAppSignValue(params));
        return OkGo.post(Constants.base_url+url)//
                .params(params)
                .tag(this);
    }


    public PostRequest postData(Map<String, String> params){
        if (getToken()==null||getToken().equals("")){
            params.put("token","null");
        }else{
            params.put("token",getToken());
        }
        params.put("sign", AppSignUtil.getgenAppSignValue(params));
        return OkGo.post(Constants.base_url)//
                .params(AppSignUtil.genAppSign(params))
                .tag(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    protected void showToast(String msg) {
        ToastUtil.showToast(mContext, msg);
    }

    protected void showToast(int resId) {
        ToastUtil.showToast(mContext, resId);
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

    protected String getUserId() {
        return (String) SPUtils.get(mContext, Constants.userid, "");
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

    //======================================================================================

}