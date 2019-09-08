package com.qymage.sys.ui.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityProjectApprovaLoglDetBinding;
import com.qymage.sys.ui.entity.ProjectApprovaLoglDetEnt;

import java.util.HashMap;

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
        HttpUtil.project_findById(HttpConsts.PROJECT_FINDBYID, getPer()).execute(new JsonCallback<Result<ProjectApprovaLoglDetEnt>>() {

            @Override
            public void onSuccess(Result<ProjectApprovaLoglDetEnt> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    setDataShow(result.data.dataInfo);
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


    private void setDataShow(ProjectApprovaLoglDetEnt.DataInfo item) {
        if (item.persion == null) {
            if (item.persion.length() >= 3) {
                String strh = item.persion.substring(item.persion.length() - 2, item.persion.length());   //截取
                mBinding.nameTvBg.setText(strh);
            } else {
                mBinding.nameTvBg.setText(item.persion);
            }
        }
        mBinding.userName.setText(item.persion + item.projectType);
        String status = VerifyUtils.isEmpty(item.actStatus) ? "" : 1 == item.actStatus ? "待处理" : 2 == item.actStatus ? "已处理" :
                3 == item.actStatus ? "抄送给我" : 4 == item.actStatus ? "已处理" : "";
        mBinding.actstatusTv.setText(status);
        mBinding.spbhTv.setText("审批编号：" + item.projectNo);
        mBinding.szbmType.setText("所在部门：" + item.number);
        mBinding.projType.setText("项目类型：" + item.projectType);
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
                msgDialogBuilder("拒绝审批？", (dialog, which) -> {
                    auditAdd("2");
                }).create().show();
                break;

            case R.id.agree_tv:// 同意
                msgDialogBuilder("同意审批？", (dialog, which) -> {
                    auditAdd("1");
                }).create().show();
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
        hashMap.put("userCode", getUserId());
        hashMap.put("remarks", "");
        hashMap.put("type", type);
        hashMap.put("taskId", id);
        HttpUtil.audit_auditAdd(HttpConsts.audit_auditAdd, hashMap).execute(new JsonCallback<Result<String>>() {
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
