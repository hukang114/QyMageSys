package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityMonthReportBinding;

/**
 * 月报
 */
public class MonthReportActivity extends BBActivity<ActivityMonthReportBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_month_report;
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
