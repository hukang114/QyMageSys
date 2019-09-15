package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityReimbursementLogBinding;


/**
 * 我的报销记录
 */
public class ReimbursementLogActivity extends BBActivity<ActivityReimbursementLogBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_reimbursement_log;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());

    }

    @Override
    protected void initData() {
        super.initData();

    }
}
