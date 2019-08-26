package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityOpenAfterWorkBinding;


/**
 * 上下班打卡
 */
public class OpenAfterWorkActivity extends BBActivity<ActivityOpenAfterWorkBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_after_work;
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
