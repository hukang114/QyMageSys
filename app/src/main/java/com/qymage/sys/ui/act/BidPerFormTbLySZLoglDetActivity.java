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
import com.qymage.sys.databinding.ActivityBidperformtblyszloglDetBinding;
import com.qymage.sys.databinding.ActivityProjectApprovaLoglDetBinding;
import com.qymage.sys.ui.adapter.ProcessListAdapter;
import com.qymage.sys.ui.entity.BidPerFormDetEnt;
import com.qymage.sys.ui.entity.FileListEnt;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.qymage.sys.ui.entity.ProjectApprovaLoglDetEnt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 投标保证金及 支-收  履约保证金 支-收详情
 */
public class BidPerFormTbLySZLoglDetActivity extends BBActivity<ActivityBidperformtblyszloglDetBinding> implements View.OnClickListener {

    List<FileListEnt> fileList = new ArrayList<>();// 上传附件
    private String Tag;
    private String id;
    private Intent mIntent;
    BidPerFormDetEnt info;
    private String bidType;
    List<ProjectApprovaLoglDetEnt.ActivityVoListBean> voListBeans = new ArrayList<>();
    ProcessListAdapter listAdapter;
    Bundle bundle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bidperformtblyszlogl_det;
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
        mBinding.bnt1.setOnClickListener(this);
        mBinding.newBuildBtn.setOnClickListener(this);
        mBinding.fileListBtn.setOnClickListener(this);
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
     * 投标保证及 支-收  履约保证金 支-收详情
     */
    private void project_findById() {
        showLoading();
        HttpUtil.bid_findById(getPer()).execute(new JsonCallback<Result<BidPerFormDetEnt>>() {

            @Override
            public void onSuccess(Result<BidPerFormDetEnt> result, Call call, Response response) {
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


    private void setDataShow(BidPerFormDetEnt item) {
        if (item.personName != null) {
            if (item.personName.length() >= 3) {
                String strh = item.personName.substring(item.personName.length() - 2, item.personName.length());   //截取
                mBinding.nameTvBg.setText(strh);
            } else {
                mBinding.nameTvBg.setText(item.personName);
            }
        }
        mBinding.userName.setText(item.personName + "申请");
//        String status = VerifyUtils.isEmpty(item.actStatus) ? "" : 1 == item.actStatus ? "待处理" : 2 == item.actStatus ? "已处理" :
////                3 == item.actStatus ? "抄送给我" : 4 == item.actStatus ? "已处理" : "";

        mBinding.actstatusTv.setText(item.actStatus);
        mBinding.spbhTv.setVisibility(View.GONE);
        mBinding.szbmType.setText("保证金名称：" + item.amountName);
        if (bidType.equals("01") || bidType.equals("03")) {
            mBinding.projType.setText("收款单位名称：" + item.name);
        } else {
            mBinding.projType.setText("付款单位名称：" + item.name);
        }
        mBinding.bankTv.setText("开户行：" + item.bank);
        mBinding.accountTv.setText("银行账号：" + item.account);
        mBinding.projNumber.setText("项目编号：" + item.projectNo);
        mBinding.projName.setText("项目名称：" + item.projectName);
        mBinding.persionName.setText("公司名称：" + item.companyName);
        mBinding.appalyContentTv.setText("金额：" + item.amount);
        mBinding.endTimeTv.setText("结束日期：" + item.endDate);
        mBinding.createtimeTv.setText("申请日期：" + DateUtil.formatMillsTo(item.createTime));
        if (bidType.equals("01") || bidType.equals("02")) {
            mBinding.fileLayout.setVisibility(View.GONE);
            mBinding.dateTimeTv.setVisibility(View.GONE);
        } else {
            mBinding.dateTimeTv.setText("开始日期：" + item.date);
            if (item.fileList != null) {
                mBinding.fileListTv.setText("申请附件" + item.fileList.size() + "个");
            } else {
                mBinding.fileListTv.setText("申请附件");
            }
        }
        mBinding.introductionTv.setText("备注：" + item.remark);


        if (BidPerformanceLvYueSZActivity.mType == 1) {
            mBinding.bnt1.setVisibility(View.GONE);
            mBinding.refuseTv.setVisibility(View.VISIBLE);
            mBinding.agreeTv.setVisibility(View.VISIBLE);
            mBinding.newBuildBtn.setVisibility(View.GONE);
        } else if (BidPerformanceLvYueSZActivity.mType == 4) {
            mBinding.refuseTv.setVisibility(View.GONE);
            mBinding.agreeTv.setVisibility(View.GONE);
            mBinding.newBuildBtn.setVisibility(View.VISIBLE);
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
        hashMap.put("bidType", bidType);
        return hashMap;
    }

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        if (info != null) {
            ProjectAppLogEnt appLogEnt = new ProjectAppLogEnt();
            appLogEnt.processInstId = info.processInstId;
            appLogEnt.id = info.id;
            switch (v.getId()) {
                case R.id.bnt1:// 撤回
                    switch (bidType) {
                        case "01":
                            auditAdd("3", AppConfig.status.value3, appLogEnt);
                            break;
                        case "02":
                            auditAdd("3", AppConfig.status.value4, appLogEnt);
                            break;
                        case "03":
                            auditAdd("3", AppConfig.status.value5, appLogEnt);
                            break;
                        case "04":
                            auditAdd("3", AppConfig.status.value6, appLogEnt);
                            break;
                    }

                    break;

                case R.id.refuse_tv: // 拒绝
                    switch (bidType) {
                        case "01":
                            auditAdd("2", AppConfig.status.value3, appLogEnt);
                            break;
                        case "02":
                            auditAdd("2", AppConfig.status.value4, appLogEnt);
                            break;
                        case "03":
                            auditAdd("2", AppConfig.status.value5, appLogEnt);
                            break;
                        case "04":
                            auditAdd("2", AppConfig.status.value6, appLogEnt);
                            break;
                    }
                    break;

                case R.id.agree_tv:// 同意
                    switch (bidType) {
                        case "01":
                            auditAdd("1", AppConfig.status.value3, appLogEnt);
                            break;
                        case "02":
                            auditAdd("1", AppConfig.status.value4, appLogEnt);
                            break;
                        case "03":
                            auditAdd("1", AppConfig.status.value5, appLogEnt);
                            break;
                        case "04":
                            auditAdd("1", AppConfig.status.value6, appLogEnt);
                            break;
                    }
                    break;
                case R.id.new_build_btn:// 复制新建
                    if (info != null) {
                        bundle = new Bundle();
                        bundle.putSerializable("data", info);
                        bundle.putString("type", bidType.replace("0", ""));
                        openActivity(BiddingMarginZhiActivity.class, bundle);
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
                        showToast("暂无上传附件");
                    }
                    break;
            }
        } else {
            showToast("获取信息失败");
        }


    }

    @Override
    protected void successTreatment() {
        super.successTreatment();
        setResult(200);
        finish();
    }


}
