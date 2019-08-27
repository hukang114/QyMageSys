package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityMyAttendanceBinding;

/**
 * 我的考勤
 */
public class MyAttendanceActivity extends BBActivity<ActivityMyAttendanceBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_attendance;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
    }
}
