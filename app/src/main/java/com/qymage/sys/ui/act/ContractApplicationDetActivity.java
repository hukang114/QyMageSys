package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityContractApplicationBinding;
import com.qymage.sys.databinding.ActivityContractApplicationDetBinding;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.adapter.ProcessListAdapter;
import com.qymage.sys.ui.entity.AppColletionLoglDet;
import com.qymage.sys.ui.entity.ContractDetAddEnt;
import com.qymage.sys.ui.entity.ContractDetEnt;
import com.qymage.sys.ui.entity.ContractPayEnt;
import com.qymage.sys.ui.entity.FileListEnt;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.PaymentInfo;
import com.qymage.sys.ui.entity.ProjecInfoEnt;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.qymage.sys.ui.entity.ProjectApprovaLoglDetEnt;
import com.qymage.sys.ui.entity.ReceiverInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 合同详情
 */
public class ContractApplicationDetActivity extends BBActivity<ActivityContractApplicationDetBinding> implements View.OnClickListener {


    private String id;// 合同id
    private Intent mIntent;
    Bundle bundle;
    ContractDetEnt info;
    List<ProjectApprovaLoglDetEnt.ActivityVoListBean> voListBeans = new ArrayList<>();
    ProcessListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contract_application_det;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        id = mIntent.getStringExtra("id");
        if (id == null) {
            return;
        }
        mBinding.refuseTv.setOnClickListener(this);
        mBinding.agreeTv.setOnClickListener(this);
        mBinding.bnt1.setOnClickListener(this);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ProcessListAdapter(R.layout.item_list_process, voListBeans);
        mBinding.recyclerview.setAdapter(listAdapter);
        if (ChoiceContractLogActivity.mType == 1) {
            mBinding.bnt1.setVisibility(View.GONE);
            mBinding.refuseTv.setVisibility(View.VISIBLE);
            mBinding.agreeTv.setVisibility(View.VISIBLE);
        } else if (ChoiceContractLogActivity.mType == 4) {
            mBinding.bnt1.setVisibility(View.VISIBLE);
            mBinding.refuseTv.setVisibility(View.GONE);
            mBinding.agreeTv.setVisibility(View.GONE);
        } else {
            mBinding.bottomStateLayout.setVisibility(View.GONE);
        }


    }

    @Override
    protected void initData() {
        super.initData();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        showLoading();
        HttpUtil.expense_findById(hashMap).execute(new JsonCallback<Result<ContractDetEnt>>() {
            @Override
            public void onSuccess(Result<ContractDetEnt> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
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

    /**
     * 设置审批流程
     */
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


    private void setDataShow(ContractDetEnt item) {
        if (item.personName != null) {
            if (item.personName.length() >= 3) {
                String strh = item.personName.substring(item.personName.length() - 2, item.personName.length());   //截取
                mBinding.nameTvBg.setText(strh);
            } else {
                mBinding.nameTvBg.setText(item.personName);
            }
        }
        mBinding.userName.setText(item.personName + "申请");
        String status = VerifyUtils.isEmpty(item.actStatus) ? "" : 1 == item.actStatus ? "待处理" : 2 == item.actStatus ? "已处理" :
                3 == item.actStatus ? "抄送给我" : 4 == item.actStatus ? "已处理" : "";
        mBinding.actstatusTv.setText(status);
        mBinding.szbmType.setText("合同名称：" + item.contractName);
        mBinding.projType.setText("合同类型：" + item.contractTypeName);
        mBinding.projNumber.setText("项目编号：" + item.projectNo);
        mBinding.projName.setText("项目名称：" + item.projectName);
        mBinding.persionName.setText("付款方名称：" + item.payName);
        mBinding.appalyContentTv.setText("合同金额：" + item.amount);
        mBinding.dateTimeTv.setText("日期：" + item.date);
        mBinding.introductionTv.setText("付款备注：" + item.payRemark);

    }


    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        ProjectAppLogEnt appLogEnt = new ProjectAppLogEnt();
        appLogEnt.processInstId = info.processInstId;
        appLogEnt.id = info.id;
        switch (v.getId()) {
            case R.id.refuse_tv: // 拒绝
                auditAdd("2", AppConfig.status.value8, appLogEnt);
                break;

            case R.id.agree_tv:// 同意
                auditAdd("1", AppConfig.status.value8, appLogEnt);
                break;
            case R.id.bnt1:
                auditAdd("3", AppConfig.status.value8, appLogEnt);
                break;
        }
    }

    @Override
    protected void successTreatment() {
        super.successTreatment();
        setResult(200);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


}
