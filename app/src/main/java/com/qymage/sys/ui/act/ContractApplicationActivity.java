package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityContractApplicationBinding;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.entity.ContractDetAddEnt;
import com.qymage.sys.ui.entity.ContractPayEnt;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.PaymentInfo;
import com.qymage.sys.ui.entity.ReceiverInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.leo.click.SingleClick;


/**
 * 合同申请
 */
public class ContractApplicationActivity extends BBActivity<ActivityContractApplicationBinding> implements View.OnClickListener {


    List<ContractDetAddEnt> listdata = new ArrayList<>();// h合同明细
    List<ContractPayEnt> listbil = new ArrayList<>();// 付款比列

    List<GetTreeEnt> auditorList = new ArrayList<>();// 审核人
    List<GetTreeEnt> copierList = new ArrayList<>();// 抄送人

    AuditorListAdapter auditorListAdapter;// 审批人适配器
    CopierListAdapter copierListAdapter;// 抄送人适配器

    ReceiverInfo receiverInfo;//收款方信息
    PaymentInfo paymentInfo; // 付款方信息


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
        }
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
}
