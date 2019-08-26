package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.BuildConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityAboutBinding;

/**
 * 关于我们
 */
public class AboutActivity extends BBActivity<ActivityAboutBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.versionTv.setText("当前版本:V" + BuildConfig.VERSION_NAME);
    }


    @Override
    protected void initData() {
        super.initData();

    }


}
