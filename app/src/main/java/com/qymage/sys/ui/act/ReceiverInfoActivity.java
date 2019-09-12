package com.qymage.sys.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivityReceiverInfoBinding;
import com.qymage.sys.ui.entity.GetCompanyInfoEnt;
import com.qymage.sys.ui.entity.PaymentInfo;
import com.qymage.sys.ui.entity.ReceiverInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 收款方信息 ,付款方信息
 */
public class ReceiverInfoActivity extends BBActivity<ActivityReceiverInfoBinding> {


    private Intent mIntent;
    private String type;
    ReceiverInfo receiverInfo;//收款方信息
    PaymentInfo paymentInfo; // 付款方信息
    private Bundle bundle;
    List<GetCompanyInfoEnt> companyInfoEnts = new ArrayList<>();//单位信息
    List<String> proList = new ArrayList<>();//

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
        mBinding.skfmcEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getCompanyInfo(mBinding.skfmcEdt.getText().toString());
                return true;
            }
            return false;
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
     * 搜索单位名称（包含银行账户 开户行 信用代码等...）
     */
    private void getCompanyInfo(String companyName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("companyName", companyName);
        showLoading();
        HttpUtil.getCompanyInfo(hashMap).execute(new JsonCallback<Result<List<GetCompanyInfoEnt>>>() {
            @Override
            public void onSuccess(Result<List<GetCompanyInfoEnt>> result, Call call, Response response) {
                if (result.data != null && result.data.size() > 0) {
                    closeLoading();
                    companyInfoEnts.clear();
                    companyInfoEnts.addAll(result.data);
                    proList.clear();
                    for (int i = 0; i < companyInfoEnts.size(); i++) {
                        proList.add(companyInfoEnts.get(i).name);
                    }
                    setProDialog(proList);
                } else {
                    showToast("暂未搜索到相关内容");
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
     * 选择合同编号或者合同名称 公司名称
     *
     * @param proList
     */
    private void setProDialog(List<String> proList) {
        String title = "请选择项收款单位";
        if (type.equals("1")) {
            title = "请选择项收款单位";
        } else if (type.equals("2")) {
            title = "请选择项付款单位";
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            mBinding.skfmcEdt.setText(companyInfoEnts.get(options1).name);
            mBinding.kfhEdt.setText(companyInfoEnts.get(options1).bank);
            mBinding.yhzhEdt.setText(companyInfoEnts.get(options1).account);
            mBinding.yxdmEdt.setText(companyInfoEnts.get(options1).creditCode);
            mBinding.kpdhEdt.setText(companyInfoEnts.get(options1).invoicePhone);
            mBinding.kpdzEdt.setText(companyInfoEnts.get(options1).invoiceAddress);
            mBinding.lxrEdt.setText(companyInfoEnts.get(options1).contacts);
            mBinding.lxdhEdt.setText(companyInfoEnts.get(options1).phone);
        })
                .setTitleText(title)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(18)
                .setOutSideCancelable(false)//点击外部dismiss default true
                .setContentTextSize(16)//滚轮文字大小
                .setSubCalSize(16)//确定和取消文字大小
                .setLineSpacingMultiplier(2.4f)
                .build();
        // 三级选择器
        pvOptions.setPicker(proList, null, null);
        pvOptions.show();
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
