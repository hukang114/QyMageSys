package com.qymage.sys.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.DateUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityAppcolletionloglDetBinding;
import com.qymage.sys.databinding.ActivityApplicationCollectionLogBinding;
import com.qymage.sys.databinding.ActivityBidperformtblyszloglDetBinding;
import com.qymage.sys.ui.adapter.ProcessListAdapter;
import com.qymage.sys.ui.entity.AppColletionLoglDet;
import com.qymage.sys.ui.entity.BidPerFormDetEnt;
import com.qymage.sys.ui.entity.CompanyMoneyPaymentVOS;
import com.qymage.sys.ui.entity.CompanyMoneyTicketVOS;
import com.qymage.sys.ui.entity.FileListEnt;
import com.qymage.sys.ui.entity.GetReceivedInfoEnt;
import com.qymage.sys.ui.entity.PaymentInfo;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.qymage.sys.ui.entity.ProjectApprovaLoglDetEnt;
import com.qymage.sys.ui.entity.ReceiverInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 收付款 支-收  开票 支-收详情 收款详情 -付款申请详情
 */
public class AppColletionLoglDetActivity extends BBActivity<ActivityAppcolletionloglDetBinding> implements View.OnClickListener {


    private String Tag;
    private String id;
    private Intent mIntent;
    AppColletionLoglDet info;
    private String bidType;
    List<ProjectApprovaLoglDetEnt.ActivityVoListBean> voListBeans = new ArrayList<>();
    ProcessListAdapter listAdapter;
    Bundle bundle;
    ReceiverInfo receiverInfo;//收款方信息
    PaymentInfo paymentInfo; // 付款方信息
    List<FileListEnt> fileList = new ArrayList<>();// 上传附件
    public List<GetReceivedInfoEnt.CompanyMoneyTicketVOBean> companyMoneyTicketVO = new ArrayList<>();//历史收票开票明细
    public List<GetReceivedInfoEnt.CompanyMoneyPaymentVOBean> companyMoneyPaymentVO = new ArrayList<>();// 历史收付款明细
    List<CompanyMoneyPaymentVOS> paymentVOS = new ArrayList<>();// 本次收款 付款明细
    List<CompanyMoneyTicketVOS> ticketVOS = new ArrayList<>();// //本次开 -收 票明细


    @Override
    protected int getLayoutId() {
        return R.layout.activity_appcolletionlogl_det;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        Tag = mIntent.getStringExtra("Tag");
        bidType = mIntent.getStringExtra("type");
        id = mIntent.getStringExtra("id");
        mBinding.refuseTv.setOnClickListener(this);
        mBinding.agreeTv.setOnClickListener(this);
        mBinding.fkMxTv.setOnClickListener(this);
        mBinding.skMxTv.setOnClickListener(this);
        mBinding.endamountBtn.setOnClickListener(this);
        mBinding.endticketBtn.setOnClickListener(this);
        mBinding.thismoneyBtn.setOnClickListener(this);
        mBinding.fileListBtn.setOnClickListener(this);
        mBinding.newBuildBtn.setOnClickListener(this);
        mBinding.bnt1.setOnClickListener(this);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ProcessListAdapter(R.layout.item_list_process, voListBeans);
        mBinding.recyclerview.setAdapter(listAdapter);


    }


    @Override
    protected void initData() {
        super.initData();
        project_findById();
    }

    /**
     * 11.3我的收款/付款/收票/开票查询详情接口
     */
    private void project_findById() {
        showLoading();
        HttpUtil.money_moneyQuery(getPer()).execute(new JsonCallback<Result<AppColletionLoglDet>>() {

            @Override
            public void onSuccess(Result<AppColletionLoglDet> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    setDataShow(result.data);
                    info = result.data;
                    setAddList();
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
        if (info.activityVo != null) {
            for (int i = 0; i < info.activityVo.size(); i++) {
                ProjectApprovaLoglDetEnt.ActivityVoListBean bean = new ProjectApprovaLoglDetEnt.ActivityVoListBean();
                bean.id = info.activityVo.get(i).id;
                bean.processInstanceId = info.activityVo.get(i).processInstanceId;
                bean.processDefId = info.activityVo.get(i).processDefId;
                bean.assignee = info.activityVo.get(i).assignee;
                bean.name = info.activityVo.get(i).name;
                bean.status = info.activityVo.get(i).status;
                bean.comment = info.activityVo.get(i).comment;
                bean.actType = info.activityVo.get(i).actType;
                bean.actId = info.activityVo.get(i).actId;
                bean.businessKey = info.activityVo.get(i).businessKey;
                bean.createdDate = info.activityVo.get(i).createdDate;
                bean.updatedDate = info.activityVo.get(i).updatedDate;
                bean.userId = info.activityVo.get(i).userId;
                bean.userName = info.activityVo.get(i).userName;
                voListBeans.add(bean);
            }
            listAdapter.notifyDataSetChanged();
        }

    }


    private void setDataShow(AppColletionLoglDet item) {
        if (item.userName != null) {
            if (item.userName.length() >= 3) {
                String strh = item.userName.substring(item.userName.length() - 2, item.userName.length());   //截取
                mBinding.nameTvBg.setText(strh);
            } else {
                mBinding.nameTvBg.setText(item.userName);
            }
        }
        mBinding.userName.setText(item.userName + "提交申请");
        mBinding.actstatusTv.setText(item.actStatus);
        mBinding.spbhTv.setText("票款差额：" + item.diffMoney);
        mBinding.contracttypename.setText("合同类型：" + item.contractTypeName);
        mBinding.szbmType.setText("合同编号：" + item.contractNo);
        mBinding.projType.setText("合同名称：" + item.contractName);
        mBinding.projNumber.setText("项目编号：" + item.projectNo);
        mBinding.projName.setText("项目名称：" + item.projectName);
        mBinding.persionName.setText("申请日期：" + DateUtil.formatMillsTo(item.createTime));

        switch (bidType) {
            case "1":
                mBinding.appalyContentTv.setText("未收款：" + item.notAmount);
                mBinding.dateTimeTv.setText("未开票：" + item.notTicket);
                mBinding.endamountTv.setText("已收款：" + item.endAmount);
                mBinding.endticketTv.setText("已开票：" + item.endTicket);
                mBinding.thismoneyTv.setText("本次收款：" + item.thisAmount);
                break;
            case "2":
                mBinding.appalyContentTv.setText("未付款：" + item.notAmount);
                mBinding.dateTimeTv.setText("未收票：" + item.notTicket);
                mBinding.endamountTv.setText("已付款：" + item.endAmount);
                mBinding.endticketTv.setText("已收票：" + item.endTicket);
                mBinding.thismoneyTv.setText("本次付款：" + item.thisAmount);
                break;
            case "3":
                mBinding.appalyContentTv.setText("未收款：" + item.notAmount);
                mBinding.dateTimeTv.setText("未开票：" + item.notTicket);
                mBinding.endamountTv.setText("已收款：" + item.endAmount);
                mBinding.endticketTv.setText("已开票：" + item.endTicket);
                mBinding.thismoneyTv.setText("本次开票：" + item.thisAmount);
                break;
            case "4":
                mBinding.appalyContentTv.setText("未付款：" + item.notAmount);
                mBinding.dateTimeTv.setText("未收票：" + item.notTicket);
                mBinding.endamountTv.setText("已付款：" + item.endAmount);
                mBinding.endticketTv.setText("已收票：" + item.endTicket);
                mBinding.thismoneyTv.setText("本次收票：" + item.thisAmount);
                break;
        }

        mBinding.paynameTv.setText("付款方名称：" + item.payName);
        mBinding.colnameTv.setText("收款方名称：" + item.colName);
        if (item.fileList != null) {
            mBinding.fileListTv.setText("合同附件" + item.fileList.size() + "个");
        } else {
            mBinding.fileListTv.setText("合同附件");
        }

        if (ApplicationCollectionLogActivity.mType == 1) {
            mBinding.bnt1.setVisibility(View.GONE);
            mBinding.refuseTv.setVisibility(View.VISIBLE);
            mBinding.agreeTv.setVisibility(View.VISIBLE);
            mBinding.newBuildBtn.setVisibility(View.GONE);
        } else if (ApplicationCollectionLogActivity.mType == 4) {
            mBinding.bnt1.setVisibility(View.VISIBLE);
            mBinding.refuseTv.setVisibility(View.GONE);
            mBinding.agreeTv.setVisibility(View.GONE);
            mBinding.newBuildBtn.setVisibility(View.VISIBLE);
            // 是否可以撤销任务:1-可以 0-不可以
            if (item.canCancelTask == 1) {
                mBinding.bnt1.setVisibility(View.VISIBLE);
            } else {
                mBinding.bnt1.setVisibility(View.GONE);
            }
        } else {
            mBinding.bottomStateLayout.setVisibility(View.GONE);
        }

    }


    private HashMap<String, Object> getPer() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("type", bidType);
        return hashMap;
    }

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        ProjectAppLogEnt appLogEnt = new ProjectAppLogEnt();
        appLogEnt.processInstId = info.processInstanceId;
        appLogEnt.id = info.id;
        switch (v.getId()) {
            case R.id.bnt1: // 撤销
                switch (bidType) {
                    case "1":
                        auditAdd("3", AppConfig.status.value3, appLogEnt);
                        break;
                    case "2":
                        auditAdd("3", AppConfig.status.value4, appLogEnt);
                        break;
                    case "3":
                        auditAdd("3", AppConfig.status.value5, appLogEnt);
                        break;
                    case "4":
                        auditAdd("3", AppConfig.status.value6, appLogEnt);
                        break;
                }

                break;
            case R.id.refuse_tv: // 拒绝
                switch (bidType) {
                    case "1":
                        auditAdd("2", AppConfig.status.value3, appLogEnt);
                        break;
                    case "2":
                        auditAdd("2", AppConfig.status.value4, appLogEnt);
                        break;
                    case "3":
                        auditAdd("2", AppConfig.status.value5, appLogEnt);
                        break;
                    case "4":
                        auditAdd("2", AppConfig.status.value6, appLogEnt);
                        break;
                }
                break;

            case R.id.agree_tv:// 同意
                switch (bidType) {
                    case "1":
                        auditAdd("1", AppConfig.status.value3, appLogEnt);
                        break;
                    case "2":
                        auditAdd("1", AppConfig.status.value4, appLogEnt);
                        break;
                    case "3":
                        auditAdd("1", AppConfig.status.value5, appLogEnt);
                        break;
                    case "4":
                        auditAdd("1", AppConfig.status.value6, appLogEnt);
                        break;
                }
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
                    showToast("该申请记录暂无上传附件");
                }
                break;
            case R.id.endamount_btn:// 已收款，已付款
                if (info != null && info.companyMoneyPaymentVOS != null && info.companyMoneyPaymentVOS.size() > 0) {
                    companyMoneyPaymentVO.clear();
                    for (int i = 0; i < info.companyMoneyPaymentVOS.size(); i++) {
                        GetReceivedInfoEnt.CompanyMoneyPaymentVOBean bean = new GetReceivedInfoEnt.CompanyMoneyPaymentVOBean();
                        bean.amount = info.companyMoneyPaymentVOS.get(i).amount;
                        bean.paymentTime = info.companyMoneyPaymentVOS.get(i).paymentTime;
                        bean.remarks = info.companyMoneyPaymentVOS.get(i).remarks;
                        companyMoneyPaymentVO.add(bean);
                    }
                    bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) companyMoneyPaymentVO);
                    bundle.putString("type", bidType);
                    openActivity(HistoryReceiptsPaymentsDetActivity.class, bundle);
                } else {
                    showToast("暂无收款或者付款数据");
                }
                break;
            case R.id.endticket_btn:// 已开票 已收票
                if (info != null && info.companyMoneyTicketVOS != null && info.companyMoneyTicketVOS.size() > 0) {
                    companyMoneyTicketVO.clear();
                    for (int i = 0; i < info.companyMoneyTicketVOS.size(); i++) {
                        GetReceivedInfoEnt.CompanyMoneyTicketVOBean bean = new GetReceivedInfoEnt.CompanyMoneyTicketVOBean();
                        bean.amount = info.companyMoneyTicketVOS.get(i).amount;
                        bean.paymentTime = info.companyMoneyTicketVOS.get(i).paymentTime;
                        bean.taxRate = info.companyMoneyTicketVOS.get(i).taxRate;
                        bean.taxes = info.companyMoneyTicketVOS.get(i).taxes;
                        bean.rateName = info.companyMoneyTicketVOS.get(i).rateName;
                        companyMoneyTicketVO.add(bean);
                    }
                    bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) companyMoneyTicketVO);
                    bundle.putString("type", bidType);
                    openActivity(HistoryInvoicedCollectActivity.class, bundle);
                } else {
                    showToast("暂无开票或者收票数据");
                }
                break;
            case R.id.thismoney_btn:// 本次收付款开收票
                if (bidType.equals("1") || bidType.equals("2")) { //
                    if (info != null && info.thiscompanyMoneyPaymentVOS != null && info.thiscompanyMoneyPaymentVOS.size() > 0) {
                        paymentVOS.clear();
                        for (int i = 0; i < info.thiscompanyMoneyPaymentVOS.size(); i++) {
                            CompanyMoneyPaymentVOS ben = new CompanyMoneyPaymentVOS();
                            ben.amount = info.thiscompanyMoneyPaymentVOS.get(i).amount;
                            ben.date = info.thiscompanyMoneyPaymentVOS.get(i).paymentTime;
                            ben.remarks = info.thiscompanyMoneyPaymentVOS.get(i).remarks;
                            paymentVOS.add(ben);
                        }
                        bundle = new Bundle();
                        bundle.putSerializable("data", (Serializable) paymentVOS);
                        bundle.putString("type", bidType);
                        bundle.putString("type_det", "det");
                        openActivity(ReceiptsPaymentsDetActivity.class, bundle);
                    } else {
                        showToast("暂无本次收款或者本次付款数据");
                    }

                } else if (bidType.equals("3") || bidType.equals("4")) {
                    if (info != null && info.thisompanyMoneyTicketVO != null && info.thisompanyMoneyTicketVO.size() > 0) {
                        ticketVOS.clear();
                        for (int i = 0; i < info.thisompanyMoneyTicketVO.size(); i++) {
                            CompanyMoneyTicketVOS ben = new CompanyMoneyTicketVOS();
                            ben.amount = info.thisompanyMoneyTicketVO.get(i).amount;
                            ben.paymentTime = info.thisompanyMoneyTicketVO.get(i).paymentTime;
                            ben.taxes = info.thisompanyMoneyTicketVO.get(i).taxes;
                            ben.taxRate = (int) info.thisompanyMoneyTicketVO.get(i).taxeRate;
                            ben.rateName = info.thisompanyMoneyTicketVO.get(i).rateName;
                            ticketVOS.add(ben);
                        }
                        bundle = new Bundle();
                        bundle.putSerializable("data", (Serializable) ticketVOS);
                        bundle.putString("type", bidType);
                        bundle.putString("type_det", "det");
                        openActivity(InvoicedCollectActivity.class, bundle);
                    } else {
                        showToast("暂无本次开票或者本次收票数据");
                    }

                }
                break;
            case R.id.new_build_btn:// 新建
                if (info != null) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", info);
                    bundle.putString("type", bidType);
                    openActivity(ApplicationCollectionActivity.class, bundle);
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
}
