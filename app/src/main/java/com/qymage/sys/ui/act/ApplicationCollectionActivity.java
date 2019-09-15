package com.qymage.sys.ui.act;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.http.LogUtils;
import com.qymage.sys.common.util.getPathUtil;
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
import com.qymage.sys.ui.entity.GetReceivedInfoEnt;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.PaymentInfo;
import com.qymage.sys.ui.entity.ProjecInfoEnt;
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
    List<ProjecInfoEnt> infoEnts = new ArrayList<>();// 更具合同编号或合同名称查询的信息集合
    List<String> proList = new ArrayList<>();// 合同编号 项目编号
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
    private String contractType;// 合同类型
    private String projectId;// 项目id
    public List<GetReceivedInfoEnt.CompanyMoneyTicketVOBean> companyMoneyTicketVO = new ArrayList<>();//历史收票开票明细
    public List<GetReceivedInfoEnt.CompanyMoneyPaymentVOBean> companyMoneyPaymentVO = new ArrayList<>();// 历史收付款明细


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
        mBinding.metitle.setrTxtClick(v -> {
            bundle = new Bundle();
            bundle.putString("type", type);
            openActivity(ApplicationCollectionLogActivity.class, bundle);
        });
        mBinding.skfxxEdt.setOnClickListener(this);
        mBinding.fkfxxEdt.setOnClickListener(this);
        mBinding.sprImg.setOnClickListener(this);
        mBinding.csrImg.setOnClickListener(this);
        mBinding.fujianImg.setOnClickListener(this);
        mBinding.yskTxt.setOnClickListener(this);
        mBinding.ykpTxt.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);
        mBinding.bzjjeEdt.setOnClickListener(this);
        mBinding.htlxCateTxt.setOnClickListener(this);
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
        mBinding.htbhEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getContract(1, mBinding.htbhEdt.getText().toString());
                return true;
            }
            return false;
        });
        mBinding.htmcEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getContract(2, mBinding.htbhEdt.getText().toString());
                return true;
            }
            return false;
        });
        mBinding.mxbhTv.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProjectNo(3, mBinding.mxbhTv.getText().toString());
                return true;
            }
            return false;
        });
        mBinding.xmmcEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProjectNo(4, mBinding.mxbhTv.getText().toString());
                return true;
            }
            return false;
        });

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
                mBinding.yskTxt.setHint("已付款");
                mBinding.wfkTv.setText("未付款");
                mBinding.bcsfksTv.setText("本次付款(元)");
                break;
            case "3":
                mBinding.metitle.setcTxt("开票申请");
                mBinding.bcsfksTv.setText("本次开票");
                break;
            case "4":
                mBinding.metitle.setcTxt("收票申请");
                mBinding.ykpTv.setText("已收票");
                mBinding.ykpTxt.setHint("已收票");
                mBinding.wkpTv.setText("未收票");
                mBinding.bcsfksTv.setText("本次收票");

                break;
        }
    }


    /**
     * 2.6根据合同编号/合同名称查询
     *
     * @param
     * @param contetn 内容
     */
    private void getContract(int cate, String contetn) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (cate == 1) {
            hashMap.put("contractNo", contetn); // 合同编号
        }
        if (cate == 2) {
            hashMap.put("contractName", contetn);//合同名称查询
        }
        showLoading();
        HttpUtil.getContract(hashMap).execute(new JsonCallback<Result<List<ProjecInfoEnt>>>() {
            @Override
            public void onSuccess(Result<List<ProjecInfoEnt>> result, Call call, Response response) {
                closeLoading();
                if (result.data != null && result.data.size() > 0) {
                    infoEnts.clear();
                    infoEnts.addAll(result.data);
                    if (cate == 1) {
                        proList.clear();
                        for (int i = 0; i < infoEnts.size(); i++) {
                            proList.add(infoEnts.get(i).contractNo);
                        }
                        setProDialog(cate, proList);
                    } else if (cate == 2) {
                        proList.clear();
                        for (int i = 0; i < infoEnts.size(); i++) {
                            proList.add(infoEnts.get(i).contractName);
                        }
                        setProDialog(cate, proList);
                    }
                } else {
                    showToast("未查询到合同编号或者合同名称");
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
     * 搜索项目编号或者项目名称
     */
    private void getProjectNo(int cate, String contnet) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (cate == 3) {
            hashMap.put("projectNo", contnet);//项目编号
        }
        if (cate == 4) {
            hashMap.put("projectName", contnet);//项目名称
        }
        if (type.equals("2") || type.equals("4")) {
            hashMap.put("bidType", type);
        }
        showLoading();
        HttpUtil.getProjectNo(hashMap).execute(new JsonCallback<Result<List<ProjecInfoEnt>>>() {
            @Override
            public void onSuccess(Result<List<ProjecInfoEnt>> result, Call call, Response response) {
                closeLoading();
                if (result.data != null && result.data.size() > 0) {
                    infoEnts.clear();
                    infoEnts.addAll(result.data);
                    if (cate == 3) {
                        proList.clear();
                        for (int i = 0; i < infoEnts.size(); i++) {
                            proList.add(infoEnts.get(i).projectNo);
                        }
                        setProDialog(cate, proList);
                    } else if (cate == 4) {
                        proList.clear();
                        for (int i = 0; i < infoEnts.size(); i++) {
                            proList.add(infoEnts.get(i).projectName);
                        }
                        setProDialog(cate, proList);
                    }
                } else {
                    showToast("尚未查询到项目编号或项目名称");
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
                closeLoading();
            }
        });

    }


    /**
     * 合同编号或者合同名称的选择
     *
     * @param cate
     * @param proList
     */
    private void setProDialog(int cate, List<String> proList) {

        String title = "请选择合同编号";
        if (cate == 1) {
            title = "请选择合同编号";
        } else if (cate == 2) {
            title = "请选择合同名称";
        } else if (cate == 3) {
            title = "请选择项目编号";
        } else if (cate == 4) {
            title = "请选择项目名称";
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            if (cate == 1 || cate == 2) {
                mBinding.htbhEdt.setText(infoEnts.get(options1).contractNo);
                mBinding.htmcEdt.setText(infoEnts.get(options1).contractName);
                mBinding.htlxCateTxt.setText(infoEnts.get(options1).contractTypeName);
                mBinding.mxbhTv.setText(infoEnts.get(options1).projectNo);
                mBinding.xmmcEdt.setText(infoEnts.get(options1).projectName);
                contractType = infoEnts.get(options1).contractType;
                projectId = infoEnts.get(options1).id;
                //按合同编号获取收付款/开票收票信息
                getMoney_Received(infoEnts.get(options1).contractNo);
            } else if (cate == 3 || cate == 4) {
                mBinding.mxbhTv.setText(infoEnts.get(options1).projectNo);
                mBinding.xmmcEdt.setText(infoEnts.get(options1).projectName);
                projectId = infoEnts.get(options1).id;
            }

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
    //------------------------------------------------------------------------------------------------


    /**
     * 按合同编号获取收付款/开票收票信息
     */
    private void getMoney_Received(String contractNo) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("contractNo", contractNo);
        hashMap.put("type", type);
        HttpUtil.money_getReceived(hashMap).execute(new JsonCallback<Result<GetReceivedInfoEnt>>() {
            @Override
            public void onSuccess(Result<GetReceivedInfoEnt> result, Call call, Response response) {
                if (result.data != null) {
                    if (result.data.companyMoneyPaymentVO != null) {
                        companyMoneyPaymentVO.clear();
                        companyMoneyPaymentVO.addAll(result.data.companyMoneyPaymentVO);
                    }
                    if (result.data.companyMoneyTicketVO != null) {
                        companyMoneyTicketVO.clear();
                        companyMoneyTicketVO.addAll(result.data.companyMoneyTicketVO);
                    }
                    mBinding.yskTxt.setText(df.format(result.data.endAmount));
                /*    if (result.data.endAmount != null) { // 已收款 已付款
                    } else {
                        mBinding.yskTxt.setText("0");
                    }*/
                    mBinding.wskEdt.setText(df.format(result.data.notAmount));
                  /*  if (result.data.notAmount != null) { // 未收款  未付款
                    } else {
                        mBinding.wskEdt.setText("0");
                    }*/
                    mBinding.ykpTxt.setText(df.format(result.data.endTicket));
                 /*   if (result.data.endTicket != null) {// 已开票  未开票
                    } else {
                        mBinding.ykpTxt.setText("0");
                    }*/
                    mBinding.wkpTxt.setText(df.format(result.data.notTicket));
                   /* if (result.data.notTicket != null) { //未开票  未收票
                    } else {
                        mBinding.wkpTxt.setText("0");
                    }*/
                    // 计算票款差额
                    mBinding.diffmoneyTxt.setText(df.format(result.data.endTicket - result.data.endAmount));

                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });


    }


    /**
     * 接口参数
     *
     * @return
     */
    private HashMap<String, Object> getPer() {
        // openType：string  100-收款  101-付款  102-开票  103-收票
        HashMap<String, Object> map = new HashMap<>();
        switch (type) {
            case "1":
                map.put("openType", "100");
                break;
            case "2":
                map.put("openType", "101");
                break;
            case "3":
                map.put("openType", "102");
                break;
            case "4":
                map.put("openType", "103");
                break;
        }
        map.put("projectId", projectId);
        map.put("projectNo", mBinding.mxbhTv.getText().toString());
        map.put("projectName", mBinding.xmmcEdt.getText().toString());
        map.put("contractType", contractType);
        map.put("contractNo", mBinding.htbhEdt.getText().toString());
        map.put("contractName", mBinding.htmcEdt.getText().toString());
        map.put("endAmount", mBinding.yskTxt.getText().toString());
        map.put("notAmount", mBinding.wskEdt.getText().toString());
        map.put("endTicket", mBinding.ykpTxt.getText().toString());
        map.put("notTicket", mBinding.wkpTxt.getText().toString());
        map.put("notTicket", mBinding.wkpTxt.getText().toString());
        map.put("diffMoney", mBinding.diffmoneyTxt.getText().toString());
        map.put("thisAmount", mBinding.bzjjeEdt.getText().toString());
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
                openFileChooseProcess();
                break;

            case R.id.ysk_txt:// 历史收-付-款明细
                if (companyMoneyPaymentVO.size() == 0) {
                    showToast("该合同暂无已收款或已付款数据");
                } else {
                    bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) companyMoneyPaymentVO);
                    bundle.putString("type", type);
                    openActivity(HistoryReceiptsPaymentsDetActivity.class, bundle);
                }
                break;

            case R.id.ykp_txt: // 历史已开票 已收票
                if (companyMoneyTicketVO.size() == 0) {
                    showToast("该合同暂无已开票或已收票数据");
                } else {
                    bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) companyMoneyTicketVO);
                    bundle.putString("type", type);
                    openActivity(HistoryInvoicedCollectActivity.class, bundle);
                }
                break;
            case R.id.bzjje_edt:// 本次收款 ，本次付款 ，本次开票，本次收票
                if (type.equals("1") || type.equals("2")) { //
                    bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) paymentVOS);
                    bundle.putString("type", type);
                    openActivity(ReceiptsPaymentsDetActivity.class, bundle);
                } else if (type.equals("3") || type.equals("4")) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) ticketVOS);
                    bundle.putString("type", type);
                    openActivity(InvoicedCollectActivity.class, bundle);
                }
                break;
            case R.id.save_btn:// 提交申请的参数
                if (isCheck()) {
                    subMitData();
                }
                break;
            case R.id.htlx_cate_txt:// 合同类型
                openActivity(ChoiceContractTypeActivity.class);
                break;

        }
    }


    private void subMitData() {
        showLoading();
        HttpUtil.inmoneyAdd(HttpConsts.INMONEYADD, getPer()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgNocanseDialogBuilder("申请提成功,查看记录", (dialog, which) -> {
                    dialog.dismiss();
                    bundle = new Bundle();
                    bundle.putString("type", type);
                    openActivity(ApplicationCollectionLogActivity.class, bundle);

                }).setCancelable(false).create().show();
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
     * 打开系统文件
     */
    private void openFileChooseProcess() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType(“image/*”);//选择图片
        //intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 0);
    }


    String path;

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
                mBinding.bzjjeEdt.setText(df.format(money));
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
                mBinding.bzjjeEdt.setText(df.format(money));
            }
        } else if (resultCode == AppConfig.num1000) {//选择合同类型的回传标识
            contractType = data.getStringExtra("value");
            mBinding.htlxCateTxt.setText(data.getStringExtra("title"));
        } else if (resultCode == RESULT_OK) {// 选择文件回调
            switch (requestCode) {
                case 0:
                    Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                    if ("file".equalsIgnoreCase(result.getScheme())) {//使用第三方应用打开
                        path = result.getPath();
                        showToast(path + "11111");
                        return;
                    }
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                        path = getPath(this, result);
                        showToast(path);
                    } else {//4.4以下下系统调用方法
                        path = getRealPathFromURI(result);
                        showToast(path + "222222");
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            showToast("取消选择");
        }
    }


    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }


    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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
            showToast("请填写已收款数据");
            return false;
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
            return true;
        }

    }


}
