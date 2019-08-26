package com.qymage.sys.ui.act;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityMessageNotifBinding;
import com.qymage.sys.ui.adapter.MessageNotifAdapter;
import com.qymage.sys.ui.entity.MessageNotifEnt;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息通知类型
 */
public class MessageNotifActivity extends BBActivity<ActivityMessageNotifBinding> {


    List<MessageNotifEnt> listdata = new ArrayList<>();
    MessageNotifAdapter notifAdapter;


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

        });
        mBinding.refreshlayout.setOnLoadMoreListener(refreshLayout -> {

        });
        notifAdapter = new MessageNotifAdapter(R.layout.item_list_messagenotif, listdata);
        mBinding.recyclerview.setAdapter(notifAdapter);

    }

    @Override
    protected void initData() {
        super.initData();
        listdata.add(new MessageNotifEnt("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566749559061&di=9ce46a2239aac4e680b714a35fe06974&imgtype=0&src=http%3A%2F%2Fimg.aiimg.com%2Fuploads%2Fallimg%2F140428%2F1-14042R30P0.jpg"
                , "设置小目标，轻松养成好习惯，成就更好的自己，快来一起体验吧！",
                "8月25日 09:10"));
        listdata.add(new MessageNotifEnt("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566749821477&di=de4eec14f0dea448d832542be2e5eb32&imgtype=0&src=http%3A%2F%2Fimg.sccnn.com%2Fbimg%2F339%2F23457.jpg"
                , "设置小目标，轻松养成好习惯，成就更好的自己，快来一起体验吧！"
                , "8月26日 11:10"));
        notifAdapter.notifyDataSetChanged();
    }


}
