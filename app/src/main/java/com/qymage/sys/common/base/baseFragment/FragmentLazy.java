package com.qymage.sys.common.base.baseFragment;

/**
 * Created by HK on 2018/4/10.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.tools.ImageManager;
import com.qymage.sys.common.tools.ToastUtil;
import com.qymage.sys.common.util.AppSignUtil;
import com.qymage.sys.common.util.SPUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.qymage.sys.common.widget.ProgressDialog;
import com.qymage.sys.ui.act.MyLoanActivity;
import com.qymage.sys.ui.adapter.LeaveTypeAdapter;
import com.qymage.sys.ui.entity.LeaveType;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.qymage.sys.ui.fragment.NewsFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 若需要采用Lazy方式加载的Fragment，初始化内容放到initData实现
 * 若不需要Lazy加载则initData方法内留空,初始化内容放到initViews即可
 * <p>
 * 注意事项 1:
 * 如果是与ViewPager一起使用，调用的是setUserVisibleHint。
 * <p>
 * 注意事项 2:
 * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
 * 针对初始就show的Fragment 为了触发onHiddenChanged事件 达到lazy效果 需要先hide再show
 *
 * @author vondear
 * @date 2015/11/21.
 */
public abstract class FragmentLazy<VB extends ViewDataBinding> extends Fragment {


    List<LeaveType> leaveTypes = new ArrayList<>();
    LeaveTypeAdapter typeAdapter;


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
        mManager = new ImageManager(mContext);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 若 viewpager 不设置 setOffscreenPageLimit 或设置数量不够
        // 销毁的Fragment onCreateView 每次都会执行(但实体类没有从内存销毁)
        // 导致initData反复执行,所以这里注释掉
        // isFirstLoad = true;
        // 取消 isFirstLoad = true的注释 , 因为上述的initData本身就是应该执行的
        // onCreateView执行 证明被移出过FragmentManager initData确实要执行.
        // 如果这里有数据累加的Bug 请在initViews方法里初始化您的数据 比如 list.clear();
        if (mRootView == null) {
            mContext = getActivity();
            isFirstLoad = true;
            mBinding = DataBindingUtil.inflate(inflater, this.getContentLayout(), container, false);
            mRootView = mBinding.getRoot();
            baseInit();
            isPrepared = true;
            lazyLoad();
        } else {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (null != parent) {
                parent.removeView(mRootView);
            }
        }
        return mRootView;
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


    public PostRequest postData(String url, Map<String, String> params) {
        if (getToken().equals("") || getToken() == null) {
        } else {
            params.put("token", getToken());
        }
        params.put("sign", AppSignUtil.getgenAppSignValue(params));
        return OkGo.post(Constants.base_url + url)//
                .params(params)
                .tag(this);
    }


    public PostRequest postData(Map<String, String> params) {
        if (getToken() == null || getToken().equals("")) {
            params.put("token", "null");
        } else {
            params.put("token", getToken());
        }
        params.put("sign", AppSignUtil.getgenAppSignValue(params));
        return OkGo.post(Constants.base_url)//
                .params(AppSignUtil.genAppSign(params))
                .tag(this);
    }


    protected void showToast(String msg) {
        ToastUtil.showToast(mContext, msg);
    }

    protected void showToast(int resId) {
        ToastUtil.showToast(mContext, resId);
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

    protected String getUserId() {
        return (String) SPUtils.get(mContext, Constants.userid, "");
    }

    public boolean IsLogin() {
        return !getToken().equals("");
    }

    /**
     * 没有登录启动登录页面
     *
     * @return
     */
    public boolean isLogin(Class tClass) {
        if (getToken().equals("") || getToken() == null) {
            openActivity(tClass);
            return false;
        } else {
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
                .setPositiveButton("确定", (dialog, which) -> {
                    dismissMsgDialog();
                })
                .create();
        msgDialog.show();
    }

    protected AlertDialog.Builder msgDialogBuilder(String msg, DialogInterface.OnClickListener lis) {
        dismissMsgDialog();
        return new AlertDialog.Builder(mContext).setTitle("提示")
                .setPositiveButton("确定", lis)
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                .setMessage(msg);
    }

    protected void dismissMsgDialog() {
        if (msgDialog != null) {
            msgDialog.dismiss();
            msgDialog = null;
        }
    }


    /**
     * 月报打分页面需要提前调用改方法，的时候需要先获取领导打分数据
     */
    protected void getleadType() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "leadType");
        HttpUtil.getEnum(hashMap).execute(new JsonCallback<Result<List<LeaveType>>>() {
            @Override
            public void onSuccess(Result<List<LeaveType>> result, Call call, Response response) {
                if (result.data != null && result.data.size() > 0) {
                    leaveTypes.clear();
                    leaveTypes.addAll(result.data);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });

    }

    //======================================================================================


    private void showCommentsDialog(String type, int modeType, ProjectAppLogEnt item) {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(getActivity(), R.layout.dialog_remarks, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        EditText editText = dialog.findViewById(R.id.notes_edt);
        EditText comments_edt = dialog.findViewById(R.id.comments_edt);
        TextView df_tv = dialog.findViewById(R.id.df_tv);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerview);
        LinearLayoutManager layouta = new LinearLayoutManager(getActivity());
        layouta.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        recyclerView.setLayoutManager(layouta);
        if (modeType == AppConfig.status.value14 && type.equals("1")) { // 月报提交需要打分和提交评语 同意 的时候执行
            comments_edt.setVisibility(View.VISIBLE);
            df_tv.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            if (leaveTypes != null && leaveTypes.size() > 0) {
                typeAdapter = new LeaveTypeAdapter(R.layout.item_list_leavetype, leaveTypes);
                recyclerView.setAdapter(typeAdapter);
                typeAdapter.setOnItemChildClickListener((adapter, view12, position) -> {
                    switch (view12.getId()) {
                        case R.id.frg_selc_all:
                            for (int i = 0; i < leaveTypes.size(); i++) {
                                if (i == position) {
                                    if (leaveTypes.get(position).isCheck) {
                                        leaveTypes.get(position).isCheck = true;
                                    } else {
                                        leaveTypes.get(position).isCheck = true;
                                    }
                                } else {
                                    leaveTypes.get(i).isCheck = false;
                                }
                            }
                            typeAdapter.notifyDataSetChanged();
                            break;
                    }
                });
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            comments_edt.setVisibility(View.GONE);
            df_tv.setVisibility(View.GONE);
        }
        TextView submit_btn = dialog.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(v -> {
            if (type.equals("2")) { // 拒绝审批
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    showToast("请填写拒绝的原因");
                } else {
                    dialog.dismiss();
                    subMitAudit(type,
                            modeType,
                            item,
                            editText.getText().toString(),
                            comments_edt.getText().toString(),
                            getSelectVal(),
                            MyLoanActivity.assignees);
                }
            } else {
                dialog.dismiss();
                subMitAudit(type,
                        modeType,
                        item,
                        editText.getText().toString(),
                        comments_edt.getText().toString(),
                        getSelectVal(),
                        MyLoanActivity.assignees);
            }
        });

    }

    /**
     * 获取选择的打分值
     *
     * @return
     */
    private String getSelectVal() {
        String val = "";
        for (int i = 0; i < leaveTypes.size(); i++) {
            if (leaveTypes.get(i).isCheck) {
                val = leaveTypes.get(i).value;
            }
        }
        return val;
    }


    // 审批的同意处理

    /**
     * @param type     1 同意 2  拒绝 3  撤销
     * @param modeType 审批的模块的类型
     * @param item     审批需的参数对象
     */
    protected void auditAdd(String type, int modeType, ProjectAppLogEnt item) {
        if (type.equals("3")) {
            msgDialogBuilder("确认撤销？", (dialog, which) -> {
                dialog.dismiss();
                showCommentsDialog(type, modeType, item);
            }).create().show();
        } else if (type.equals("2")) {
            msgDialogBuilder("拒绝审批？", (dialog, which) -> {
                dialog.dismiss();
                showCommentsDialog(type, modeType, item);
            }).create().show();
        } else if (type.equals("1")) {
            msgDialogBuilder("同意审批？", (dialog, which) -> {
                dialog.dismiss();
                showCommentsDialog(type, modeType, item);
            }).create().show();
        }
    }


    private void WhetherScoring(String type, int modeType, ProjectAppLogEnt item) {


    }


    /**
     * @param type     1 同意 2  拒绝 3  审批
     * @param modeType 审批的模块的类型
     * @param item     审批需的参数对象
     *                 id  String  id
     *                 examType：类型  1—通过   2-拒绝 3-撤回
     *                 remarks String  备注
     *                 processInstanceId String 流程实例ID
     *                 modeType String  模块id
     *                 assignees String  借款接口(传借款人id)
     *                 leadComment String 领导评语（月报）
     *                 leadGrade string  领导打分（月报）
     */
    private void subMitAudit(String type, int modeType, ProjectAppLogEnt item, String remarks, String leadComment, String leadGrade, String assignees) {
        showLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", item.id);
        hashMap.put("remarks", "");
        hashMap.put("type", type);
        hashMap.put("processInstanceId", item.processInstId);
        hashMap.put("modeType", modeType);
        hashMap.put("assignees", assignees);
        hashMap.put("remarks", remarks);
        hashMap.put("leadComment", leadComment);
        hashMap.put("leadGrade", leadGrade);
        HttpUtil.audit_auditAdd(hashMap).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgDialogBuilder(result.message, (dialog, which) -> {
                    dialog.dismiss();
                    successTreatment();
                }).setCancelable(false).create().show();
                successTreatment();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 审批功的处理
     */
    protected void successTreatment() {

    }


    /**
     * 更新消息
     *
     * @param msgId
     */
    protected void msgUdate(String msgId, int position) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("msgId", msgId);
        HttpUtil.msgUdate(hashMap).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                msgUpdateSuccess(position);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }

    /**
     * 更新消息成的处理
     */
    protected void msgUpdateSuccess(int position) {
        if (NewsFragment.mHander != null) {
            Message message = NewsFragment.mHander.obtainMessage();
            message.what = 100;
            NewsFragment.mHander.sendMessage(message);
        }

    }


}