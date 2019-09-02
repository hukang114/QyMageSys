package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityLoanApplicationBinding;


/**
 * 借款申请
 */
public class LoanApplicationActivity extends BBActivity<ActivityLoanApplicationBinding> {



    @Override
    protected int getLayoutId() {
        return R.layout.activity_loan_application;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());

    }

    @Override
    protected void initData() {
        super.initData();

    }

}


