package com.qymage.sys.ui.act;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.widget.FrameEmptyLayout;
import com.qymage.sys.databinding.ActivityRankingBinding;
import com.qymage.sys.ui.adapter.RankingAdapter;
import com.qymage.sys.ui.entity.RankingKaoQing;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 排名
 */
public class RankingActivity extends BBActivity<ActivityRankingBinding> {


    RankingAdapter adapter;
    List<RankingKaoQing.RankListEntity> listdata = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ranking;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.refreshlayout.setEnableLoadMore(false);
        mBinding.refreshlayout.setOnRefreshListener(refreshLayout -> {
            get_rankQuery();
        });
        adapter = new RankingAdapter(R.layout.imte_list_ranking, listdata);
        mBinding.recyclerview.setAdapter(adapter);


    }


    @Override
    protected void initData() {
        super.initData();
        get_rankQuery();

    }


    private void get_rankQuery() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("data", "");
        showLoading();
        HttpUtil.attendance_rankQuery(hashMap).execute(new JsonCallback<Result<RankingKaoQing>>() {

            @Override
            public void onSuccess(Result<RankingKaoQing> result, Call call, Response response) {
                if (result.data != null) {
                    mBinding.emptylayout.showContent();
                    mBinding.averageManHour.setText("平均工时：" + result.data.avgHours);
                    if (result.data.rankList != null) {
                        listdata.clear();
                        listdata.addAll(result.data.rankList);
                        adapter.notifyDataSetChanged();
                    } else {
                        mBinding.emptylayout.showEmpty();
                    }
                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                showToast(e.getMessage());
                mBinding.emptylayout.showError();
                mBinding.emptylayout.setRetryListener(() -> {
                    get_rankQuery();
                });
            }
        });

    }


}
