package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityReceiverInfoBinding;
import com.qymage.sys.ui.entity.PaymentInfo;
import com.qymage.sys.ui.entity.ReceiverInfo;

import java.io.Serializable;


/**
 * 收款方信息 ,付款方信息
 */
public class ReceiverInfoActivity extends BBActivity<ActivityReceiverInfoBinding> {


    private Intent mIntent;
    private String type;
    ReceiverInfo receiverInfo;//收款方信息
    PaymentInfo paymentInfo; // 付款方信息
    private Bundle bundle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_receiver_info;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        type = mIntent.getStringExtra("type");
        if (type == null) {
            return;
        }
        mBinding.metitle.setrTxtClick(v -> {
            if (type.equals("1")) { // 收款信息
                receiverInfo.colName = mBinding.skfmcEdt.getText().toString();
                receiverInfo.colBank = mBinding.kfhEdt.getText().toString();
                receiverInfo.colAccount = mBinding.yhzhEdt.getText().toString();
                receiverInfo.colCreditCode = mBinding.yxdmEdt.getText().toString();
                receiverInfo.colInvoicePhone = mBinding.kpdhEdt.getText().toString();
                receiverInfo.colInvoiceAddress = mBinding.kpdzEdt.getText().toString();
                receiverInfo.colContacts = mBinding.lxrEdt.getText().toString();
                receiverInfo.colPhone = mBinding.lxdhEdt.getText().toString();
                bundle = new Bundle();
                bundle.putSerializable("data", receiverInfo);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(500, intent);
                finish();
            } else if (type.equals("2")) {// 付款信息
                paymentInfo.payName = mBinding.skfmcEdt.getText().toString();
                paymentInfo.payBank = mBinding.kfhEdt.getText().toString();
                paymentInfo.payAccount = mBinding.yhzhEdt.getText().toString();
                paymentInfo.payCreditCode = mBinding.yxdmEdt.getText().toString();
                paymentInfo.payInvoicePhone = mBinding.kpdhEdt.getText().toString();
                paymentInfo.payInvoiceAddress = mBinding.kpdzEdt.getText().toString();
                paymentInfo.payContacts = mBinding.lxrEdt.getText().toString();
                paymentInfo.payPhone = mBinding.lxdhEdt.getText().toString();
                bundle = new Bundle();
                bundle.putSerializable("data", paymentInfo);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(600, intent);
                finish();
            }

        });


    }


    @Override
    protected void initData() {
        super.initData();
        if (type.equals("1")) {
            mBinding.metitle.setcTxt(getResources().getString(R.string.skfxx_txt));
            receiverInfo = (ReceiverInfo) mIntent.getSerializableExtra("data");
            setSKInfiShow();
        } else if (type.equals("2")) {
            mBinding.metitle.setcTxt(getResources().getString(R.string.fkfxx_txt));
            mBinding.sfkNameTv.setText(getResources().getString(R.string.fkfxx_txt));
            mBinding.skfmcEdt.setHint(getResources().getString(R.string.fkfxx_txt));
            paymentInfo = (PaymentInfo) mIntent.getSerializableExtra("data");
            setFKInfiShow();
        }
    }

    /**
     * 设置付款信息显示
     */
    private void setFKInfiShow() {
        mBinding.skfmcEdt.setText(paymentInfo.payName);
        mBinding.kfhEdt.setText(paymentInfo.payBank);
        mBinding.yhzhEdt.setText(paymentInfo.payAccount);
        mBinding.yxdmEdt.setText(paymentInfo.payCreditCode);
        mBinding.kpdhEdt.setText(paymentInfo.payInvoicePhone);
        mBinding.kpdzEdt.setText(paymentInfo.payInvoiceAddress);
        mBinding.lxrEdt.setText(paymentInfo.payContacts);
        mBinding.lxdhEdt.setText(paymentInfo.payPhone);

    }


    /**
     * 设置收款信息显示
     */
    private void setSKInfiShow() {
        mBinding.skfmcEdt.setText(receiverInfo.colName);
        mBinding.kfhEdt.setText(receiverInfo.colBank);
        mBinding.yhzhEdt.setText(receiverInfo.colAccount);
        mBinding.yxdmEdt.setText(receiverInfo.colCreditCode);
        mBinding.kpdhEdt.setText(receiverInfo.colInvoicePhone);
        mBinding.kpdzEdt.setText(receiverInfo.colInvoiceAddress);
        mBinding.lxrEdt.setText(receiverInfo.colContacts);
        mBinding.lxdhEdt.setText(receiverInfo.colPhone);

    }
}
