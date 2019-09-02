package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityProjectApprovaApplylBinding;


/**
 * 立项申请
 */

public class ProjectApprovaApplylActivity extends BBActivity<ActivityProjectApprovaApplylBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_approva_applyl;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.metitle.setrTxtClick(v -> {
            openActivity(ProjectApprovaLoglActivity.class);
        });
    }

    @Override
    protected void initData() {
        super.initData();

    }
}

