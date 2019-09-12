package com.qymage.sys.common.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;


import com.qymage.sys.AppConfig;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.tools.Tools;
import com.qymage.sys.common.util.AppManager;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;


public abstract class BBActivity<VB extends ViewDataBinding> extends BaseActivity {
    protected VB mBinding;

    protected int getHight;
    protected int getWidth;

    protected DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    protected DateFormat DEFAULT_NYR = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    protected DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        AppManager.getInstance().addActivity(this); //添加到栈中
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), this.getLayoutId(), null, false);
        super.setContentView(mBinding.getRoot());
        setStatusBar();
        initView();
        initData();
    }

    protected void setStatusBar() {

    }

    protected void initView() {
        getWidth = Tools.getScreenWidth(this);
        getHight = Tools.getScreenHeight(this);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected void initData() {
    }


    /**
     * @param type     1 同意 2  拒绝 3  审批
     * @param modeType 审批的模块的类型
     * @param item     审批需的参数对象
     */
    protected void auditAdd(String type, int modeType, ProjectAppLogEnt item) {
        if (type.equals("3")) {
            msgDialogBuilder("确认撤销？", (dialog, which) -> {
                dialog.dismiss();
                subMitAudit(type, modeType, item);
            }).create().show();
        } else if (type.equals("2")) {
            msgDialogBuilder("拒绝审批？", (dialog, which) -> {
                dialog.dismiss();
                subMitAudit(type, modeType, item);
            }).create().show();
        } else if (type.equals("1")) {
            msgDialogBuilder("同意审批？", (dialog, which) -> {
                dialog.dismiss();
                subMitAudit(type, modeType, item);
            }).create().show();
        }

    }

    /**
     * @param type     1 同意 2  拒绝 3  审批
     * @param modeType 审批的模块的类型
     * @param item     审批需的参数对象
     */
    private void subMitAudit(String type, int modeType, ProjectAppLogEnt item) {
        showLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Id", item.id);
        hashMap.put("remarks", "");
        hashMap.put("type", type);
        hashMap.put("processInstanceId", item.processInstId);
        hashMap.put("modeType", modeType);
        HttpUtil.audit_auditAdd(hashMap).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgNocanseDialogBuilder(result.message, (dialog, which) -> {
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


}
