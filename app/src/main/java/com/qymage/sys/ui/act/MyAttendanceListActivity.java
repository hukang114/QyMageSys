package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.util.DateUtil;
import com.qymage.sys.databinding.ActivityMyAttendanceListBinding;
import com.qymage.sys.ui.entity.ClockQueryList;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 考勤管理列表
 */
public class MyAttendanceListActivity extends BBActivity<ActivityMyAttendanceListBinding> {


    ClockQueryList info;
    private Intent mIntent;
    private String type;
    CommonAdapter<ClockQueryList.AttDaysListBean> adapter1;
    CommonAdapter<ClockQueryList.RestDaysListBean> adapter2;
    CommonAdapter<ClockQueryList.LateListBean> adapter3;
    CommonAdapter<ClockQueryList.LeaveEarlyListBean> adapter4;
    CommonAdapter<ClockQueryList.AbsentListBean> adapter5;
    CommonAdapter<ClockQueryList.AbnormalListBean> adapter6;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_attendance_list;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        info = (ClockQueryList) mIntent.getSerializableExtra("data");
        type = mIntent.getStringExtra("type");
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void initData() {
        super.initData();
        if (type.equals("attDaysList")) {
            mBinding.metitle.setcTxt("出勤天数");
            setAdapter1();
        } else if (type.equals("restDaysList")) {
            mBinding.metitle.setcTxt("休息天数");
            setAdapter2();
        } else if (type.equals("lateList")) {
            mBinding.metitle.setcTxt("迟到次数");
            setAdapter3();
        } else if (type.equals("leaveEarlyList")) {
            mBinding.metitle.setcTxt("早退次数");
            setAdapter4();
        } else if (type.equals("absentList")) {
            mBinding.metitle.setcTxt("旷工");
            setAdapter5();
        } else if (type.equals("abnormalList")) {
            mBinding.metitle.setcTxt("异常");
            setAdapter6();
        }

    }

    private void setAdapter6() {
        adapter6 = new CommonAdapter<ClockQueryList.AbnormalListBean>(this, R.layout.item_list_myattendance, info.abnormalList) {
            @Override
            protected void convert(ViewHolder holder, ClockQueryList.AbnormalListBean item, int position) {
                String clockMode;
                if (item.clockMode.equals("1")) {
                    clockMode = "上班打卡";
                } else {
                    clockMode = "下班打卡";
                }
                holder.setText(R.id.crate_time_tv, item.date);
                holder.setText(R.id.weekday_tv, DateUtil.DateToWeek(cn.hutool.core.date.DateUtil.parse(item.date).getTime()) + "\n" +
                        "打卡时间：" + item.clockTime + "\n" + "打卡备注：" + item.remarks + "\n" + "打卡地址：" + item.address + "\n" + clockMode);
            }
        };
        mBinding.recyclerview.setAdapter(adapter6);
    }

    private void setAdapter5() {
        adapter5 = new CommonAdapter<ClockQueryList.AbsentListBean>(this, R.layout.item_list_myattendance, info.absentList) {
            @Override
            protected void convert(ViewHolder holder, ClockQueryList.AbsentListBean item, int position) {
                holder.setText(R.id.crate_time_tv, item.date);
                holder.setText(R.id.weekday_tv, DateUtil.DateToWeek(cn.hutool.core.date.DateUtil.parse(item.date).getTime()));
            }
        };
        mBinding.recyclerview.setAdapter(adapter5);
    }

    private void setAdapter4() {
        adapter4 = new CommonAdapter<ClockQueryList.LeaveEarlyListBean>(this, R.layout.item_list_myattendance, info.leaveEarlyList) {
            @Override
            protected void convert(ViewHolder holder, ClockQueryList.LeaveEarlyListBean item, int position) {
                holder.setText(R.id.crate_time_tv, item.date);
                holder.setText(R.id.weekday_tv, DateUtil.DateToWeek(cn.hutool.core.date.DateUtil.parse(item.date).getTime()) + "\n" +
                        "打卡时间：" + item.clockTime + "\n" + "打卡备注：" + item.remarks + "\n" + "迟到时长：" + item.result + "分钟");
            }
        };
        mBinding.recyclerview.setAdapter(adapter4);

    }

    private void setAdapter3() {
        adapter3 = new CommonAdapter<ClockQueryList.LateListBean>(this, R.layout.item_list_myattendance, info.lateList) {
            @Override
            protected void convert(ViewHolder holder, ClockQueryList.LateListBean item, int position) {
                holder.setText(R.id.crate_time_tv, item.date);
                holder.setText(R.id.weekday_tv, DateUtil.DateToWeek(cn.hutool.core.date.DateUtil.parse(item.date).getTime()) + "\n" +
                        "打卡时间：" + item.clockTime + "\n" + "打卡备注：" + item.remarks + "\n" + "迟到时长：" + item.result + "分钟");
            }
        };
        mBinding.recyclerview.setAdapter(adapter3);
    }

    private void setAdapter2() {
        adapter2 = new CommonAdapter<ClockQueryList.RestDaysListBean>(this, R.layout.item_list_myattendance, info.restDaysList) {
            @Override
            protected void convert(ViewHolder holder, ClockQueryList.RestDaysListBean item, int position) {
                holder.setText(R.id.crate_time_tv, item.date);
                holder.setText(R.id.weekday_tv, DateUtil.DateToWeek(cn.hutool.core.date.DateUtil.parse(item.date).getTime()));
            }
        };
        mBinding.recyclerview.setAdapter(adapter2);
    }

    private void setAdapter1() {
        adapter1 = new CommonAdapter<ClockQueryList.AttDaysListBean>(this, R.layout.item_list_myattendance, info.attDaysList) {
            @Override
            protected void convert(ViewHolder holder, ClockQueryList.AttDaysListBean item, int position) {
                holder.setText(R.id.crate_time_tv, item.date);
                holder.setText(R.id.weekday_tv, DateUtil.DateToWeek(cn.hutool.core.date.DateUtil.parse(item.date).getTime()));
            }
        };
        mBinding.recyclerview.setAdapter(adapter1);
    }


}
