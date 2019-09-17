package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.DatePicker;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivityReimbursementLogBinding;
import com.qymage.sys.ui.adapter.ProcessListAdapter;
import com.qymage.sys.ui.adapter.ReimbursementLogAdapter;
import com.qymage.sys.ui.entity.AskForLeaveEntity;
import com.qymage.sys.ui.entity.ReimbursementLogEnt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 我的报销记录
 */
public class ReimbursementLogActivity extends BBActivity<ActivityReimbursementLogBinding> implements View.OnClickListener {


    List<ReimbursementLogEnt> listdata = new ArrayList<>();
    ReimbursementLogAdapter adapter;
    private int page = 1;
    DateTime mothdateTime;
    DateTime endTime;
    private int date_typp = 1;
    private long startime = 0;
    private long endtime = 0;
    private Bundle bundle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_reimbursement_log;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.startDateTv.setOnClickListener(this);
        mBinding.endDateTv.setOnClickListener(this);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReimbursementLogAdapter(R.layout.item_reimbures_list, listdata);
        mBinding.recyclerview.setAdapter(adapter);
        mothdateTime = DateUtil.offsetMonth(new Date(), 0);
        mBinding.endDateTv.setText(DateUtil.format(mothdateTime, "yyyy-MM-dd"));
        endTime = DateUtil.offsetWeek(mothdateTime, -1);
        mBinding.startDateTv.setText(DateUtil.format(endTime, "yyyy-MM-dd"));
        mBinding.refreshlayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            getListData(Constants.RequestMode.FRIST);
        });
        mBinding.refreshlayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            getListData(Constants.RequestMode.LOAD_MORE);
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            bundle = new Bundle();
            bundle.putString("id", listdata.get(position).id);
            openActivity(ReimbursementLogDetActivity.class, bundle);
        });


    }

    /**
     * 获取数据
     *
     * @param mode
     */
    private void getListData(Constants.RequestMode mode) {
        showLoading();
        HttpUtil.expense_findByUser(getPer()).execute(new JsonCallback<Result<List<ReimbursementLogEnt>>>() {
            @Override
            public void onSuccess(Result<List<ReimbursementLogEnt>> result, Call call, Response response) {
                mBinding.emptylayout.showContent();
                mBinding.refreshlayout.finishRefresh(); // 刷新完成
                mBinding.refreshlayout.finishLoadMore();
                closeLoading();
                if (mode == Constants.RequestMode.FRIST) {
                    listdata.clear();
                    if (result.data != null && result.data.size() > 0) {
                        listdata.addAll(result.data);
                    } else {
                        mBinding.emptylayout.showEmpty();
                    }
                } else if (mode == Constants.RequestMode.LOAD_MORE) {
                    if (result.data != null && result.data.size() > 0) {
                        List<ReimbursementLogEnt> list = result.data;
                        listdata.addAll(list);
                    } else {
                        // 全部加载完成,没有数据了调用此方法
                        mBinding.refreshlayout.finishLoadMoreWithNoMoreData();
                        page--;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                mBinding.refreshlayout.finishRefresh(); // 刷新完成
                mBinding.refreshlayout.finishLoadMore();
                showToast(e.getMessage());
                mBinding.emptylayout.showError();
                mBinding.emptylayout.setRetryListener(() -> {
                    page = 1;
                    getListData(Constants.RequestMode.FRIST);
                });
            }
        });


    }

    private Map<String, Object> getPer() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("startDate", mBinding.startDateTv.getText().toString());
        hashMap.put("endDate", mBinding.endDateTv.getText().toString());
        hashMap.put("page", page + "");
        return hashMap;
    }

    @Override
    protected void initData() {
        super.initData();
        page = 1;
        getListData(Constants.RequestMode.FRIST);
    }

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_date_tv:
                date_typp = 1;
                showDateDialog();
                break;

            case R.id.date_select_img:
            case R.id.end_date_tv:
                date_typp = 2;
                showDateDialog();
                break;
        }
    }

    /**
     * 日期选择
     */
    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String desc = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
            int months = month + 1;
            String monStr = months + "";
            int dayOfMonths = dayOfMonth;
            String dayOfMontStr = dayOfMonth + "";
            if (month + 1 < 10) {
                monStr = "0" + months;
            }
            if (dayOfMonths < 10) {
                dayOfMontStr = "0" + dayOfMonths;
            }
            if (date_typp == 1) {
                startime = Long.parseLong(year + "" + (month + 1) + dayOfMonth);
                mBinding.startDateTv.setText(year + "-" + monStr + "-" + dayOfMontStr);
                page = 1;
                getListData(Constants.RequestMode.FRIST);
            } else if (date_typp == 2) {
                endtime = Long.parseLong(year + "" + (month + 1) + dayOfMonth);
                if (endtime < startime) {
                    showToast("结束时间不能小于开始时间");
                } else {
                    mBinding.endDateTv.setText(year + "-" + monStr + "-" + dayOfMontStr);
                    page = 1;
                    getListData(Constants.RequestMode.FRIST);
                }
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }
}
