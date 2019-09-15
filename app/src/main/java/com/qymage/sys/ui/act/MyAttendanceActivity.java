package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityMyAttendanceBinding;

/**
 * 我的考勤
 */
public class MyAttendanceActivity extends BBActivity<ActivityMyAttendanceBinding> implements View.OnClickListener {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_attendance;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.rankingNumTv.setOnClickListener(this);
        mBinding.kaoqingDateTv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ranking_num_tv:
                openActivity(RankingActivity.class);

                break;
            case R.id.kaoqing_date_tv:

                break;
        }

    }
}
