package com.qymage.sys.ui.act;

import android.content.Intent;
import android.view.View;

import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityBidperformtblyszloglDetBinding;
import com.qymage.sys.databinding.ActivityProjectApprovaLoglDetBinding;
import com.qymage.sys.ui.entity.BidPerFormDetEnt;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.qymage.sys.ui.entity.ProjectApprovaLoglDetEnt;

import java.util.HashMap;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 投标保证及 支-收  履约保证金 支-收详情
 */
public class BidPerFormTbLySZLoglDetActivity extends BBActivity<ActivityBidperformtblyszloglDetBinding> implements View.OnClickListener {


    private String Tag;
    private String id;
    private Intent mIntent;
    BidPerFormDetEnt info;
    private String bidType;


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
        String status = VerifyUtils.isEmpty(item.actStatus) ? "" : 1 == item.actStatus ? "待处理" : 2 == item.actStatus ? "已处理" :
                3 == item.actStatus ? "抄送给我" : 4 == item.actStatus ? "已处理" : "";
        mBinding.actstatusTv.setText(status);
        mBinding.szbmType.setText("保证金名称：" + item.amountName);
        mBinding.projType.setText("付款单位名称：" + item.name);
        mBinding.projNumber.setText("项目编号：" + item.projectNo);
        mBinding.projName.setText("项目名称：" + item.projectName);
        mBinding.persionName.setText("公司名称：" + item.companyName);
        mBinding.appalyContentTv.setText("金额：" + item.amount);
        mBinding.dateTimeTv.setText("日期：" + item.date);
        mBinding.introductionTv.setText("备注：" + item.remark);

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
        ProjectAppLogEnt appLogEnt = new ProjectAppLogEnt();
        appLogEnt.processInstId = info.processInstId;
        appLogEnt.id = info.id;

        switch (v.getId()) {
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


}
