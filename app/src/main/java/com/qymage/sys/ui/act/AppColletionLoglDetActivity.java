package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityAppcolletionloglDetBinding;
import com.qymage.sys.databinding.ActivityApplicationCollectionLogBinding;
import com.qymage.sys.databinding.ActivityBidperformtblyszloglDetBinding;
import com.qymage.sys.ui.adapter.ProcessListAdapter;
import com.qymage.sys.ui.entity.AppColletionLoglDet;
import com.qymage.sys.ui.entity.BidPerFormDetEnt;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.qymage.sys.ui.entity.ProjectApprovaLoglDetEnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 收付款 支-收  开票 支-收详情
 */
public class AppColletionLoglDetActivity extends BBActivity<ActivityAppcolletionloglDetBinding> implements View.OnClickListener {


    private String Tag;
    private String id;
    private Intent mIntent;
    AppColletionLoglDet info;
    private String bidType;
    List<ProjectApprovaLoglDetEnt.ActivityVoListBean> voListBeans = new ArrayList<>();
    ProcessListAdapter listAdapter;


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
        if (ApplicationCollectionLogActivity.mType == 1) {
            mBinding.bnt1.setVisibility(View.GONE);
            mBinding.refuseTv.setVisibility(View.VISIBLE);
            mBinding.agreeTv.setVisibility(View.VISIBLE);
        } else if (ApplicationCollectionLogActivity.mType == 4) {
            mBinding.bnt1.setVisibility(View.VISIBLE);
            mBinding.refuseTv.setVisibility(View.GONE);
            mBinding.agreeTv.setVisibility(View.GONE);
        } else {
            mBinding.bottomStateLayout.setVisibility(View.GONE);
        }
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
        if (item.personName != null) {
            if (item.personName.length() >= 3) {
                String strh = item.personName.substring(item.personName.length() - 2, item.personName.length());   //截取
                mBinding.nameTvBg.setText(strh);
            } else {
                mBinding.nameTvBg.setText(item.personName);
            }
        }
        mBinding.userName.setText(item.personName + "申请");
        mBinding.actstatusTv.setText(item.actStatus);
        mBinding.spbhTv.setText("编号id：" + item.id);
        mBinding.szbmType.setText("合同编号：" + item.contractNo);
        mBinding.projType.setText("合同名称：" + item.contractName);
        mBinding.projNumber.setText("项目编号：" + item.projectNo);
        mBinding.projName.setText("项目名称：" + item.projectName);
        mBinding.persionName.setText("公司名称：" + item.companyName);
        mBinding.appalyContentTv.setText("金额：" + item.thisMoney);
        mBinding.dateTimeTv.setText("日期：" + item.date);
        mBinding.introductionTv.setText("付款方名称：" + item.payName);
        mBinding.colnameTv.setText("收款方名称：" + item.colName);

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
        appLogEnt.processInstId = info.processInstId;
        appLogEnt.id = info.id;
        switch (v.getId()) {
            case R.id.bnt1: // 撤销
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
        }

    }

    @Override
    protected void successTreatment() {
        super.successTreatment();
        setResult(200);
        finish();
    }
}
