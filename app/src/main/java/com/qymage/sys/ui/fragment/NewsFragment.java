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
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.FragmentNewsBinding;
import com.qymage.sys.ui.act.ApplicationCollectionLogActivity;
import com.qymage.sys.ui.act.AskForLeaveRecordlActivity;
import com.qymage.sys.ui.act.BidPerformanceLvYueSZActivity;
import com.qymage.sys.ui.act.ChoiceContractLogActivity;
import com.qymage.sys.ui.act.MainActivity;
import com.qymage.sys.ui.act.MessageNotifActivity;
import com.qymage.sys.ui.act.MyLoanActivity;
import com.qymage.sys.ui.act.ProjectApprovaLoglActivity;
import com.qymage.sys.ui.act.WorkNoticeActivity;
import com.qymage.sys.ui.adapter.NewsAdapter;
import com.qymage.sys.ui.entity.NewsEntity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A 消息
 */
public class NewsFragment extends FragmentLazy<FragmentNewsBinding> {


    List<NewsEntity> newsEntities = new ArrayList<>();

    NewsAdapter adapter;
    Bundle bundle;


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
            getMessage_User_Msg();
        });
        mBinding.refreshlayout.setEnableLoadMore(false);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            switch (newsEntities.get(position).msgType) {
                case 1://立项记录
                    openActivity(ProjectApprovaLoglActivity.class);
                    break;
                case 2://日报
                    MainActivity.viewpager.setCurrentItem(2);
                    MainActivity.tab3.setChecked(true);
                    break;
                case 3:// 投标保证金支记录
                    bundle = new Bundle();
                    bundle.putString("type", "01");
                    openActivity(BidPerformanceLvYueSZActivity.class, bundle);
                    break;
                case 4:// 投标保证金收
                    bundle = new Bundle();
                    bundle.putString("type", "02");
                    openActivity(BidPerformanceLvYueSZActivity.class, bundle);
                    break;
                case 5:// 履约保证金支
                    bundle = new Bundle();
                    bundle.putString("type", "03");
                    openActivity(BidPerformanceLvYueSZActivity.class, bundle);
                    break;
                case 6://履约保证金收
                    bundle = new Bundle();
                    bundle.putString("type", "04");
                    openActivity(BidPerformanceLvYueSZActivity.class, bundle);
                    break;
                case 7://请假记录
                    openActivity(AskForLeaveRecordlActivity.class);
                    break;
                case 8:// 合同记录
                    openActivity(ChoiceContractLogActivity.class);
                    break;
                case 9:// 付款记录
                    bundle = new Bundle();
                    bundle.putString("type", "2");
                    openActivity(ApplicationCollectionLogActivity.class, bundle);
                    break;
                case 10://收款记录
                    bundle = new Bundle();
                    bundle.putString("type", "1");
                    openActivity(ApplicationCollectionLogActivity.class, bundle);
                    break;
                case 11://开票记录
                    bundle = new Bundle();
                    bundle.putString("type", "3");
                    openActivity(ApplicationCollectionLogActivity.class, bundle);
                    break;
                case 12://收票记录
                    bundle = new Bundle();
                    bundle.putString("type", "4");
                    openActivity(ApplicationCollectionLogActivity.class, bundle);
                    break;
                case 13://借款记录
                    openActivity(MyLoanActivity.class);
                    break;
                case 14: //
                    MainActivity.viewpager.setCurrentItem(2);
                    MainActivity.tab3.setChecked(true);
                    break;
                case 15:
                    openActivity(MessageNotifActivity.class);
                    break;
            }
        });


    }

    @Override
    protected void initData() {
        getMessage_User_Msg();
    }


    private void getMessage_User_Msg() {
        showLoading();
        HttpUtil.message_User_Msg(new HashMap<>()).execute(new JsonCallback<Result<List<NewsEntity>>>() {
            @Override
            public void onSuccess(Result<List<NewsEntity>> result, Call call, Response response) {
                closeLoading();
                mBinding.emptylayout.showContent();
                mBinding.refreshlayout.finishRefresh(); // 刷新完成
                if (result.data != null && result.data.size() > 0) {
                    newsEntities.clear();
                    newsEntities.addAll(result.data);
                    adapter.notifyDataSetChanged();
                } else {
                    mBinding.emptylayout.showEmpty();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                mBinding.refreshlayout.finishRefresh(); // 刷新完成
                showToast(e.getMessage());
                mBinding.emptylayout.showError();
                mBinding.emptylayout.setRetryListener(() -> {
                    getMessage_User_Msg();
                });
            }
        });


    }


}
