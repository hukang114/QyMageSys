package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivityApplicationCollectionBinding;
import com.qymage.sys.ui.act.ReceiverInfoActivity;
import com.qymage.sys.ui.act.SelectionDepartmentActivity;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.entity.CompanyMoneyPaymentVOS;
import com.qymage.sys.ui.entity.CompanyMoneyTicketVOS;
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
 * 收款申请，付款申请 ，开票申请，收票申请
 */
public class ApplicationCollectionActivity extends BBActivity<ActivityApplicationCollectionBinding> implements View.OnClickListener {


    private Intent mIntent;
    private String type;

    List<CompanyMoneyPaymentVOS> paymentVOS = new ArrayList<>();// 收款 付款明细
    List<CompanyMoneyTicketVOS> ticketVOS = new ArrayList<>();// //开 -收 票明细
    List<FileListEnt> fileList = new ArrayList<>();// 上传附件
    List<GetTreeEnt> auditorList = new ArrayList<>();// 审核人
    List<GetTreeEnt> copierList = new ArrayList<>();// 抄送人
    AuditorListAdapter auditorListAdapter;// 审批人适配器
    CopierListAdapter copierListAdapter;// 抄送人适配器
    ReceiverInfo receiverInfo;//收款方信息
    PaymentInfo paymentInfo; // 付款方信息
    private Bundle bundle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_application_collection;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mIntent = getIntent();
        type = mIntent.getStringExtra("type");
        if (type == null) {
            return;
        }
        mBinding.skfxxEdt.setOnClickListener(this);
        mBinding.fkfxxEdt.setOnClickListener(this);
        mBinding.sprImg.setOnClickListener(this);
        mBinding.csrImg.setOnClickListener(this);
        mBinding.fujianImg.setOnClickListener(this);
        mBinding.yskTxt.setOnClickListener(this);
        mBinding.ykpTxt.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);
        LinearLayoutManager layouta = new LinearLayoutManager(this);
        layouta.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        LinearLayoutManager layoutb = new LinearLayoutManager(this);
        layoutb.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        LinearLayoutManager layoutc = new LinearLayoutManager(this);
        layoutc.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        mBinding.sprRecyclerview.setLayoutManager(layouta);
        mBinding.csrRecyclerview.setLayoutManager(layoutb);
        mBinding.fujianRecyclerview.setLayoutManager(layoutc);
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
        // 抄送人
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
        switch (type) {
            case "1":
                mBinding.metitle.setcTxt("收款申请");
                break;
            case "2":
                mBinding.metitle.setcTxt("付款申请");
                mBinding.yskTv.setText("已付款");
                mBinding.yskTxt.setText("已付款");
                mBinding.wfkTv.setText("未付款");
                break;
            case "3":
                mBinding.metitle.setcTxt("开票申请");
                break;
            case "4":
                mBinding.metitle.setcTxt("收票申请");
                mBinding.ykpTv.setText("已收票");
                mBinding.ykpTxt.setText("已收票");
                mBinding.wkpTv.setText("未收票");
                break;
        }
    }


    /**
     * 接口参数
     *
     * @return
     */
    private HashMap<String, Object> getPer() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("openType", type);
        map.put("projectId", mBinding.mxbhTv.getText().toString());
        map.put("projectName", mBinding.xmmcEdt.getText().toString());
        map.put("contractNo", mBinding.htbhEdt.getText().toString());
        map.put("contractName", mBinding.htmcEdt.getText().toString());
        map.put("endAmount", mBinding.yskTxt.getText().toString());
        map.put("notAmount", mBinding.wskEdt.getText().toString());
        map.put("endTicket", mBinding.ykpTxt.getText().toString());
        map.put("notTicket", mBinding.wkpTxt.getText().toString());
        map.put("notTicket", mBinding.wkpTxt.getText().toString());
        //  map.put("diffMoney", ""); // 没有次设计
        map.put("thisMoney", mBinding.bzjjeEdt.getText().toString());
        //收付款明细
        map.put("companyMoneyPaymentVOS", paymentVOS);
        //开收票明细
        map.put("companyMoneyTicketVOS", ticketVOS);
        //付款方信息↓
        map.put("payName", paymentInfo.payName);
        map.put("payBank", paymentInfo.payBank);
        map.put("payAccount", paymentInfo.payAccount);
        map.put("payCreditCode", paymentInfo.payCreditCode);
        map.put("payInvoicePhone", paymentInfo.payInvoicePhone);
        map.put("payInvoiceAddress", paymentInfo.payInvoiceAddress);
        map.put("payContacts", paymentInfo.payContacts);
        map.put("payPhone", paymentInfo.payPhone);
        //收款方信息
        map.put("colName", receiverInfo.colName);
        map.put("colBank", receiverInfo.colBank);
        map.put("colAccount", receiverInfo.colAccount);
        map.put("colCreditCode", receiverInfo.colCreditCode);
        map.put("colInvoicePhone", receiverInfo.colInvoicePhone);
        map.put("colInvoiceAddress", receiverInfo.colInvoiceAddress);
        map.put("colContacts", receiverInfo.colContacts);
        map.put("colPhone", receiverInfo.colPhone);
        //文件
        map.put("fileList", fileList);
        ////审核人
        map.put("auditor", auditorList);
        //抄送人
        map.put("copier", copierList);

        return map;
    }


    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

            case R.id.ysk_txt:// 收-付-款明细
                bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) paymentVOS);
                bundle.putString("type", type);
                openActivity(ReceiptsPaymentsDetActivity.class, bundle);
                break;

            case R.id.ykp_txt: // 已开票 已收票
                bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) ticketVOS);
                bundle.putString("type", type);
                openActivity(InvoicedCollectActivity.class, bundle);

                break;

            case R.id.save_btn:// 提交申请的参数
                if (isCheck()) {
                    subMitData();
                }

                break;


        }
    }


    private void subMitData() {
        showLoading();
        HttpUtil.inmoneyAdd(HttpConsts.INMONEYADD, getPer()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                showToast(result.message);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                msgDialog("申请提成功");
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) { // //收付款明细
            List<CompanyMoneyPaymentVOS> list = (List<CompanyMoneyPaymentVOS>) data.getSerializableExtra("data");
            paymentVOS.clear();
            paymentVOS.addAll(list);
            if (paymentVOS.size() > 0) {
                Double money = 0.0;
                for (int i = 0; i < paymentVOS.size(); i++) {
                    money = money + Double.parseDouble(paymentVOS.get(i).amount);
                }
                mBinding.yskTxt.setText(df.format(money));
            }

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
        } else if (resultCode == 700) {//开收票明细
            List<CompanyMoneyTicketVOS> list = (List<CompanyMoneyTicketVOS>) data.getSerializableExtra("data");
            ticketVOS.clear();
            ticketVOS.addAll(list);
            if (ticketVOS.size() > 0) {
                Double money = 0.0;
                for (int i = 0; i < ticketVOS.size(); i++) {
                    money = money + Double.parseDouble(ticketVOS.get(i).amount);
                }
                mBinding.ykpTxt.setText(df.format(money));
            }
        }
    }


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
        } else if (TextUtils.isEmpty(mBinding.htbhEdt.getText().toString())) {
            showToast("请填写合同编号");
            return false;
        } else if (TextUtils.isEmpty(mBinding.xmmcEdt.getText().toString())) {
            showToast("请填写项目名称");
            return false;
        } else if (TextUtils.isEmpty(mBinding.yskTxt.getText().toString())) {
            if (type.equals("1")) {
                showToast("请填写已收款数据");
                return false;
            } else if (type.equals("2")) {
                showToast("请填写已付款数据");
                return false;
            } else {
                showToast("请填写已收款数据");
                return false;
            }
        } else if (TextUtils.isEmpty(mBinding.wskEdt.getText().toString())) {
            showToast("请填写未收款或者未付款数据");
            return false;
        } else if (TextUtils.isEmpty(mBinding.ykpTxt.getText().toString())) {
            showToast("请填写已开票或者已收票");
            return false;
        } else if (TextUtils.isEmpty(mBinding.bzjjeEdt.getText().toString())) {
            showToast("请填写本次金额");
            return false;
        } else if (TextUtils.isEmpty(mBinding.bzjjeEdt.getText().toString())) {
            showToast("请填写本次金额");
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
