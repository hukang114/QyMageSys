package com.qymage.sys.ui.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityProjectApprovaLoglDetBinding;
import com.qymage.sys.ui.adapter.ProcessListAdapter;
import com.qymage.sys.ui.entity.ProjectApprovaLoglDetEnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 立项记录详情
 */
public class ProjectApprovaLoglDetActivity extends BBActivity<ActivityProjectApprovaLoglDetBinding> implements View.OnClickListener {


    private String Tag;
    private String id;
    private Intent mIntent;
    ProjectApprovaLoglDetEnt info;
    List<ProjectApprovaLoglDetEnt.ActivityVoListBean> voListBeans = new ArrayList<>();
    ProcessListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_approva_logl_det;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        Tag = mIntent.getStringExtra("Tag");
        id = mIntent.getStringExtra("id");
        mBinding.refuseTv.setOnClickListener(this);
        mBinding.agreeTv.setOnClickListener(this);
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
     * 获取立项记录详情
     */
    private void project_findById() {
        showLoading();
        HttpUtil.project_findById(getPer()).execute(new JsonCallback<Result<ProjectApprovaLoglDetEnt>>() {
            @Override
            public void onSuccess(Result<ProjectApprovaLoglDetEnt> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    setDataShow(result.data);
                    info = result.data;
                    if (result.data.activityVoList != null) {
                        voListBeans.addAll(result.data.activityVoList);
                        listAdapter.notifyDataSetChanged();
                    }
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


    private void setDataShow(ProjectApprovaLoglDetEnt item) {
        if (item.personName != null) {
            if (item.personName.length() >= 3) {
                String strh = item.personName.substring(item.personName.length() - 2, item.personName.length());   //截取
                mBinding.nameTvBg.setText(strh);
            } else {
                mBinding.nameTvBg.setText(item.personName);
            }
        }
        mBinding.userName.setText(item.persion + item.projectType);
        mBinding.actstatusTv.setText(item.actStatus);
        mBinding.spbhTv.setText("编号：" + item.id);
        mBinding.szbmType.setText("所在部门：" + item.projectTypeName);
        mBinding.projType.setText("项目类型：" + item.projectTypeName);
        mBinding.projNumber.setText("项目编号：" + item.projectNo);
        mBinding.projName.setText("项目名称：" + item.projectName);
        mBinding.persionName.setText("负责人：" + item.persion);
        mBinding.appalyContentTv.setText("预算金额：" + item.amount);
        mBinding.dateTimeTv.setText("日期：" + item.date);
        mBinding.introductionTv.setText("项目介绍：" + item.introduction);

    }


    private HashMap<String, Object> getPer() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        return hashMap;
    }

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refuse_tv: // 拒绝
                if (info != null) {
                    msgDialogBuilder("拒绝审批？", (dialog, which) -> {
                        auditAdd("2");
                    }).create().show();
                }
                break;

            case R.id.agree_tv:// 同意
                if (info != null) {
                    msgDialogBuilder("同意审批？", (dialog, which) -> {
                        auditAdd("1");
                    }).create().show();
                }
                break;
        }

    }


    /**
     * 审批操作
     * type：类型  1—通过   2-拒绝 3-撤回
     *
     * @param type
     */
    private void auditAdd(String type) {
        showLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Id", info.id);
        hashMap.put("remarks", "");
        hashMap.put("type", type);
        hashMap.put("processInstanceId", info.processInstId);
        hashMap.put("modeType", AppConfig.status.value1);
        HttpUtil.audit_auditAdd(hashMap).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgDialogBuilder(result.message, (dialog, which) -> {
                    dialog.dismiss();
                    setResult(200);
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


}
