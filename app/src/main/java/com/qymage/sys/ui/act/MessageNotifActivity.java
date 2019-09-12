package com.qymage.sys.ui.act;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivityMessageNotifBinding;
import com.qymage.sys.ui.adapter.MessageNotifAdapter;
import com.qymage.sys.ui.entity.JournalEntity;
import com.qymage.sys.ui.entity.MessageNotifEnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 消息通知类型
 */
public class MessageNotifActivity extends BBActivity<ActivityMessageNotifBinding> {


    List<MessageNotifEnt> listdata = new ArrayList<>();
    MessageNotifAdapter notifAdapter;
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_notif;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.refreshlayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            get_listNotice(Constants.RequestMode.FRIST);
        });
        mBinding.refreshlayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            get_listNotice(Constants.RequestMode.LOAD_MORE);
        });
        notifAdapter = new MessageNotifAdapter(R.layout.item_list_messagenotif, listdata);
        mBinding.recyclerview.setAdapter(notifAdapter);
        notifAdapter.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle3 = new Bundle();
            bundle3.putString("path", "www");
            bundle3.putString("title", "详情");
            bundle3.putString("content", listdata.get(position).noticeContent);
            openActivity(BrowsePathActivity.class, bundle3);
            msgUdate(listdata.get(position).Id);
        });
    }


    @Override
    protected void initData() {
        super.initData();
        get_listNotice(Constants.RequestMode.FRIST);
    }

    private void get_listNotice(Constants.RequestMode mode) {
        showLoading();
        HttpUtil.notice_listNotice(new HashMap<>()).execute(new JsonCallback<Result<List<MessageNotifEnt>>>() {
            @Override
            public void onSuccess(Result<List<MessageNotifEnt>> result, Call call, Response response) {
                closeLoading();
                mBinding.emptylayout.showContent();
                mBinding.refreshlayout.finishRefresh(); // 刷新完成
                mBinding.refreshlayout.finishLoadMore();
                if (mode == Constants.RequestMode.FRIST) {
                    listdata.clear();
                    if (result.data != null && result.data.size() > 0) {
                        listdata.addAll(result.data);
                    } else {
                        mBinding.emptylayout.showEmpty();
                    }
                } else if (mode == Constants.RequestMode.LOAD_MORE) {
                    if (result.data != null && result.data.size() > 0) {
                        List<MessageNotifEnt> list = result.data;
                        listdata.addAll(list);
                    } else {
                        // 全部加载完成,没有数据了调用此方法
                        mBinding.refreshlayout.finishLoadMoreWithNoMoreData();
                        page--;
                    }
                }
                notifAdapter.notifyDataSetChanged();
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
                    get_listNotice(Constants.RequestMode.FRIST);
                });
            }
        });
    }

    /**
     * 更新消息
     *
     * @param msgId
     */
    private void msgUdate(String msgId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("msgId", msgId);
        HttpUtil.msgUdate(hashMap).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }


}
