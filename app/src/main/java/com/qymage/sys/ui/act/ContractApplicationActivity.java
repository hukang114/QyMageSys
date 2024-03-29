package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
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
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.http.LogUtils;
import com.qymage.sys.common.util.DateUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityContractApplicationBinding;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.adapter.FileListAdapter;
import com.qymage.sys.ui.entity.ContractDetAddEnt;
import com.qymage.sys.ui.entity.ContractDetEnt;
import com.qymage.sys.ui.entity.ContractPayEnt;
import com.qymage.sys.ui.entity.FileListEnt;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.PaymentInfo;
import com.qymage.sys.ui.entity.ProjecInfoEnt;
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
 * 合同申请
 */
public class ContractApplicationActivity extends BBActivity<ActivityContractApplicationBinding> implements View.OnClickListener {


    List<ContractDetAddEnt> listdata = new ArrayList<>();// h合同明细
    List<ContractPayEnt> listbil = new ArrayList<>();// 付款比列

    List<GetTreeEnt> auditorList = new ArrayList<>();// 审核人
    List<GetTreeEnt> copierList = new ArrayList<>();// 抄送人

    List<FileListEnt> fileList = new ArrayList<>();// 上传附件
    FileListAdapter fileListAdapter;// 文件适配器

    AuditorListAdapter auditorListAdapter;// 审批人适配器
    CopierListAdapter copierListAdapter;// 抄送人适配器

    ReceiverInfo receiverInfo;//收款方信息
    PaymentInfo paymentInfo; // 付款方信息
    List<ProjecInfoEnt> infoEnts = new ArrayList<>();// 项目编号信息包含名称合同名称


    List<String> proList = new ArrayList<>();// 合同编号 项目编号

    List<String> proNumBer = new ArrayList<>();// 项目编号 项目名称

    private String projectId;// 项目id
    private String contractType;// 合同类型
    ContractDetEnt info;//通过合同详情新建传递过来的数据
    private Intent mIntent;


    Bundle bundle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contract_application;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.metitle.setrTxtClick(new View.OnClickListener() {
            @SingleClick(2000)
            @Override
            public void onClick(View v) {
                openActivity(ChoiceContractLogActivity.class);
            }
        });
        mIntent = getIntent();
        try {
            info = (ContractDetEnt) mIntent.getSerializableExtra("data");
        } catch (Exception e) {

        }
        mBinding.htmxEdt.setOnClickListener(this);
        mBinding.fkblsmEdt.setOnClickListener(this);
        mBinding.sprImg.setOnClickListener(this);
        mBinding.csrImg.setOnClickListener(this);
        mBinding.fujianImg.setOnClickListener(this);
        mBinding.skfxxEdt.setOnClickListener(this);
        mBinding.fkfxxEdt.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);
        mBinding.htrqEdt.setOnClickListener(this);
        mBinding.htlxCateTxt.setOnClickListener(this);
        LinearLayoutManager layouta = new LinearLayoutManager(this);
        layouta.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        LinearLayoutManager layoutb = new LinearLayoutManager(this);
        layoutb.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        mBinding.sprRecyclerview.setLayoutManager(layouta);
        mBinding.csrRecyclerview.setLayoutManager(layoutb);
        mBinding.fujianRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        // 审批人
        auditorListAdapter = new AuditorListAdapter(R.layout.item_list_auditor, auditorList);
        mBinding.sprRecyclerview.setAdapter(auditorListAdapter);
        auditorListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.caccel_ioc:
                    if (auditorList.get(position).isDef) {
                        showToast("默认审批人不能移除");
                    } else {
                        auditorList.remove(position);
                        auditorListAdapter.notifyDataSetChanged();
                    }
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
        // 文件适配器
        fileListAdapter = new FileListAdapter(R.layout.item_file_list, fileList);
        mBinding.fujianRecyclerview.setAdapter(fileListAdapter);
        fileListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.file_del_img:
                    fileList.remove(position);
                    fileListAdapter.notifyDataSetChanged();
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

        mBinding.xmbhEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProjectNo(1, mBinding.xmbhEdt.getText().toString());
             /*   if (!mBinding.xmbhEdt.getText().toString().equals("")) {
                } else {
                    showToast("请输入搜索关键字");
                }*/
                return true;
            }
            return false;
        });
        mBinding.xmmcEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProjectNo(2, mBinding.xmmcEdt.getText().toString());
              /*  if (!mBinding.xmbhEdt.getText().toString().equals("")) {
                } else {
                    showToast("请输入搜索关键字");
                }*/
                return true;
            }
            return false;
        });


    }

    @Override
    protected void initData() {
        super.initData();
        //添加合同默认审批人
        getAuditQuery(MainActivity.processDefId(AppConfig.btnType4));
        // 设置新建操作显示对应的数据
        if (info != null) {
            mBinding.xmbhEdt.setText(info.projectNo);
            mBinding.xmmcEdt.setText(info.projectName);
            projectId = info.projectId;
            contractType = info.contractType;
            mBinding.htlxCateTxt.setText(info.contractTypeName);
            mBinding.htbhEdt.setText(info.contractNo);
            mBinding.htmcEdt.setText(info.contractName);
            mBinding.htjeEdt.setText(df.format(info.amount));
            String date;
            if (info.date != null && info.date.length() > 10) {
                date = info.date.substring(0, 10);
            } else {
                date = DateUtil.formatNYR(System.currentTimeMillis());
            }
            mBinding.htrqEdt.setText(date);
            //  合同明细
            if (info.contractDetails != null && info.contractDetails.size() > 0) {
                listdata.clear();
                for (int i = 0; i < info.contractDetails.size(); i++) {
                    ContractDetAddEnt addEnt = new ContractDetAddEnt();
                    addEnt.amount = info.contractDetails.get(i).amount + "";
                    addEnt.taxes = info.contractDetails.get(i).taxes + "";
                    addEnt.taxRate = info.contractDetails.get(i).taxRate;
                    listdata.add(addEnt);
                }
            }
            mBinding.hkbzContent.setText(info.payRemark);
            // 付款比例
            if (info.contractPayscale != null && info.contractPayscale.size() > 0) {
                listbil.clear();
                for (int i = 0; i < info.contractPayscale.size(); i++) {
                    ContractPayEnt ent = new ContractPayEnt();
                    ent.date = info.contractPayscale.get(i).date;
                    ent.amount = info.contractPayscale.get(i).amount + "";
                    ent.payScale = info.contractPayscale.get(i).payScale;
                    listbil.add(ent);
                }
            }
            // 收款方信息
            receiverInfo.colName = VerifyUtils.isEmpty(info.colName) ? "" : info.colName;
            receiverInfo.colBank = VerifyUtils.isEmpty(info.colBank) ? "" : info.colBank;
            receiverInfo.colAccount = VerifyUtils.isEmpty(info.colAccount) ? "" : info.colAccount;
            receiverInfo.colCreditCode = VerifyUtils.isEmpty(info.colCreditCode) ? "" : info.colCreditCode;
            receiverInfo.colInvoicePhone = VerifyUtils.isEmpty(info.colInvoicePhone) ? "" : info.colInvoicePhone;
            receiverInfo.colInvoiceAddress = VerifyUtils.isEmpty(info.colInvoiceAddress) ? "" : info.colInvoiceAddress;
            receiverInfo.colContacts = VerifyUtils.isEmpty(info.colContacts) ? "" : info.colContacts;
            receiverInfo.colPhone = VerifyUtils.isEmpty(info.colPhone) ? "" : info.colPhone;
            // 付款放信息
            paymentInfo.payName = VerifyUtils.isEmpty(info.payName) ? "" : info.payName;
            paymentInfo.payBank = VerifyUtils.isEmpty(info.payBank) ? "" : info.payBank;
            paymentInfo.payAccount = VerifyUtils.isEmpty(info.payAccount) ? "" : info.payAccount;
            paymentInfo.payCreditCode = VerifyUtils.isEmpty(info.payCreditCode) ? "" : info.payCreditCode;
            paymentInfo.payInvoicePhone = VerifyUtils.isEmpty(info.payInvoicePhone) ? "" : info.payInvoicePhone;
            paymentInfo.payInvoiceAddress = VerifyUtils.isEmpty(info.payInvoiceAddress) ? "" : info.payInvoiceAddress;
            paymentInfo.payContacts = VerifyUtils.isEmpty(info.payContacts) ? "" : info.payContacts;
            paymentInfo.payPhone = VerifyUtils.isEmpty(info.payPhone) ? "" : info.payPhone;

            //  附件
            if (info.fileList != null && info.fileList.size() > 0) {
                fileList.clear();
                for (int i = 0; i < info.fileList.size(); i++) {
                    fileList.add(new FileListEnt(info.fileList.get(i).fileName, info.fileList.get(i).filePath));
                }
            }
            fileListAdapter.notifyDataSetChanged();
            setRecPayInfoShow();

        }

    }

    /**
     * 拿到默认的审批人
     *
     * @param listdata
     */
    @Override
    protected void getAuditQuerySuccess(List<GetTreeEnt> listdata) {
        // 加上默认审批人的标识
        for (int i = 0; i < listdata.size(); i++) {
            GetTreeEnt ent = new GetTreeEnt();
            ent.isDef = true; // 默认审批人
            ent.userId = listdata.get(i).userId;
            ent.userName = listdata.get(i).userName;
            auditorList.add(ent);
        }
        auditorListAdapter.notifyDataSetChanged();
    }

    /**
     * 搜索项目编号或者项目名称
     */
    private void getProjectNo(int type, String contnet) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (type == 1) {
            hashMap.put("projectNo", contnet);//项目编号
        }
        if (type == 2) {
            hashMap.put("projectName", contnet);//项目名称
        }
        hashMap.put("btnType", AppConfig.btnType4);
        showLoading();
        HttpUtil.getProjectNo(hashMap).execute(new JsonCallback<Result<List<ProjecInfoEnt>>>() {
            @Override
            public void onSuccess(Result<List<ProjecInfoEnt>> result, Call call, Response response) {
                closeLoading();
                if (result.data != null && result.data.size() > 0) {
                    infoEnts.clear();
                    infoEnts.addAll(result.data);
                    if (type == 1) {
                        proList.clear();
                        for (int i = 0; i < infoEnts.size(); i++) {
                            proList.add(infoEnts.get(i).projectNo);
                        }
                        setProDialog(type, proList);
                    } else if (type == 2) {
                        proList.clear();
                        for (int i = 0; i < infoEnts.size(); i++) {
                            proList.add(infoEnts.get(i).projectName);
                        }
                        setProDialog(type, proList);
                    }
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
     * 选择合同编号或者合同名称
     *
     * @param type
     * @param proList
     */
    private void setProDialog(int type, List<String> proList) {
        String title = "请选择项目编号";
        if (type == 1) {
            title = "请选择项目编号";
        } else if (type == 2) {
            title = "请选择项目名称";
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            mBinding.xmbhEdt.setText(infoEnts.get(options1).projectNo);
            mBinding.xmmcEdt.setText(infoEnts.get(options1).projectName);
            projectId = infoEnts.get(options1).id;
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
                if (TextUtils.isEmpty(mBinding.htjeEdt.getText().toString())) {
                    showToast("请输入合同金额");
                } else if (Double.parseDouble(mBinding.htjeEdt.getText().toString()) <= 0) {
                    showToast("请输入合同有效金额");
                } else {
                    bundle = new Bundle();
                    bundle.putString("htjeMoney", mBinding.htjeEdt.getText().toString());
                    bundle.putSerializable("data", (Serializable) listbil);
                    openActivity(ContractPaymentRaActivity.class, bundle);
                }
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
                openFileChoose();
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

            case R.id.htrq_edt:
                selectClickDate();
                break;
            case R.id.htlx_cate_txt:// 合同类型
                openActivity(ChoiceContractTypeActivity.class);
                break;
            case R.id.save_btn:
                if (isCheck()) {
                    subMitData();
                }
                break;
        }
    }

    /**
     * 拿到上传成功的文件数据
     *
     * @param fileListEnts
     * @return
     */
    @Override
    protected void updateFileSuccess(List<FileListEnt> fileListEnts) {
        fileList.addAll(fileListEnts);
        fileListAdapter.notifyDataSetChanged();
    }

    /**
     * 提交合同申请数据
     */
    private void subMitData() {
        showLoading();
        HttpUtil.contract_submit(getPar()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgNocanseDialogBuilder("合同申请提成功", (dialog, which) -> {
                    dialog.dismiss();
                    openActivity(ChoiceContractLogActivity.class);
                    finish();
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
     * 选择日期
     */
    private void selectClickDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String desc = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
            mBinding.htrqEdt.setText(desc);
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
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
            if (list.size() > 0) {
                if (auditorList.size() > 0) {
                    for (GetTreeEnt gteTar : list) {
                        boolean flag = false;
                        for (GetTreeEnt gte : auditorList) {
                            if (gteTar.userId.toString().trim().equals(gte.userId.toString().trim())) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            auditorList.add(gteTar);
                        }
                    }
                } else {
                    auditorList.clear();
                    auditorList.addAll(list);
                }
            }
            auditorListAdapter.notifyDataSetChanged();
        } else if (resultCode == 400) { // 抄送人
            List<GetTreeEnt> list = (List<GetTreeEnt>) data.getSerializableExtra("data");
            copierList.clear();
            copierList.addAll(list);
            copierListAdapter.notifyDataSetChanged();
        } else if (resultCode == 500) { // 获取收款方信息
            receiverInfo = (ReceiverInfo) data.getSerializableExtra("data");
        } else if (resultCode == 600) {// 获取付款方信息
            paymentInfo = (PaymentInfo) data.getSerializableExtra("data");
        } else if (resultCode == 700) { //付款比列
            listbil.clear();
            List<ContractPayEnt> list = (List<ContractPayEnt>) data.getSerializableExtra("data");
            listbil.addAll(list);
        } else if (resultCode == AppConfig.num1000) {//选择合同类型的回传标识
            getContractNo(data.getStringExtra("value"));
            contractType = data.getStringExtra("value");
            mBinding.htlxCateTxt.setText(data.getStringExtra("title"));
        }
        setRecPayInfoShow();
    }


    /**
     * 显示付款方名称和收款方名称 统计付款比例
     */
    private void setRecPayInfoShow() {
        //收款方名称
        if (receiverInfo != null && receiverInfo.colName != null) {
            mBinding.skfxxEdt.setText(receiverInfo.colName);
        }
        //付款方名称
        if (paymentInfo != null && paymentInfo.payName != null) {
            mBinding.fkfxxEdt.setText(paymentInfo.payName);
        }

        // 付款比例
        int sumpayScale = 0;
        if (listbil.size() > 0) {
            for (int i = 0; i < listbil.size(); i++) {
                // 付款比例
                sumpayScale += listbil.get(i).payScale;
            }
        }
        mBinding.fkblsmEdt.setText(sumpayScale + "%");

        if (sumpayScale > 100) {
            showToast("付款比例不能超过100%");
        }

        // 合同明细
        double sum = 0;
        if (listdata.size() > 0) {
            for (int i = 0; i < listdata.size(); i++) {
                if (listdata.get(i).amount != null && !listdata.get(i).amount.equals("")) {
                    sum += Double.parseDouble(listdata.get(i).amount);
                }
            }
        }
        mBinding.htmxEdt.setText(df.format(sum));

    }

    private double hkblMoney() {
        double sum = 0;
        if (listbil.size() > 0) {
            for (int i = 0; i < listbil.size(); i++) {
                // 付款比例总金额
                if (listbil.get(i).amount != null) {
                    sum += Double.parseDouble(listbil.get(i).amount);
                }
            }
        }
        return sum;
    }


    /**
     * 根据合同类型获取合同编号
     *
     * @param value
     */
    private void getContractNo(String value) {
        showLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("contractType", value);
        HttpUtil.getContractNo(hashMap).execute(new JsonCallback<Result<ProjecInfoEnt>>() {
            @Override
            public void onSuccess(Result<ProjecInfoEnt> result, Call call, Response response) {
                closeLoading();
                if (result.data != null && result.data.contractNo != null) {
                    mBinding.htbhEdt.setText(result.data.contractNo);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
            }
        });

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
        hashMap.put("date", mBinding.htrqEdt.getText().toString());
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
        if (TextUtils.isEmpty(mBinding.xmbhEdt.getText().toString())) {
            showToast("请填写项目编号");
            return false;
        } else if (TextUtils.isEmpty(mBinding.xmmcEdt.getText().toString())) {
            showToast("请填写项目名称");
            return false;
        } else if (TextUtils.isEmpty(mBinding.htlxCateTxt.getText().toString())) {
            showToast("请填写合同类型");
            return false;
        } else if (TextUtils.isEmpty(mBinding.htbhEdt.getText().toString())) {
            showToast("请填写合同编号");
            return false;
        } else if (TextUtils.isEmpty(mBinding.htmcEdt.getText().toString())) {
            showToast("请填写合同名称");
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
        } else if (Double.parseDouble(mBinding.htjeEdt.getText().toString()) != Double.parseDouble(mBinding.htmxEdt.getText().toString())) {
            showToast("合同金额与合同明细金额不一致");
            return false;
        } else if (Integer.parseInt(mBinding.fkblsmEdt.getText().toString().replace("%", "")) > 100) {
            showToast("付款比例不能超过100%");
            return false;
        } else if (Double.parseDouble(mBinding.htjeEdt.getText().toString()) != hkblMoney()) {
            showToast("合同金额与付款总比例的金额不一致");
            return false;
        } else if (receiverInfo.colName == null || receiverInfo.colName.equals("")) {
            showToast("请填写收款方信息");
            return false;
        } else if (paymentInfo.payName == null || paymentInfo.payName.equals("")) {
            showToast("请填写付款方信息");
            return false;
        } else if (auditorList.size() == 0) {
            showToast("请选择审批人");
            return false;
        } else {
            return true;
        }

    }


}
