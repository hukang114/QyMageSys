package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityPerformanceBondShouBinding;


/**
 * 履约保证金收
 */
public class PerformanceBondShouActivity extends BBActivity<ActivityPerformanceBondShouBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_performance_bond_shou;
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
