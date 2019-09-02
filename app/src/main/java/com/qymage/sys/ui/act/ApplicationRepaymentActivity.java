package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityApplicationRepaymentBinding;

/**
 * 还款申请
 */
public class ApplicationRepaymentActivity extends BBActivity<ActivityApplicationRepaymentBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_application_repayment;
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
