package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.DatePicker;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.base.baseFragment.FragmentLazy;
import com.qymage.sys.databinding.FragmentAskForLeaveRecordBinding;
import com.qymage.sys.databinding.FragmentJournalBinding;
import com.qymage.sys.ui.adapter.AskForLeaveRecordAdapter;
import com.qymage.sys.ui.adapter.JournalAdapter;
import com.qymage.sys.ui.entity.AskForLeaveEntity;
import com.qymage.sys.ui.entity.JournalEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 请假记录
 */
public class AskForLeaveRecordlActivity extends BBActivity<FragmentAskForLeaveRecordBinding> {


    // AskForLeaveRecordActivity

    List<AskForLeaveEntity> record = new ArrayList<>();
    AskForLeaveRecordAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ask_for_leave_record;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AskForLeaveRecordAdapter(R.layout.item_list_ask_for_leave, record);
        mBinding.recyclerview.setAdapter(adapter);
        mBinding.refreshlayout.setOnRefreshListener(refreshLayout -> {
            mBinding.refreshlayout.finishRefresh();
        });
        mBinding.refreshlayout.setOnLoadMoreListener(refreshLayout -> {
            mBinding.refreshlayout.finishLoadMore();
        });
    }


    @Override
    protected void initData() {

        record.add(new AskForLeaveEntity(
                "http://img0w.pconline.com.cn/pconline/1312/20/spcgroup/width_640,qua_30/4037677_09-002443_965.jpg",
                "小月月",
                "婚假",
                "2019-08-24 09:00",
                "2019-08-24 17:00",
                "等待xxxxx审批",
                0));

        record.add(new AskForLeaveEntity(
                "http://5b0988e595225.cdn.sohucs.com/images/20190715/eafa82ee23204e7eaf336403a2bfc8b1.jpeg",
                "小花花",
                "产假",
                "2019-08-24 10:00",
                "2019-08-25 10:00",
                "审批拒绝",
                1));

        record.add(new AskForLeaveEntity(
                "http://5b0988e595225.cdn.sohucs.com/images/20190715/eafa82ee23204e7eaf336403a2bfc8b1.jpeg",
                "张三四",
                "事假",
                "2019-09-24 10:00",
                "2019-09-25 10:00",
                "同意申请",
                2));


        adapter.notifyDataSetChanged();
    }

}
