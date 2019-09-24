package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityMyAttendanceBinding;
import com.qymage.sys.ui.entity.ClockQueryList;

import java.util.Date;
import java.util.HashMap;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我的考勤
 */
public class MyAttendanceActivity extends BBActivity<ActivityMyAttendanceBinding> implements View.OnClickListener {


    DateTime mothdateTime;
    DateTime weekdateTime;
    ClockQueryList info;
    Bundle bundle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_attendance;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.rankingNumTv.setOnClickListener(this);
        mBinding.kaoqingDateTv.setOnClickListener(this);
        mBinding.liftSubIoc.setOnClickListener(this);
        mBinding.rightIocAdd.setOnClickListener(this);
        mBinding.usernameEt.setOnClickListener(this);
        mBinding.sexEt.setOnClickListener(this);
        mBinding.stationEt.setOnClickListener(this);
        mBinding.birthdayEt.setOnClickListener(this);
        mBinding.absentTv.setOnClickListener(this);
        mBinding.abnormalTv.setOnClickListener(this);
        mBinding.radiogroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.week_btn:
                    mBinding.rankingNumTv.setVisibility(View.INVISIBLE);
                    mBinding.indexNumTv.setVisibility(View.INVISIBLE);
                    weekdateTime = DateUtil.offsetWeek(new Date(), 0);
                    DateTime beginOfWeek = DateUtil.beginOfWeek(weekdateTime);
                    DateTime endOfWeek = DateUtil.endOfWeek(weekdateTime);
                    mBinding.kaoqingDateTv.setText(DateUtil.format(beginOfWeek, "yyyy/MM/dd") + "-" + DateUtil.format(endOfWeek, "yyyy/MM/dd"));
                    break;
                case R.id.month_btn:
                    mothdateTime = DateUtil.offsetMonth(new Date(), 0);
                    mBinding.kaoqingDateTv.setText(DateUtil.format(mothdateTime, "yyyy-MM"));
                    mBinding.rankingNumTv.setVisibility(View.VISIBLE);
                    mBinding.indexNumTv.setVisibility(View.VISIBLE);
                    break;
            }
            initRankDate();
        });
        mBinding.monthBtn.setChecked(true);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    private void initRankDate() {
        showLoading();
        HttpUtil.attendance_ClockQuery(getPer()).execute(new JsonCallback<Result<ClockQueryList>>() {

            @Override
            public void onSuccess(Result<ClockQueryList> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    info = result.data;
                    setDataShow();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                showToast(e.getMessage());
            }
        });

    }


    private void setDataShow() {
        if (info.userName != null) {
            if (info.userName.length() >= 3) {
                String strh = info.userName.substring(info.userName.length() - 2, info.userName.length());   //截取
                mBinding.nameTvBg.setText(strh);
            } else {
                mBinding.nameTvBg.setText(info.userName);
            }
        }
        mBinding.kaoqinRulesTv.setText("工号：" + info.userNo);
        mBinding.userNameTv.setText(info.userName);
        String cindex = VerifyUtils.isEmpty(info.cindex) ? "0" : info.cindex;
        mBinding.indexNumTv.setText("指数：" + cindex);
        String ranking = VerifyUtils.isEmpty(info.ranking) ? "0" : info.ranking;
        mBinding.rankingNumTv.setText("排行: " + ranking);
        String totalHours = VerifyUtils.isEmpty(info.totalHours) ? "0" : info.totalHours;
        mBinding.phoneEt.setText(totalHours + "小时");
        String avgHours = VerifyUtils.isEmpty(info.avgHours) ? "0" : info.avgHours;
        mBinding.jonNumEt.setText(avgHours + "小时");
        String attDays = VerifyUtils.isEmpty(info.attDays) ? "0" : info.attDays;
        mBinding.usernameEt.setText(attDays + "天");
        String restDays = VerifyUtils.isEmpty(info.restDays) ? "0" : info.restDays;
        mBinding.sexEt.setText(restDays + "天");
        String late = VerifyUtils.isEmpty(info.late) ? "0" : info.late;
        mBinding.stationEt.setText(late + "次");
        String leaveEarly = VerifyUtils.isEmpty(info.leaveEarly) ? "0" : info.leaveEarly;
        mBinding.birthdayEt.setText(leaveEarly + "次");
        String absent = VerifyUtils.isEmpty(info.absent) ? "0" : info.absent;
        mBinding.absentTv.setText(absent + "次");
        String abnormal = VerifyUtils.isEmpty(info.abnormal) ? "0" : info.abnormal;
        mBinding.abnormalTv.setText(abnormal + "次");
        String leave = VerifyUtils.isEmpty(info.leave) ? "0" : info.leave;
        mBinding.leaveTv.setText(leave + "次");
        String business = VerifyUtils.isEmpty(info.business) ? "0" : info.business;
        mBinding.businessTv.setText(business + "次");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ranking_num_tv:
                openActivity(RankingActivity.class);

                break;
            case R.id.kaoqing_date_tv:

                break;
            case R.id.lift_sub_ioc: //减
                if (mBinding.monthBtn.isChecked()) { // 选中是月
                    mothdateTime = DateUtil.offsetMonth(mothdateTime, -1);
                    mBinding.kaoqingDateTv.setText(DateUtil.format(mothdateTime, "yyyy-MM"));
                } else {
                    weekdateTime = DateUtil.offsetWeek(weekdateTime, -1);
                    DateTime beginOfWeek = DateUtil.beginOfWeek(weekdateTime);
                    DateTime endOfWeek = DateUtil.endOfWeek(weekdateTime);
                    mBinding.kaoqingDateTv.setText(DateUtil.format(beginOfWeek, "yyyy/MM/dd") + "-" + DateUtil.format(endOfWeek, "yyyy/MM/dd"));
                }
                initRankDate();
                break;
            case R.id.right_ioc_add: //加
                if (mBinding.monthBtn.isChecked()) { // 选中是月
                    mothdateTime = DateUtil.offsetMonth(mothdateTime, 1);
                    mBinding.kaoqingDateTv.setText(DateUtil.format(mothdateTime, "yyyy-MM"));
                } else {
                    weekdateTime = DateUtil.offsetWeek(weekdateTime, 1);
                    DateTime beginOfWeek = DateUtil.beginOfWeek(weekdateTime);
                    DateTime endOfWeek = DateUtil.endOfWeek(weekdateTime);
                    mBinding.kaoqingDateTv.setText(DateUtil.format(beginOfWeek, "yyyy/MM/dd") + "-" + DateUtil.format(endOfWeek, "yyyy/MM/dd"));
                }
                initRankDate();
                break;
            case R.id.username_et:
                if (info != null && info.attDaysList != null) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", info);
                    bundle.putString("type", "attDaysList");
                    openActivity(MyAttendanceListActivity.class, bundle);
                } else {
                    showToast("暂无数据");
                }
                break;
            case R.id.sex_et:
                if (info != null && info.restDaysList != null) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", info);
                    bundle.putString("type", "restDaysList");
                    openActivity(MyAttendanceListActivity.class, bundle);
                } else {
                    showToast("暂无数据");
                }
                break;
            case R.id.station_et:
                if (info != null && info.lateList != null) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", info);
                    bundle.putString("type", "lateList");
                    openActivity(MyAttendanceListActivity.class, bundle);
                } else {
                    showToast("暂无数据");
                }
                break;
            case R.id.birthday_et:
                if (info != null && info.attDaysList != null) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", info);
                    bundle.putString("type", "attDaysList");
                    openActivity(MyAttendanceListActivity.class, bundle);
                } else {
                    showToast("暂无数据");
                }

                break;
            case R.id.absent_tv:
                if (info != null && info.absentList != null) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", info);
                    bundle.putString("type", "absentList");
                    openActivity(MyAttendanceListActivity.class, bundle);
                } else {
                    showToast("暂无数据");
                }
                break;
            case R.id.abnormal_tv:
                if (info != null && info.abnormalList != null) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", info);
                    bundle.putString("type", "abnormalList");
                    openActivity(MyAttendanceListActivity.class, bundle);
                } else {
                    showToast("暂无数据");
                }
                break;
        }
    }

    private HashMap<String, Object> getPer() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userCode", getUserId());
        if (mBinding.monthBtn.isChecked()) {
            hashMap.put("type", 1 + "");//统计类型 1-月  2-周
        } else {
            hashMap.put("type", 2 + "");//统计类型 1-月  2-周
        }
        hashMap.put("clockDate", mBinding.kaoqingDateTv.getText().toString());

        return hashMap;

    }


}
