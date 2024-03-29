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
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityContractApplicationDetBinding;
import com.qymage.sys.databinding.ActivityLoanDetBinding;
import com.qymage.sys.ui.adapter.ProcessListAdapter;
import com.qymage.sys.ui.entity.ContractDetEnt;
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
 * 借款详情
 */
public class LoanDetailsActivity extends BBActivity<ActivityLoanDetBinding> implements View.OnClickListener {


    private String id;// 合同id
    private Intent mIntent;
    Bundle bundle;
    LoanQueryDetEnt info;
    List<ProjectApprovaLoglDetEnt.ActivityVoListBean> voListBeans = new ArrayList<>();
    ProcessListAdapter listAdapter;
    public static LoanDetailsActivity instance = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loan_det;
    }


    @Override
    protected void initView() {
        super.initView();
        instance = this;
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        id = mIntent.getStringExtra("id");
        if (id == null) {
            return;
        }
        mBinding.bnt2.setOnClickListener(this);
        mBinding.bnt3.setOnClickListener(this);
        mBinding.bnt1.setOnClickListener(this);
        mBinding.bnt4.setOnClickListener(this);
        mBinding.newBuildBtn.setOnClickListener(this);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ProcessListAdapter(R.layout.item_list_process, voListBeans);
        mBinding.recyclerview.setAdapter(listAdapter);
        mBinding.bottomStateLayout.setVisibility(View.GONE);


    }

    @Override
    protected void initData() {
        super.initData();
        getDetData();

    }

    private void getDetData() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        showLoading();
        HttpUtil.loan_loanQueryDet(hashMap).execute(new JsonCallback<Result<LoanQueryDetEnt>>() {
            @Override
            public void onSuccess(Result<LoanQueryDetEnt> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    mBinding.bottomStateLayout.setVisibility(View.VISIBLE);
                    info = result.data;
                    setDataShow(result.data);
                    setAddList();
                } else {
                    return;
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
        if (info.activityVoList != null) {
            for (int i = 0; i < info.activityVoList.size(); i++) {
                ProjectApprovaLoglDetEnt.ActivityVoListBean bean = new ProjectApprovaLoglDetEnt.ActivityVoListBean();
                bean.id = info.activityVoList.get(i).id;
                bean.processInstanceId = info.activityVoList.get(i).processInstanceId;
                bean.processDefId = info.activityVoList.get(i).processDefId;
                bean.assignee = info.activityVoList.get(i).assignee;
                bean.name = info.activityVoList.get(i).name;
                bean.status = info.activityVoList.get(i).status;
                bean.comment = info.activityVoList.get(i).comment;
                bean.actType = info.activityVoList.get(i).actType;
                bean.actId = info.activityVoList.get(i).actId;
                bean.businessKey = info.activityVoList.get(i).businessKey;
                bean.createdDate = info.activityVoList.get(i).createdDate;
                bean.updatedDate = info.activityVoList.get(i).updatedDate;
                bean.userId = info.activityVoList.get(i).userId;
                bean.userName = info.activityVoList.get(i).userName;
                voListBeans.add(bean);
            }
            listAdapter.notifyDataSetChanged();
        }

    }


    private void setDataShow(LoanQueryDetEnt item) {
        if (item.personName != null) {
            if (item.personName.length() >= 3) {
                String strh = item.personName.substring(item.personName.length() - 2, item.personName.length());   //截取
                mBinding.nameTvBg.setText(strh);
            } else {
                mBinding.nameTvBg.setText(item.personName);
            }
        }
        String typename = VerifyUtils.isEmpty(item.type) ? "借款" : item.type.equals("1") ? "借款" : item.type.equals("2") ? "还款" : "";
        mBinding.userName.setText(item.personName + "申请" + typename);
//        String status = VerifyUtils.isEmpty(item.actStatus) ? "" : 1 == item.actStatus ? "待处理" : 2 == item.actStatus ? "已处理" :
//                3 == item.actStatus ? "抄送给我" : 4 == item.actStatus ? "已处理" : "";
        mBinding.actstatusTv.setText(item.actStatus);
//        mBinding.szbmType.setText("合同名称：" + item.contractName);
        mBinding.szbmType.setVisibility(View.GONE);
//        mBinding.projType.setText("合同类型：" + item.contractTypeName);
        mBinding.projType.setVisibility(View.GONE);
        mBinding.projNumber.setText("借款编号：" + item.borrower);
        mBinding.projName.setText("所在部门：" + item.deptName);
        mBinding.persionName.setText("使用时间：" + item.useDate);
        mBinding.appalyContentTv.setText("借款金额：" + item.amount);
        mBinding.dateTimeTv.setText("借款时间：" + item.createTime);
        mBinding.introductionTv.setText("借款事由：" + item.cause);
        switch (MyLoanActivity.mType) {
            case 1:
                mBinding.bnt1.setVisibility(View.GONE);
                if (item.returnStatus == 1) {
                    mBinding.bnt4.setVisibility(View.VISIBLE);
                    mBinding.bnt2.setVisibility(View.GONE);
                    mBinding.bnt3.setVisibility(View.GONE);
                } else {
                    mBinding.bnt2.setVisibility(View.VISIBLE);
                    mBinding.bnt3.setVisibility(View.VISIBLE);
                    mBinding.bnt4.setVisibility(View.GONE);
                }
                break;
            case 2:
                mBinding.bnt1.setVisibility(View.GONE);
                mBinding.bnt2.setVisibility(View.GONE);
                mBinding.bnt3.setVisibility(View.GONE);
                mBinding.bnt4.setVisibility(View.GONE);
                break;
            case 3:
                mBinding.bnt1.setVisibility(View.GONE);
                mBinding.bnt2.setVisibility(View.GONE);
                mBinding.bnt3.setVisibility(View.GONE);
                mBinding.bnt4.setVisibility(View.GONE);
                break;
            case 4:
                mBinding.bnt2.setVisibility(View.GONE);
                mBinding.bnt3.setVisibility(View.GONE);
                mBinding.newBuildBtn.setVisibility(View.VISIBLE);
                if (item.canCancelTask == 1) {
                    mBinding.bnt1.setVisibility(View.VISIBLE);
                } else {
                    mBinding.bnt1.setVisibility(View.GONE);
                }
                mBinding.bnt4.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }

    ProjectAppLogEnt appLogEnt;

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        if (info != null) {
            appLogEnt = new ProjectAppLogEnt();
            appLogEnt.processInstId = info.processInstId;
            appLogEnt.id = info.id;
        } else {
            return;
        }
        switch (v.getId()) {
            case R.id.bnt2: // 拒绝
                if (appLogEnt != null) {
                    auditAdd("2", AppConfig.status.value13, appLogEnt);
                }
                break;
            case R.id.bnt3:// 同意
                if (appLogEnt != null) {
                    auditAdd("1", AppConfig.status.value13, appLogEnt);
                }
                break;
            case R.id.bnt1:
                if (appLogEnt != null) {
                    auditAdd("3", AppConfig.status.value13, appLogEnt);
                }
                break;
            case R.id.bnt4:
                if (appLogEnt != null) {
                    bundle = new Bundle();
                    bundle.putString("processInstId", info.processInstId);
                    bundle.putString("id", info.id);
                    bundle.putString("amount", df.format(info.amount));
                    openActivity(ApplicationRepaymentActivity.class, bundle);
                }
                break;
            case R.id.new_build_btn:// 复制新建
                if (info != null) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", info);
                    openActivity(LoanApplicationActivity.class, bundle);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            getDetData();
        }

    }

    @Override
    protected void successTreatment() {
        super.successTreatment();
        setResult(200);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
