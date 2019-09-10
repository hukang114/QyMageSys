package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityWeeklySummWeeklyPlanBinding;
import com.qymage.sys.ui.adapter.WeekPalyListBeanaAdapter;
import com.qymage.sys.ui.entity.YesQueryEny;

import java.util.List;

/**
 * 周计划 周总结 显示
 */
public class WeeklySummWeeklyPlanActivity extends BBActivity<ActivityWeeklySummWeeklyPlanBinding> {


    List<YesQueryEny.WeekPalyListBean> listdata;
    private Intent mIntent;
    WeekPalyListBeanaAdapter adapter;
    private String type;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_weekly_summ_weekly_plan;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> {
            finish();
        });
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mIntent = getIntent();
        listdata = (List<YesQueryEny.WeekPalyListBean>) mIntent.getSerializableExtra("data");
        type = mIntent.getStringExtra("type");
        if (type.equals("2")) {
            mBinding.metitle.setcTxt(this.getResources().getString(R.string.m_week_summary_txt));
        }
        if (listdata == null) {
            return;
        }
        adapter = new WeekPalyListBeanaAdapter(R.layout.item_list_weekpalylist, listdata);
        mBinding.recyclerview.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        super.initData();
    }


}
