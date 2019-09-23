package com.qymage.sys.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityAskforDetBinding;
import com.qymage.sys.databinding.ActivityLoanDetBinding;
import com.qymage.sys.ui.adapter.ProcessListAdapter;
import com.qymage.sys.ui.entity.ASkForDetEnt;
import com.qymage.sys.ui.entity.LoanQueryDetEnt;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.qymage.sys.ui.entity.ProjectApprovaLoglDetEnt;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 请假详情
 */
public class ASkForDetailsActivity extends BBActivity<ActivityAskforDetBinding> implements View.OnClickListener {


    private String id;// 列表id
    private Intent mIntent;
    Bundle bundle;
    ASkForDetEnt item;
    List<ProjectApprovaLoglDetEnt.ActivityVoListBean> voListBeans = new ArrayList<>();
    ProcessListAdapter listAdapter;
    CommonAdapter<String> imgadapter;//
    private List<String> planting_imgList = new ArrayList<>();//

    @Override
    protected int getLayoutId() {
        return R.layout.activity_askfor_det;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        id = mIntent.getStringExtra("id");
        if (id == null) {
            return;
        }
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ProcessListAdapter(R.layout.item_list_process, voListBeans);
        mBinding.recyclerview.setAdapter(listAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.recyclerviewImg.setLayoutManager(linearLayoutManager);
        mBinding.bnt2.setOnClickListener(this);
        mBinding.bnt3.setOnClickListener(this);
        mBinding.bnt1.setOnClickListener(this);
        setImgAdapter();

    }

    private void setImgAdapter() {
        mBinding.recyclerviewImg.setAdapter(imgadapter = new CommonAdapter<String>(this, R.layout.item_evorder_imglist, planting_imgList) {
            @Override
            protected void convert(ViewHolder holder, String path, int position) {
                Glide.with(mActivity).load(path).into((ImageView) holder.getView(R.id.title_right_img));
                holder.setVisible(R.id.cancel_img, false);
                holder.getConvertView().setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    bundle.putSerializable("piclist", (Serializable) planting_imgList);
                    openActivity(ViewPagerActivity.class, bundle);
                });
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        showLoading();
        HttpUtil.leave_detalSerc(hashMap).execute(new JsonCallback<Result<ASkForDetEnt>>() {
            @Override
            public void onSuccess(Result<ASkForDetEnt> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    item = result.data;
                    setDataShow();
                    setAddList();
                } else {
                    return;
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

    private void setAddList() {
        if (item.activityVo != null) {
            for (int i = 0; i < item.activityVo.size(); i++) {
                ProjectApprovaLoglDetEnt.ActivityVoListBean bean = new ProjectApprovaLoglDetEnt.ActivityVoListBean();
                bean.id = item.activityVo.get(i).id;
                bean.processInstanceId = item.activityVo.get(i).processInstanceId;
                bean.processDefId = item.activityVo.get(i).processDefId;
                bean.assignee = item.activityVo.get(i).assignee;
                bean.name = item.activityVo.get(i).name;
                bean.status = item.activityVo.get(i).status;
                bean.comment = item.activityVo.get(i).comment;
                bean.actType = item.activityVo.get(i).actType;
                bean.actId = item.activityVo.get(i).actId;
                bean.businessKey = item.activityVo.get(i).businessKey;
                bean.createdDate = item.activityVo.get(i).createdDate;
                bean.updatedDate = item.activityVo.get(i).updatedDate;
                bean.userId = item.activityVo.get(i).userId;
                bean.userName = item.activityVo.get(i).userName;
                voListBeans.add(bean);
            }
            listAdapter.notifyDataSetChanged();
        }

    }


    private void setDataShow() {
        if (item != null && item.photo.contains("|")) { // 多图
            List<String> result = Arrays.asList(item.photo.split("\\|"));
            planting_imgList.clear();
            planting_imgList.addAll(result);
            imgadapter.notifyDataSetChanged();
        } else {// 单图
            planting_imgList.clear();
            planting_imgList.add(item.photo);
            imgadapter.notifyDataSetChanged();
        }
        if (item.name != null) {
            if (item.name.length() >= 3) {
                String strh = item.name.substring(item.name.length() - 2, item.name.length());   //截取
                mBinding.nameTvBg.setText(strh);
            } else {
                mBinding.nameTvBg.setText(item.name);
            }
        }
        mBinding.userName.setText(item.name + "请假申请");
        mBinding.actstatusTv.setText(item.actStatus);
//        mBinding.szbmType.setText("合同名称：" + item.contractName);
        mBinding.szbmType.setVisibility(View.GONE);
//        mBinding.projType.setText("合同类型：" + item.contractTypeName);
        mBinding.projType.setVisibility(View.GONE);
        mBinding.projNumber.setText("创建时间：" + item.createDate);
        mBinding.projName.setText("请假类型：" + item.leaveName);
        mBinding.persionName.setText("开始时间：" + item.startDate);
        mBinding.appalyContentTv.setText("结束时间：" + item.endDate);
        mBinding.dateTimeTv.setText("时长：" + item.ofTime);
        mBinding.introductionTv.setText("请假原因：" + item.cause);

        switch (AskForLeaveRecordlActivity.mType) {
            case 1:
                mBinding.bnt1.setVisibility(View.GONE);
                mBinding.bnt2.setVisibility(View.VISIBLE);
                mBinding.bnt3.setVisibility(View.VISIBLE);
                break;
            case 2:
                mBinding.bnt1.setVisibility(View.GONE);
                mBinding.bnt2.setVisibility(View.GONE);
                mBinding.bnt3.setVisibility(View.GONE);
                break;
            case 3:
                mBinding.bnt1.setVisibility(View.GONE);
                mBinding.bnt2.setVisibility(View.GONE);
                mBinding.bnt3.setVisibility(View.GONE);
                break;
            case 4:
                mBinding.bnt2.setVisibility(View.GONE);
                mBinding.bnt3.setVisibility(View.GONE);
                if (item.canCancelTask == 1) {
                    mBinding.bnt1.setVisibility(View.VISIBLE);
                } else {
                    mBinding.bnt1.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }

    }

    ProjectAppLogEnt appLogEnt;

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        if (item != null) {
            appLogEnt = new ProjectAppLogEnt();
            appLogEnt.processInstId = item.processInstId;
            appLogEnt.id = item.id;
        }
        switch (v.getId()) {
            case R.id.bnt2: // 拒绝
                if (appLogEnt != null) {
                    auditAdd("2", AppConfig.status.value7, appLogEnt);
                }
                break;
            case R.id.bnt3:// 同意
                if (appLogEnt != null) {
                    auditAdd("1", AppConfig.status.value7, appLogEnt);
                }
                break;
            case R.id.bnt1:
                if (appLogEnt != null) {
                    auditAdd("3", AppConfig.status.value7, appLogEnt);
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 审批处理成功的回调
     */
    @Override
    protected void successTreatment() {
        super.successTreatment();
        setResult(200);
        finish();
    }
}
