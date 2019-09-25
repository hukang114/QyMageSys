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
import com.qymage.sys.common.util.DateUtil;
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
    ReceiverInfo receiverInfo;//收款方信息
    PaymentInfo paymentInfo; // 付款方信息
    List<ContractDetAddEnt> listdata = new ArrayList<>();// h合同明细
    List<ContractPayEnt> listbil = new ArrayList<>();// 付款比列
    List<FileListEnt> fileList = new ArrayList<>();// 上传附件


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
        mBinding.fkMxTv.setOnClickListener(this);
        mBinding.skMxTv.setOnClickListener(this);
        mBinding.contractdetailsBtn.setOnClickListener(this);
        mBinding.contractpayscaleBtn.setOnClickListener(this);
        mBinding.fileListBtn.setOnClickListener(this);
        mBinding.newBuildBtn.setOnClickListener(this);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ProcessListAdapter(R.layout.item_list_process, voListBeans);
        mBinding.recyclerview.setAdapter(listAdapter);


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
        mBinding.userName.setText(item.personName + "提交合同申请");
        mBinding.actstatusTv.setText(item.actStatus);
        mBinding.szbmType.setText("合同名称：" + item.contractName);
        mBinding.projType.setText("合同类型：" + item.contractTypeName);
        mBinding.projNumber.setText("项目编号：" + item.projectNo);
        mBinding.projName.setText("项目名称：" + item.projectName);
        mBinding.persionName.setText("付款方名称：" + item.payName);
        mBinding.colnameTv.setText("收款方名称：" + item.colName);
        mBinding.contractdetailsTv.setText("合同明细：" + df.format(contDetNum()) + "元");
        mBinding.contractpayscaleTv.setText("付款比例：" + df.format(Payscale()) + "元");
        if (item.fileList != null) {
            mBinding.fileListTv.setText("合同附件" + item.fileList.size() + "个");
        } else {
            mBinding.fileListTv.setText("合同附件");
        }
        mBinding.appalyContentTv.setText("合同金额：" + item.amount);
        mBinding.dateTimeTv.setText("合同日期：" + item.date);
        mBinding.createtimeTv.setText("申请日期：" + DateUtil.formatMillsTo(item.createTime));
        mBinding.introductionTv.setText("付款备注：" + item.payRemark);

        if (ChoiceContractLogActivity.mType == 1) {
            mBinding.bnt1.setVisibility(View.GONE);
            mBinding.refuseTv.setVisibility(View.VISIBLE);
            mBinding.agreeTv.setVisibility(View.VISIBLE);
            mBinding.newBuildBtn.setVisibility(View.GONE);
        } else if (ChoiceContractLogActivity.mType == 4) {
            mBinding.refuseTv.setVisibility(View.GONE);
            mBinding.agreeTv.setVisibility(View.GONE);
            if (item.canCancelTask == 1) {
                mBinding.bnt1.setVisibility(View.VISIBLE);
            } else {
                mBinding.bnt1.setVisibility(View.GONE);
            }
        } else {
            mBinding.bottomStateLayout.setVisibility(View.GONE);
        }

    }


    private double Payscale() {
        double num = 0;
        if (info != null && info.contractPayscale != null && info.contractPayscale.size() > 0) {
            for (int i = 0; i < info.contractPayscale.size(); i++) {
                num += info.contractPayscale.get(i).amount;
            }
        }
        return num;

    }

    /**
     * 统计合同明细
     *
     * @return
     */
    private double contDetNum() {
        double num = 0;
        if (info != null && info.contractDetails != null && info.contractDetails.size() > 0) {
            for (int i = 0; i < info.contractDetails.size(); i++) {
                num += info.contractDetails.get(i).amount;
            }
        }
        return num;
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
            case R.id.fk_mx_tv:// 查看付款方信息
                if (info != null) {
                    paymentInfo = new PaymentInfo();
                    paymentInfo.payName = VerifyUtils.isEmpty(info.payName) ? "" : info.payName;
                    paymentInfo.payBank = VerifyUtils.isEmpty(info.payBank) ? "" : info.payBank;
                    paymentInfo.payAccount = VerifyUtils.isEmpty(info.payAccount) ? "" : info.payAccount;
                    paymentInfo.payCreditCode = VerifyUtils.isEmpty(info.payCreditCode) ? "" : info.payCreditCode;
                    paymentInfo.payInvoicePhone = VerifyUtils.isEmpty(info.payInvoicePhone) ? "" : info.payInvoicePhone;
                    paymentInfo.payInvoiceAddress = VerifyUtils.isEmpty(info.payInvoiceAddress) ? "" : info.payInvoiceAddress;
                    paymentInfo.payContacts = VerifyUtils.isEmpty(info.payContacts) ? "" : info.payContacts;
                    paymentInfo.payPhone = VerifyUtils.isEmpty(info.payPhone) ? "" : info.payPhone;
                    bundle = new Bundle();
                    bundle.putString("type_det", "det");
                    bundle.putString("type", "2");
                    bundle.putSerializable("data", paymentInfo);
                    openActivity(ReceiverInfoActivity.class, bundle);
                } else {
                    showToast("尚未获取到数据");
                }
                break;
            case R.id.sk_mx_tv:// 查看收款放信息
                if (info != null) {
                    receiverInfo = new ReceiverInfo();
                    receiverInfo.colName = VerifyUtils.isEmpty(info.colName) ? "" : info.colName;
                    receiverInfo.colBank = VerifyUtils.isEmpty(info.colBank) ? "" : info.colBank;
                    receiverInfo.colAccount = VerifyUtils.isEmpty(info.colAccount) ? "" : info.colAccount;
                    receiverInfo.colCreditCode = VerifyUtils.isEmpty(info.colCreditCode) ? "" : info.colCreditCode;
                    receiverInfo.colInvoicePhone = VerifyUtils.isEmpty(info.colInvoicePhone) ? "" : info.colInvoicePhone;
                    receiverInfo.colInvoiceAddress = VerifyUtils.isEmpty(info.colInvoiceAddress) ? "" : info.colInvoiceAddress;
                    receiverInfo.colContacts = VerifyUtils.isEmpty(info.colContacts) ? "" : info.colContacts;
                    receiverInfo.colPhone = VerifyUtils.isEmpty(info.colPhone) ? "" : info.colPhone;
                    bundle.putString("type_det", "det");
                    bundle.putString("type", "1");
                    bundle.putSerializable("data", receiverInfo);
                    openActivity(ReceiverInfoActivity.class, bundle);
                } else {
                    showToast("尚未获取到数据");
                }
                break;
            case R.id.contractdetails_btn://合同明细
                if (info != null && info.contractDetails != null && info.contractDetails.size() > 0) {
                    listdata.clear();
                    for (int i = 0; i < info.contractDetails.size(); i++) {
                        ContractDetAddEnt addEnt = new ContractDetAddEnt();
                        addEnt.amount = info.contractDetails.get(i).amount + "";
                        addEnt.taxes = info.contractDetails.get(i).taxes + "";
                        addEnt.taxRate = info.contractDetails.get(i).taxRate;
                        listdata.add(addEnt);
                    }
                    bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) listdata);
                    bundle.putString("type_det", "det");
                    openActivity(ContractDetailsAddActivity.class, bundle);
                } else {
                    showToast("暂无合同明细数据");
                }
                break;
            case R.id.contractpayscale_btn: // 付款比例
                if (info != null && info.contractPayscale != null && info.contractPayscale.size() > 0) {
                    listbil.clear();
                    for (int i = 0; i < info.contractPayscale.size(); i++) {
                        ContractPayEnt ent = new ContractPayEnt();
                        ent.date = info.contractPayscale.get(i).date;
                        ent.amount = info.contractPayscale.get(i).amount + "";
                        ent.payScale = info.contractPayscale.get(i).payScale;
                        listbil.add(ent);
                    }
                    bundle = new Bundle();
                    bundle.putString("type_det", "det");
                    bundle.putSerializable("data", (Serializable) listbil);
                    openActivity(ContractPaymentRaActivity.class, bundle);
                } else {
                    showToast("暂无付款比例数据");
                }
                break;
            case R.id.file_list_btn:
                if (info != null && info.fileList != null && info.fileList.size() > 0) {
                    fileList.clear();
                    for (int i = 0; i < info.fileList.size(); i++) {
                        fileList.add(new FileListEnt(info.fileList.get(i).fileName, info.fileList.get(i).filePath));
                    }
                    bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) fileList);
                    openActivity(ListAttachmentsActivity.class, bundle);
                } else {
                    showToast("该合同暂无上传附件");
                }
                break;
            case R.id.new_build_btn:// 新建
                if (info != null) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", info);
                    openActivity(ContractApplicationActivity.class, bundle);
                } else {
                    showToast("尚未获取到数据");
                }
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
