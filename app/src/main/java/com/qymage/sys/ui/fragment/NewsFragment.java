package com.qymage.sys.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qymage.sys.R;
import com.qymage.sys.common.base.baseFragment.BBFragment;
import com.qymage.sys.common.base.baseFragment.FragmentLazy;
import com.qymage.sys.databinding.FragmentNewsBinding;
import com.qymage.sys.ui.act.MessageNotifActivity;
import com.qymage.sys.ui.act.WorkNoticeActivity;
import com.qymage.sys.ui.adapter.NewsAdapter;
import com.qymage.sys.ui.entity.NewsEntity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A 消息
 */
public class NewsFragment extends FragmentLazy<FragmentNewsBinding> {


    List<NewsEntity> newsEntities = new ArrayList<>();

    NewsAdapter adapter;


    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void baseInit() {
        super.baseInit();
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NewsAdapter(R.layout.item_list_news_home, newsEntities);
        mBinding.recyclerview.setAdapter(adapter);
        mBinding.refreshlayout.setOnRefreshListener(refreshLayout -> {
            mBinding.refreshlayout.finishRefresh();
        });
        mBinding.refreshlayout.setOnLoadMoreListener(refreshLayout -> {
            mBinding.refreshlayout.finishLoadMore();
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {

            if (position == 1) {
                openActivity(MessageNotifActivity.class);
            } else {
                openActivity(WorkNoticeActivity.class);
            }

        });


    }

    @Override
    protected void initData() {
        newsEntities.add(new NewsEntity(R.mipmap.msg_cate1, "工作通知", "2019-01-01", "[请假类]后台程序员提交请假申请", 2));
        newsEntities.add(new NewsEntity(R.mipmap.msg_cate2, "通知公告", "2019-01-02", "[请假类]你提交的请假申请审批被拒绝", 10));
        adapter.notifyDataSetChanged();
    }
}
