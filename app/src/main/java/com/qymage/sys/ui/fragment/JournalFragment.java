package com.qymage.sys.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.baseFragment.FragmentLazy;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.FragmentJournalBinding;
import com.qymage.sys.ui.act.ASkForDetailsActivity;
import com.qymage.sys.ui.act.MonthlyDetailsActivity;
import com.qymage.sys.ui.adapter.JournalAdapter;
import com.qymage.sys.ui.entity.JournalEntity;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A 日志
 */
public class JournalFragment extends FragmentLazy<FragmentJournalBinding> implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    private int date_typp = 1;
    private long startime = 0;
    private long endtime = 0;
    public static int workType = 1;
    public static int logType = 1;// logType String  1-待处理 2-已处理 3-抄送我  4-已提交

    List<JournalEntity> journalEntities = new ArrayList<>();
    JournalAdapter adapter;
    private int page = 1;
    private Bundle bundle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public static JournalFragment newInstance() {
        JournalFragment fragment = new JournalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_journal;
    }


    @Override
    protected void baseInit() {
        super.baseInit();
        mBinding.startDateTv.setOnClickListener(this);
        mBinding.endDateTv.setOnClickListener(this);
        mBinding.dateSelectImg.setOnClickListener(this);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new JournalAdapter(R.layout.item_list_journal, journalEntities);
        mBinding.recyclerview.setAdapter(adapter);
        mBinding.refreshlayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            getAllTypeData(Constants.RequestMode.FRIST);
        });
        mBinding.refreshlayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            getAllTypeData(Constants.RequestMode.LOAD_MORE);
        });
        mBinding.workGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.daily_btn:// 日报
                    workType = 1;
                    mBinding.radioGroup.setVisibility(View.VISIBLE);
                    break;
                case R.id.weekly_btn:// 周报
                    workType = 2;
                    mBinding.radioGroup.setVisibility(View.GONE);
                    break;
                case R.id.monthly_btn:// 月报
                    workType = 3;
                    mBinding.radioGroup.setVisibility(View.VISIBLE);
                    break;
            }
            page = 1;
            getAllTypeData(Constants.RequestMode.FRIST);
        });
        mBinding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.pending_btn: // 待处理
                    logType = 1;
                    break;
                case R.id.processed_btn:// 已处理
                    logType = 2;
                    break;
                case R.id.copy_to_me_btn:// 抄送给我
                    logType = 3;
                    break;
                case R.id.yitijioa_btn:// 已提交
                    logType = 4;
                    break;
            }
            page = 1;
            getAllTypeData(Constants.RequestMode.FRIST);
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            ProjectAppLogEnt item = new ProjectAppLogEnt();
            int state = 0;
            if (workType == 1) { // 日报
                item.id = journalEntities.get(position).id;
                item.processInstId = journalEntities.get(position).processInstId;
                state = AppConfig.status.value2;
            } else if (workType == 3) { // 月报
                item.id = journalEntities.get(position).id;
                item.processInstId = journalEntities.get(position).processInstId;
                state = AppConfig.status.value14;
            }
            switch (view.getId()) {
                case R.id.bnt1: // 撤销
                    auditAdd("3", state, item);
                    break;
                case R.id.bnt2:// 拒绝
                    auditAdd("2", state, item);
                    break;
                case R.id.bnt3:// 同意
                    auditAdd("1", state, item);
                    break;
            }
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            bundle = new Bundle();
            if (workType == 3) {
                bundle.putString("id", journalEntities.get(position).Id);
            } else {
                bundle.putString("id", journalEntities.get(position).id);
            }
            if (workType != 2) {
                bundle.putString("workType", workType + "");
                openActivity(MonthlyDetailsActivity.class, bundle);
            }
        });

    }


    @Override
    protected void initData() {
        getAllTypeData(Constants.RequestMode.FRIST);
    }

    private void getAllTypeData(Constants.RequestMode mode) {
        if (workType == 1) {
            getlistLogQuery(mode);
        } else if (workType == 2) {
            getweeklyQuery(mode);
        } else if (workType == 3) {
            getListMonQuery(mode);
        }
    }

    /**
     * 获取月报
     */
    private void getListMonQuery(Constants.RequestMode mode) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("logType", logType + "");
        hashMap.put("page", page + "");
        showLoading();
        HttpUtil.log_ListMonQuery(hashMap).execute(new JsonCallback<Result<List<JournalEntity>>>() {
            @Override
            public void onSuccess(Result<List<JournalEntity>> result, Call call, Response response) {
                closeLoading();
                mBinding.emptylayout.showContent();
                mBinding.refreshlayout.finishRefresh(); // 刷新完成
                mBinding.refreshlayout.finishLoadMore();
                if (mode == Constants.RequestMode.FRIST) {
                    journalEntities.clear();
                    if (result.data != null && result.data.size() > 0) {
                        journalEntities.addAll(result.data);
                    } else {
                        mBinding.emptylayout.showEmpty();
                    }
                } else if (mode == Constants.RequestMode.LOAD_MORE) {
                    if (result.data != null && result.data.size() > 0) {
                        List<JournalEntity> list = result.data;
                        journalEntities.addAll(list);
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
                    getListMonQuery(Constants.RequestMode.FRIST);
                });
            }
        });

    }

    /**
     * 获取周报
     *
     * @param mode
     */
    private void getweeklyQuery(Constants.RequestMode mode) {
        showLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("keyword", "");
        hashMap.put("page", page);
        HttpUtil.weeklyQuery(hashMap).execute(new JsonCallback<Result<List<JournalEntity>>>() {
            @Override
            public void onSuccess(Result<List<JournalEntity>> result, Call call, Response response) {
                closeLoading();
                mBinding.emptylayout.showContent();
                mBinding.refreshlayout.finishRefresh(); // 刷新完成
                mBinding.refreshlayout.finishLoadMore();
                if (mode == Constants.RequestMode.FRIST) {
                    journalEntities.clear();
                    if (result.data != null && result.data.size() > 0) {
                        journalEntities.addAll(result.data);
                    } else {
                        mBinding.emptylayout.showEmpty();
                    }
                } else if (mode == Constants.RequestMode.LOAD_MORE) {
                    if (result.data != null && result.data.size() > 0) {
                        List<JournalEntity> list = result.data;
                        journalEntities.addAll(list);
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
                    getweeklyQuery(Constants.RequestMode.FRIST);
                });
            }
        });
    }


    /**
     * 获取日报
     *
     * @param mode
     */
    private void getlistLogQuery(Constants.RequestMode mode) {
        showLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("logType", logType + "");
        hashMap.put("page", page + "");
        HttpUtil.listLogQuery(hashMap).execute(new JsonCallback<Result<List<JournalEntity>>>() {
            @Override
            public void onSuccess(Result<List<JournalEntity>> result, Call call, Response response) {
                closeLoading();
                mBinding.emptylayout.showContent();
                mBinding.refreshlayout.finishRefresh(); // 刷新完成
                mBinding.refreshlayout.finishLoadMore();
                if (mode == Constants.RequestMode.FRIST) {
                    journalEntities.clear();
                    if (result.data != null && result.data.size() > 0) {
                        journalEntities.addAll(result.data);
                    } else {
                        mBinding.emptylayout.showEmpty();
                    }
                } else if (mode == Constants.RequestMode.LOAD_MORE) {
                    if (result.data != null && result.data.size() > 0) {
                        List<JournalEntity> list = result.data;
                        journalEntities.addAll(list);
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
                    getlistLogQuery(Constants.RequestMode.FRIST);
                });
            }
        });

    }

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
     * 审批处理成功回调
     */
    @Override
    protected void successTreatment() {
        super.successTreatment();
        page = 1;
        getAllTypeData(Constants.RequestMode.FRIST);
    }

    /**
     * 日期选择
     */
    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    /**
     * 选择日期回调监听
     *
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String desc = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
        if (date_typp == 1) {
            startime = Long.parseLong(year + "" + (month + 1) + dayOfMonth);
            mBinding.startDateTv.setText(desc);
        } else if (date_typp == 2) {
            endtime = Long.parseLong(year + "" + (month + 1) + dayOfMonth);
            if (endtime < startime) {
                showToast("结束时间不能小于开始时间");
            } else {
                mBinding.endDateTv.setText(desc);
            }
        }

    }
}
