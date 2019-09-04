package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityBiddingMarginZhiBinding;

/**
 * 投标保证金支出
 */
public class BiddingMarginZhiActivity extends BBActivity<ActivityBiddingMarginZhiBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bidding_margin_zhi;
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

