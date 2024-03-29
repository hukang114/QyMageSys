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
import com.qymage.sys.common.util.DateUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityProjectApprovaLoglDetBinding;
import com.qymage.sys.ui.adapter.ProcessListAdapter;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.qymage.sys.ui.entity.ProjectApprovaLoglDetEnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 立项记录详情
 * 立项详情
 */
public class ProjectApprovaLoglDetActivity extends BBActivity<ActivityProjectApprovaLoglDetBinding> implements View.OnClickListener {


    private String Tag;
    private String id;
    private Intent mIntent;
    ProjectApprovaLoglDetEnt info;
    List<ProjectApprovaLoglDetEnt.ActivityVoListBean> voListBeans = new ArrayList<>();
    ProcessListAdapter listAdapter;
    private Bundle bundle;


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
        mBinding.bnt1.setOnClickListener(this);
        mBinding.newBuildBtn.setOnClickListener(this);
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
        mBinding.userName.setText(item.personName + "提交" + item.projectTypeName + "申请");
        mBinding.actstatusTv.setText(item.actStatus);
//        mBinding.spbhTv.setText("编号Id：" + item.id);
        mBinding.spbhTv.setVisibility(View.GONE);
        mBinding.szbmType.setText("负责人：" + item.persion);
        mBinding.projType.setText("项目类型：" + item.projectTypeName);
        mBinding.projNumber.setText("项目编号：" + item.projectNo);
        mBinding.projName.setText("项目名称：" + item.projectName);
//        mBinding.persionName.setText("负责人：" + item.persion);
        mBinding.persionName.setVisibility(View.GONE);
        mBinding.appalyContentTv.setText("预算金额：" + item.amount);
        mBinding.dateTimeTv.setText("合同日期：" + item.date);
        mBinding.createtimeTv.setText("申请日期：" + DateUtil.formatMillsTo(item.createTime));
        mBinding.introductionTv.setText("项目介绍：" + item.introduction);
        if (ProjectApprovaLoglActivity.mType == 1) {
            mBinding.bnt1.setVisibility(View.GONE);
            mBinding.refuseTv.setVisibility(View.VISIBLE);
            mBinding.agreeTv.setVisibility(View.VISIBLE);
            mBinding.newBuildBtn.setVisibility(View.GONE);
        } else if (ProjectApprovaLoglActivity.mType == 4) {
            mBinding.refuseTv.setVisibility(View.GONE);
            mBinding.agreeTv.setVisibility(View.GONE);
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
        return hashMap;
    }

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        ProjectAppLogEnt item = new ProjectAppLogEnt();
        item.id = info.id;
        item.processInstId = info.processInstId;
        switch (v.getId()) {
            case R.id.refuse_tv: // 拒绝
                auditAdd("2", AppConfig.status.value1, item);
                break;
            case R.id.agree_tv:// 同意
                auditAdd("1", AppConfig.status.value1, item);
                break;
            case R.id.bnt1:
                auditAdd("3", AppConfig.status.value1, item);
                break;
            case R.id.new_build_btn:// 新建
                if (info != null) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", info);
                    openActivity(ProjectApprovaApplylActivity.class, bundle);
                } else {
                    showToast("暂未获取到数据");
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
