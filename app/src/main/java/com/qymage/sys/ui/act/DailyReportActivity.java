package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityDailyReportBinding;


/**
 * 日报
 */
public class DailyReportActivity extends BBActivity<ActivityDailyReportBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_daily_report;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
    }
}
