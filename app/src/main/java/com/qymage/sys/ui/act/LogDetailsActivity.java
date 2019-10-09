package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityLogDetailsBinding;
import com.qymage.sys.ui.entity.DayLogListEnt;
import com.qymage.sys.ui.entity.DayWeekMonthDet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志 提交中的明细记录
 */
public class LogDetailsActivity extends BBActivity<ActivityLogDetailsBinding> {


    List<DayWeekMonthDet.LogListBean> dayLogListEnts;
    LogDailyDetailsAdapter adapter;
    Bundle bundle;
    private Intent mIntent;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_log_details;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        dayLogListEnts = (List<DayWeekMonthDet.LogListBean>) mIntent.getSerializableExtra("data");
        if (dayLogListEnts == null) {
            return;
        }
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LogDailyDetailsAdapter(R.layout.item_history_log_daily_details, dayLogListEnts);
        mBinding.recyclerview.setAdapter(adapter);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.baoxiao_feiyong:
                    bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) dayLogListEnts.get(position).subMoneyList);
                    openActivity(LogHistoryReimbuActivity.class, bundle);
                    break;
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

    }

    /**
     * 合同明细适配器处理
     */
    public class LogDailyDetailsAdapter extends BaseQuickAdapter<DayWeekMonthDet.LogListBean, BaseViewHolder> {

        public LogDailyDetailsAdapter(int layoutResId, @Nullable List<DayWeekMonthDet.LogListBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DayWeekMonthDet.LogListBean item) {
            helper.setText(R.id.day_log_htbh_tv, item.contractNo)
                    .setText(R.id.hetong_mingchen, item.contractName)
                    .setText(R.id.xiangmu_bianhao, item.projectNo)
                    .setText(R.id.xiangmu_mingchen, item.projectName)
                    .setText(R.id.contracttype, item.contractTypeName)
                    .setText(R.id.gongshi_bili, item.manHours)
                    .setText(R.id.gongzuo_neirong, item.workContent)
                    .setText(R.id.baoxiao_feiyong, item.subMoney);
            helper.addOnClickListener(R.id.baoxiao_feiyong);
        }

    }
}
