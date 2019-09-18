package com.qymage.sys.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.http.LogUtils;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityAskforDetBinding;
import com.qymage.sys.databinding.ActivityMonthlyDetBinding;
import com.qymage.sys.ui.adapter.ProcessListAdapter;
import com.qymage.sys.ui.entity.DayWeekMonthDet;
import com.qymage.sys.ui.entity.LoanQueryDetEnt;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.qymage.sys.ui.entity.ProjectApprovaLoglDetEnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 月报详情 日报详情
 */
public class MonthlyDetailsActivity extends BBActivity<ActivityMonthlyDetBinding> implements View.OnClickListener {


    private String id;// 列表id
    private Intent mIntent;
    Bundle bundle;
    DayWeekMonthDet info;
    private String workType;
    List<ProjectApprovaLoglDetEnt.ActivityVoListBean> voListBeans = new ArrayList<>();
    ProcessListAdapter listAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_monthly_det;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        id = mIntent.getStringExtra("id");
        workType = mIntent.getStringExtra("workType");
        if (id == null) {
            return;
        }
        LogUtils.e("id" + id);
        if (workType.equals("1")) {
            mBinding.metitle.setcTxt("日报详情");
        } else if (workType.equals("2")) {
            mBinding.metitle.setcTxt("周报详情");
        } else if (workType.equals("3")) {
            mBinding.metitle.setcTxt("月报详情");
        }
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ProcessListAdapter(R.layout.item_list_process, voListBeans);
        mBinding.recyclerview.setAdapter(listAdapter);
        mBinding.bnt2.setOnClickListener(this);
        mBinding.bnt3.setOnClickListener(this);
        mBinding.bnt1.setOnClickListener(this);
        switch (MyLoanActivity.mType) {
            case 1:
                mBinding.bnt1.setVisibility(View.GONE);
                mBinding.bnt2.setVisibility(View.VISIBLE);
                mBinding.bnt3.setVisibility(View.VISIBLE);
                break;
            case 2:
                mBinding.bnt1.setVisibility(View.GONE);
                mBinding.bnt2.setVisibility(View.GONE);
                mBinding.bnt3.setVisibility(View.GONE);
                break;
            case 3:
                mBinding.bnt1.setVisibility(View.GONE);
                mBinding.bnt2.setVisibility(View.GONE);
                mBinding.bnt3.setVisibility(View.GONE);
                break;
            case 4:
                mBinding.bnt1.setVisibility(View.VISIBLE);
                mBinding.bnt2.setVisibility(View.GONE);
                mBinding.bnt3.setVisibility(View.GONE);
                break;
            default:
                break;
        }


    }

    @Override
    protected void initData() {
        super.initData();
        if (workType.equals("1")) {
            getDayDet();
        } else if (workType.equals("2")) {
        } else if (workType.equals("3")) {
            getMonthlDet();
        }

    }


    /**
     * 获取月报详情
     */
    private void getMonthlDet() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Id", id);
        showLoading();
        HttpUtil.log_monQuery(map).execute(new JsonCallback<Result<DayWeekMonthDet>>() {
            @Override
            public void onSuccess(Result<DayWeekMonthDet> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    info = result.data;
                    setDataShow();
                    setAddList();
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

    private void setAddList() {
        if (info.activityVo != null) {
            for (int i = 0; i < info.activityVo.size(); i++) {
                ProjectApprovaLoglDetEnt.ActivityVoListBean bean = new ProjectApprovaLoglDetEnt.ActivityVoListBean();
                bean.id = info.activityVo.get(i).id;
                bean.processInstanceId = info.activityVo.get(i).processInstanceId;
                bean.processDefId = info.activityVo.get(i).processDefId;
                bean.assignee = info.activityVo.get(i).assignee;
                bean.name = info.activityVo.get(i).name;
                bean.status = info.activityVo.get(i).status;
                bean.comment = info.activityVo.get(i).comment;
                bean.actType = info.activityVo.get(i).actType;
                bean.actId = info.activityVo.get(i).actId;
                bean.businessKey = info.activityVo.get(i).businessKey;
                bean.createdDate = info.activityVo.get(i).createdDate;
                bean.updatedDate = info.activityVo.get(i).updatedDate;
                bean.userId = info.activityVo.get(i).userId;
                bean.userName = info.activityVo.get(i).userName;
                voListBeans.add(bean);
            }
            listAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取日报
     */
    private void getDayDet() {
        showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        HttpUtil.log_logQuery(map).execute(new JsonCallback<Result<DayWeekMonthDet>>() {
            @Override
            public void onSuccess(Result<DayWeekMonthDet> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    info = result.data;
                    setDataShow();
                    setAddList();
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


    private HashMap<String, Object> getPer() {
        HashMap<String, Object> map = new HashMap<>();
        if (workType.equals("1")) {
            map.put("id", id);
        } else {
            map.put("Id", id);
        }
        return map;
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
        mBinding.userName.setText(info.userName + "提交申请");
        mBinding.actstatusTv.setText(info.actStatus);

        if (workType.equals("1")) {
//        mBinding.szbmType.setText("合同名称：" + item.contractName);
            mBinding.szbmType.setVisibility(View.GONE);
//        mBinding.projType.setText("合同类型：" + item.contractTypeName);
            mBinding.projType.setVisibility(View.GONE);
            mBinding.projNumber.setText("昨日工作计划：" + info.dayWork);
            mBinding.projName.setText("明日工作计划：" + info.wordplay);
            mBinding.persionName.setText("本周工作总结：" + info.jobWord);
            mBinding.appalyContentTv.setText("下周工作计划：" + info.nextWork);
            mBinding.dateTimeTv.setText("时间：" + info.createTime);
//            mBinding.introductionTv.setText("借款事由：" + item.cause);
            mBinding.introductionTv.setVisibility(View.GONE);
        } else if (workType.equals("3")) {
//            mBinding.szbmType.setText("合同名称：" + item.contractName);
            mBinding.szbmType.setVisibility(View.GONE);
            mBinding.projType.setText("时间：" + info.createTime);
            mBinding.projNumber.setText("本月工作计划：" + info.monthWork);
            mBinding.projName.setText("本月工作总结：" + info.submonthWork);
            mBinding.persionName.setText("下月工作计划：" + info.nextMonthWeek);
            mBinding.appalyContentTv.setText("自我评级：" + info.selg);
            mBinding.dateTimeTv.setText("领导评语：" + info.leadComment);
            mBinding.introductionTv.setText("领导打分：" + info.leadGrade);

        }


    }

    ProjectAppLogEnt appLogEnt;
    int status;

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        if (info != null) {
            appLogEnt = new ProjectAppLogEnt();
            appLogEnt.processInstId = info.processInstanceId;
            appLogEnt.id = info.Id;
            if (workType.equals("1")) {
                status = AppConfig.status.value2;
            } else if (workType.equals("3")) {
                status = AppConfig.status.value14;
            }

        }
        switch (v.getId()) {
            case R.id.bnt2: // 拒绝
                if (appLogEnt != null) {
                    auditAdd("2", status, appLogEnt);
                }
                break;
            case R.id.bnt3:// 同意
                if (appLogEnt != null) {
                    auditAdd("1", status, appLogEnt);
                }
                break;
            case R.id.bnt1:
                if (appLogEnt != null) {
                    auditAdd("3", status, appLogEnt);
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void successTreatment() {
        super.successTreatment();
        setResult(200);
        finish();
    }
}
