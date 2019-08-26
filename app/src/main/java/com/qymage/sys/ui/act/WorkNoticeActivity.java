package com.qymage.sys.ui.act;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityWorkNoticeBinding;
import com.qymage.sys.ui.adapter.WorkNoticeListAdapter;
import com.qymage.sys.ui.entity.WorkNoticeEnt;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 工作通知
 */
public class WorkNoticeActivity extends BBActivity<ActivityWorkNoticeBinding> {


    WorkNoticeListAdapter adapter;
    List<WorkNoticeEnt> listdata = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_work_notice;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WorkNoticeListAdapter(R.layout.item_list_work_notice, listdata);
        mBinding.recyclerview.setAdapter(adapter);
        mBinding.refreshlayout.setOnRefreshListener(refreshLayout -> {

        });
        mBinding.refreshlayout.setOnLoadMoreListener(refreshLayout -> {

        });
    }

    @Override
    protected void initData() {
        super.initData();
        listdata.add(new WorkNoticeEnt("http://b-ssl.duitang.com/uploads/item/201901/15/20190115181136_ZLMrV.thumb.700_0.jpeg"
                , "李麻子"
                , "项目类型：销售"
                , "项目名称：环保水位站"
                , "预算申请：2000元"
                , "申请时间：2019-08-20 10：10"));
        listdata.add(new WorkNoticeEnt("http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20190316/a2b4fe8ce83a453a91354d6e88751f2a.jpeg"
                , "张三提交的采购申请"
                , "项目类型：销售"
                , "项目名称：环保水位站"
                , "预算申请：2000元"
                , "申请时间：2019-08-20 10：10"));

        adapter.notifyDataSetChanged();

    }


}
