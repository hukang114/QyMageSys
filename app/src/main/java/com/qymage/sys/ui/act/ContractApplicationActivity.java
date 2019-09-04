package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityContractApplicationBinding;
import com.qymage.sys.ui.entity.ContractDetAddEnt;
import com.qymage.sys.ui.entity.ContractPayEnt;

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
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            listdata.clear();
            List<ContractDetAddEnt> list = (List<ContractDetAddEnt>) data.getSerializableExtra("data");
            listdata.addAll(list);
        }
    }
}
