package com.qymage.sys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityApplicationCollectionBinding;

/**
 * 收款申请，付款申请 ，开票申请，收票申请
 */
public class ApplicationCollectionActivity extends BBActivity<ActivityApplicationCollectionBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_application_collection;
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
