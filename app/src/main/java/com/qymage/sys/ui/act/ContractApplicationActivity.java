package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivityContractApplicationBinding;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.entity.ContractDetAddEnt;
import com.qymage.sys.ui.entity.ContractPayEnt;
import com.qymage.sys.ui.entity.FileListEnt;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.PaymentInfo;
import com.qymage.sys.ui.entity.ReceiverInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 合同申请
 */
public class ContractApplicationActivity extends BBActivity<ActivityContractApplicationBinding> implements View.OnClickListener {


    List<ContractDetAddEnt> listdata = new ArrayList<>();// h合同明细
    List<ContractPayEnt> listbil = new ArrayList<>();// 付款比列

    List<GetTreeEnt> auditorList = new ArrayList<>();// 审核人
    List<GetTreeEnt> copierList = new ArrayList<>();// 抄送人

    List<FileListEnt> fileList = new ArrayList<>();// 上传附件

    AuditorListAdapter auditorListAdapter;// 审批人适配器
    CopierListAdapter copierListAdapter;// 抄送人适配器

    ReceiverInfo receiverInfo;//收款方信息
    PaymentInfo paymentInfo; // 付款方信息
    private String projectId;// 项目id
    private String contractType;// 合同类型
    private String contractNo;


    Bundle bundle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contract_application;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.htmxEdt.setOnClickListener(this);
        mBinding.fkblsmEdt.setOnClickListener(this);
        mBinding.sprImg.setOnClickListener(this);
        mBinding.csrImg.setOnClickListener(this);
        mBinding.fujianImg.setOnClickListener(this);
        mBinding.skfxxEdt.setOnClickListener(this);
        mBinding.fkfxxEdt.setOnClickListener(this);
        LinearLayoutManager layouta = new LinearLayoutManager(this);
        layouta.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        LinearLayoutManager layoutb = new LinearLayoutManager(this);
        layoutb.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        mBinding.sprRecyclerview.setLayoutManager(layouta);
        mBinding.csrRecyclerview.setLayoutManager(layoutb);
        // 审批人
        auditorListAdapter = new AuditorListAdapter(R.layout.item_list_auditor, auditorList);
        mBinding.sprRecyclerview.setAdapter(auditorListAdapter);
        auditorListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.caccel_ioc:
                    auditorList.remove(position);
                    auditorListAdapter.notifyDataSetChanged();
                    break;
            }
        });

        copierListAdapter = new CopierListAdapter(R.layout.item_list_auditor, copierList);
        mBinding.csrRecyclerview.setAdapter(copierListAdapter);
        copierListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.caccel_ioc:
                    copierList.remove(position);
                    copierListAdapter.notifyDataSetChanged();
                    break;
            }
        });
        receiverInfo = new ReceiverInfo(
                ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , "");

        paymentInfo = new PaymentInfo(
                ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , "");


    }

    @Override
    protected void initData() {
        super.initData();
        getProjectNo();

    }

    /**
     * 合同编号
     */
    private void getProjectNo() {
        HttpUtil.getProjectNo(HttpConsts.getProjectNo, new HashMap<>()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                getProjectName(result.data);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });

    }


    /**
     * @param projectNo 更具编号查询名称
     */
    private void getProjectName(String projectNo) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("projectNo", projectNo);
        HttpUtil.getProject(HttpConsts.getProject, map).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {

            }
        });


    }


    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.htmx_edt:// 合同明细
                bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) listdata);
                openActivity(ContractDetailsAddActivity.class, bundle);

                break;
            case R.id.fkblsm_edt:// 付款比列
                bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) listbil);
                openActivity(ContractPaymentRaActivity.class, bundle);
                break;
            case R.id.spr_img:// 添加审批人
                bundle = new Bundle();
                bundle.putString("type", "1");
                openActivity(SelectionDepartmentActivity.class, bundle);
                break;

            case R.id.csr_img:// 添加抄送人
                bundle = new Bundle();
                bundle.putString("type", "2");
                openActivity(SelectionDepartmentActivity.class, bundle);
                break;
            case R.id.fujian_img:// 添加附件

                break;
            case R.id.skfxx_edt:// 收款方信息
                bundle = new Bundle();
                bundle.putString("type", "1");
                bundle.putSerializable("data", receiverInfo);
                openActivity(ReceiverInfoActivity.class, bundle);
                break;
            case R.id.fkfxx_edt:// 付款方信息
                bundle = new Bundle();
                bundle.putString("type", "2");
                bundle.putSerializable("data", paymentInfo);
                openActivity(ReceiverInfoActivity.class, bundle);
                break;
            case R.id.save_btn:
                if (isCheck()) {
                    subMitData();
                }
                break;
        }
    }

    /**
     * 提交合同申请数据
     */
    private void subMitData() {
        showLoading();
        HttpUtil.contract_submit(HttpConsts.CONTRACT_SUBMIT, getPar()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgDialog("合同申请提成功");
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                showToast(e.getMessage());
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) { // 合同明细
            listdata.clear();
            List<ContractDetAddEnt> list = (List<ContractDetAddEnt>) data.getSerializableExtra("data");
            listdata.addAll(list);
        } else if (resultCode == 300) { // 审核人
            List<GetTreeEnt> list = (List<GetTreeEnt>) data.getSerializableExtra("data");
            auditorList.clear();
            auditorList.addAll(list);
            auditorListAdapter.notifyDataSetChanged();
        } else if (resultCode == 400) { // 抄送人
            List<GetTreeEnt> list = (List<GetTreeEnt>) data.getSerializableExtra("data");
            copierList.clear();
            copierList.addAll(list);
            copierListAdapter.notifyDataSetChanged();
        } else if (resultCode == 500) { // 获取收款方信息
            receiverInfo = (ReceiverInfo) data.getSerializableExtra("data");
            showToast(receiverInfo.colName);
        } else if (resultCode == 600) {// 获取付款方信息
            paymentInfo = (PaymentInfo) data.getSerializableExtra("data");
            showToast(paymentInfo.payName);
        } else if (resultCode == 700) { //付款比列
            listbil.clear();
            List<ContractPayEnt> list = (List<ContractPayEnt>) data.getSerializableExtra("data");
            listbil.addAll(list);
        }
    }


    /**
     * 合同申请参数
     *
     * @return
     */
    private HashMap<String, Object> getPar() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("projectId", projectId);
        hashMap.put("contractType", contractType);
        hashMap.put("contractNo", mBinding.htbhEdt.getText().toString());
        hashMap.put("contractName", mBinding.htmcEdt.getText().toString());
        hashMap.put("amount", mBinding.htjeEdt.getText().toString());
        hashMap.put("amountName", "");
        hashMap.put("date", mBinding.htrqEdt.getText().toString());
        hashMap.put("endDate", "");
        hashMap.put("payRemark", mBinding.hkbzContent.getText().toString());
        hashMap.put("contractDetails", listdata);
        hashMap.put("contractPayscale", listbil);
        //付款方信息↓
        hashMap.put("payName", paymentInfo.payName);
        hashMap.put("payBank", paymentInfo.payBank);
        hashMap.put("payAccount", paymentInfo.payAccount);
        hashMap.put("payCreditCode", paymentInfo.payCreditCode);
        hashMap.put("payInvoicePhone", paymentInfo.payInvoicePhone);
        hashMap.put("payInvoiceAddress", paymentInfo.payInvoiceAddress);
        hashMap.put("payContacts", paymentInfo.payContacts);
        hashMap.put("payPhone", paymentInfo.payPhone);
        //收款方信息
        hashMap.put("colName", receiverInfo.colName);
        hashMap.put("colBank", receiverInfo.colBank);
        hashMap.put("colAccount", receiverInfo.colAccount);
        hashMap.put("colCreditCode", receiverInfo.colCreditCode);
        hashMap.put("colInvoicePhone", receiverInfo.colInvoicePhone);
        hashMap.put("colInvoiceAddress", receiverInfo.colInvoiceAddress);
        hashMap.put("colContacts", receiverInfo.colContacts);
        hashMap.put("colPhone", receiverInfo.colPhone);
        hashMap.put("fileList", fileList);
        hashMap.put("auditor", auditorList);
        hashMap.put("copier", copierList);
        return hashMap;


    }


    /**
     * 检测参数
     *
     * @return
     */
    private boolean isCheck() {
        if (TextUtils.isEmpty(mBinding.htbhEdt.getText().toString())) {
            showToast("请填写合同编号");
            return false;
        } else if (TextUtils.isEmpty(mBinding.htmcEdt.getText().toString())) {
            showToast("请填写合同名称");
            return false;
        } else if (TextUtils.isEmpty(mBinding.htlxCateTxt.getText().toString())) {
            showToast("请填写合同类型");
            return false;
        } else if (TextUtils.isEmpty(mBinding.xmbhEdt.getText().toString())) {
            showToast("请填写项目编号");
            return false;
        } else if (TextUtils.isEmpty(mBinding.xmmcEdt.getText().toString())) {
            showToast("请填写项目名称");
            return false;
        } else if (TextUtils.isEmpty(mBinding.htjeEdt.getText().toString())) {
            showToast("请填合同金额");
            return false;
        } else if (TextUtils.isEmpty(mBinding.htrqEdt.getText().toString())) {
            showToast("请填合同日期");
            return false;
        } else if (TextUtils.isEmpty(mBinding.hkbzContent.getText().toString())) {
            showToast("请填付款备注");
            return false;
        } else if (listdata.size() == 0) {
            showToast("请填写合同明细");
            return false;
        } else if (TextUtils.isEmpty(mBinding.hkbzContent.getText().toString())) {
            showToast("请填付款备注");
            return false;
        } else if (listbil.size() == 0) {
            showToast("请填写付款比例");
            return false;
        } else if (receiverInfo.colName == null) {
            showToast("请填写收款方信息");
            return false;
        } else if (paymentInfo.payName == null) {
            showToast("请填写付款方信息");
            return false;
        } else if (auditorList.size() == 0) {
            showToast("请选择审批人");
            return false;
        } else if (copierList.size() == 0) {
            showToast("请选择抄送人");
            return false;
        } else {
            return false;
        }

    }


}
