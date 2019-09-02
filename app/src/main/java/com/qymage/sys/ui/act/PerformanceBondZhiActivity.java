package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityPerformanceBondZhiBinding;


/**
 * 履约保证金的支
 */
public class PerformanceBondZhiActivity extends BBActivity<ActivityPerformanceBondZhiBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_performance_bond_zhi;
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
