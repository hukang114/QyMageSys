package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivityBiddingMarginZhiBinding;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.entity.CompanyMoneyPaymentVOS;
import com.qymage.sys.ui.entity.CompanyMoneyTicketVOS;
import com.qymage.sys.ui.entity.FileListEnt;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.PaymentInfo;
import com.qymage.sys.ui.entity.ReceiverInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 投标保证金支  收-履约保证金 支 -收 统一此页面处理
 */
public class BiddingMarginZhiActivity extends BBActivity<ActivityBiddingMarginZhiBinding> implements View.OnClickListener {


    private Intent mIntent;
    private String type;
    private int date_typp = 1;
    private long startime = 0;
    private long endtime = 0;
    private String projectId = "";// 项目id
    private String companyId = "";// 公司id
    List<FileListEnt> fileList = new ArrayList<>();// 上传附件
    List<GetTreeEnt> auditorList = new ArrayList<>();// 审核人
    List<GetTreeEnt> copierList = new ArrayList<>();// 抄送人
    AuditorListAdapter auditorListAdapter;// 审批人适配器
    CopierListAdapter copierListAdapter;// 抄送人适配器
    private Bundle bundle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bidding_margin_zhi;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.metitle.setlImgClick(v -> finish());
        mIntent = getIntent();
        type = mIntent.getStringExtra("type");
        if (type == null) {
            return;
        }
        mBinding.startDateTv.setOnClickListener(this);
        mBinding.endDateTv.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);
        mBinding.sprImg.setOnClickListener(this);
        mBinding.csrImg.setOnClickListener(this);
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


    }

    @Override
    protected void initData() {
        super.initData();
        switch (type) {
            case "1":// 投标保证金支
                mBinding.metitle.setcTxt(this.getResources().getString(R.string.toubiaozhi_txt));
                break;
            case "2"://投标保证金收
                mBinding.metitle.setcTxt("投标保证金收");
                mBinding.skdwmcTv.setText("付款单位名称");
                mBinding.skdwmcEdt.setHint("请输入付款单位名称");
                break;
            case "3":// 履约保证金支
                mBinding.metitle.setcTxt(this.getResources().getString(R.string.lybzjz_txt));
                break;
            case "4"://履约保证金收
                mBinding.metitle.setcTxt(this.getResources().getString(R.string.lybzjs_txt));
                mBinding.skdwmcTv.setText("付款单位名称");
                mBinding.skdwmcEdt.setHint("请输入付款单位名称");
                break;
        }
    }


    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_date_tv:// 开始日期
                date_typp = 1;
                showDateDialog();
                break;

            case R.id.end_date_tv:// 到期日期
                date_typp = 2;
                showDateDialog();
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

                break;
            case R.id.save_btn:
                if (isCheck()) {
                    subMitData();
                }
                break;

        }
    }


    /**
     * 提交数据
     */
    private void subMitData() {
        showLoading();
        HttpUtil.bid_submit(HttpConsts.BID_SUBMIT, getPer()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgDialog("申请提成功");
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
     * 日期选择
     */
    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String desc = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
                if (date_typp == 1) {
                    startime = Long.parseLong(year + "" + (month + 1) + dayOfMonth);
                    mBinding.startDateTv.setText(desc);
                } else if (date_typp == 2) {
                    endtime = Long.parseLong(year + "" + (month + 1) + dayOfMonth);
                    if (endtime < startime) {
                        showToast("结束时间不能小于开始时间");
                    } else {
                        mBinding.endDateTv.setText(desc);
                    }
                }
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) { // //收付款明细

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
        } else if (resultCode == 600) {// 获取付款方信息
        } else if (resultCode == 700) {//开收票明细
        }
    }


    /**
     * 参数
     *
     * @return
     */
    private HashMap<String, Object> getPer() {
        HashMap<String, Object> hashMap = new HashMap();
        hashMap.put("projectId", mBinding.baoxiaoMoneyEdt.getText().toString());
        hashMap.put("companyId", companyId);
        hashMap.put("bidType", type);
        hashMap.put("name", mBinding.skdwmcEdt.getText().toString());
        hashMap.put("bank", mBinding.khhEdt.getText().toString());
        hashMap.put("account", mBinding.yhzhEdt.getText().toString());
        hashMap.put("amount", mBinding.bzjjeEdt.getText().toString());
        hashMap.put("amountName", mBinding.bzjmcEdt.getText().toString());
        hashMap.put("date", mBinding.startDateTv.getText().toString());
        hashMap.put("endDate", mBinding.endDateTv.getText().toString());
        hashMap.put("remark", mBinding.baoxiaoContent.getText().toString());
        //文件
        hashMap.put("fileList", fileList);
        ////审核人
        hashMap.put("auditor", auditorList);
        //抄送人
        hashMap.put("copier", copierList);
        return hashMap;
    }


    private boolean isCheck() {
        if (TextUtils.isEmpty(mBinding.baoxiaoMoneyEdt.getText().toString())) {
            showToast("请填写项目编号");
            return false;
        } else if (TextUtils.isEmpty(mBinding.shenqingMoneyEdt.getText().toString())) {
            showToast("请输入项目名称");
            return false;
        } else if (TextUtils.isEmpty(mBinding.bzjmcEdt.getText().toString())) {
            showToast("请输入保证金名称");
            return false;
        } else if (TextUtils.isEmpty(mBinding.gsmcEdt.getText().toString())) {
            showToast("请输入公司名称");
            return false;
        } else if (TextUtils.isEmpty(mBinding.skdwmcEdt.getText().toString())) {
            showToast("请输入单位名称");
            return false;
        } else if (TextUtils.isEmpty(mBinding.yhzhEdt.getText().toString())) {
            showToast("请输入银行账户");
            return false;
        } else if (TextUtils.isEmpty(mBinding.khhEdt.getText().toString())) {
            showToast("请输入开户行");
            return false;
        } else if (TextUtils.isEmpty(mBinding.bzjjeEdt.getText().toString())) {
            showToast("请输入保证金金额");
            return false;
        } else if (TextUtils.isEmpty(mBinding.startDateTv.getText().toString())) {
            showToast("请选择开始日期");
            return false;
        } else if (TextUtils.isEmpty(mBinding.endDateTv.getText().toString())) {
            showToast("请选择结束日期");
            return false;
        } else if (TextUtils.isEmpty(mBinding.endDateTv.getText().toString())) {
            showToast("请选择结束日期");
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

