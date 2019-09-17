package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivityReimbursementLogDetBinding;
import com.qymage.sys.ui.entity.ASkForDetEnt;
import com.qymage.sys.ui.entity.ReimbursementLogDetEnt;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 报销详情
 */
public class ReimbursementLogDetActivity extends BBActivity<ActivityReimbursementLogDetBinding> {


    private String id;// 列表id
    private Intent mIntent;
    Bundle bundle;
    ReimbursementLogDetEnt item;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_reimbursement_log_det;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        id = mIntent.getStringExtra("id");
        if (id == null) {
            return;
        }

    }

    @Override
    protected void initData() {
        super.initData();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        showLoading();
        HttpUtil.baoxiao_findById(hashMap).execute(new JsonCallback<Result<ReimbursementLogDetEnt>>() {
            @Override
            public void onSuccess(Result<ReimbursementLogDetEnt> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    item = result.data;
                    setDataShow();
                } else {
                    return;
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                showToast(e.getMessage());
            }
        });

    }

    private void setDataShow() {
        if (item.reimburserName != null) {
            if (item.reimburserName.length() >= 3) {
                String strh = item.reimburserName.substring(item.reimburserName.length() - 2, item.reimburserName.length());   //截取
                mBinding.nameTvBg.setText(strh);
            } else {
                mBinding.nameTvBg.setText(item.reimburserName);
            }
        }
        mBinding.userName.setText(item.reimburserName + "报销申请");
        mBinding.actstatusTv.setText(item.actStatus);
        mBinding.szbmType.setText("报销类型：" + item.amountType);
        mBinding.projType.setText("报销人：" + item.reimburserName);
        mBinding.projNumber.setText("部门名称：" + item.amountType);
        mBinding.projName.setText("项目编号：" + item.projectNo);
        mBinding.persionName.setText("项目名称：" + item.projectName);
        mBinding.appalyContentTv.setText("合同编号：" + item.contractNo);
        mBinding.dateTimeTv.setText("报销金额：" + item.amount);
        mBinding.introductionTv.setText("报销日期：" + item.date);
        mBinding.remark.setText("备注：" + item.remark);

    }
}

