package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivityApplicationRepaymentBinding;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.QuotaQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 还款申请
 */
public class ApplicationRepaymentActivity extends BBActivity<ActivityApplicationRepaymentBinding> implements View.OnClickListener {


    List<GetTreeEnt> auditorList = new ArrayList<>();// 审核人
    List<GetTreeEnt> copierList = new ArrayList<>();// 抄送人
    AuditorListAdapter auditorListAdapter;// 审批人适配器
    CopierListAdapter copierListAdapter;// 抄送人适配器
    private Bundle bundle;
    QuotaQuery query;
    List<String> orgList = new ArrayList<>();
    private Intent mIntent;
    private String TaskId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_application_repayment;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mIntent = getIntent();
        TaskId = mIntent.getStringExtra("TaskId");
        if (TaskId == null) {
            return;
        }
        mBinding.bumTxt.setOnClickListener(this);
        mBinding.shiyongDateTv.setOnClickListener(this);
        mBinding.sprImg.setOnClickListener(this);
        mBinding.csrImg.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);
        LinearLayoutManager layouta = new LinearLayoutManager(this);
        layouta.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        LinearLayoutManager layoutb = new LinearLayoutManager(this);
        layoutb.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        mBinding.sprRecyclerview.setLayoutManager(layouta);
        mBinding.csrRecyclerview.setLayoutManager(layoutb);
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
        orgList.add("技术部");
        orgList.add("人事部");
        orgList.add("财务部");
        orgList.add("采购部");
        loan_quotaQuery();

    }

    private void loan_quotaQuery() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userCode", getUserId());
        showLoading();
        HttpUtil.loan_quotaQuery(hashMap).execute(new JsonCallback<Result<QuotaQuery>>() {
            @Override
            public void onSuccess(Result<QuotaQuery> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    query = result.data;
                    mBinding.loanQuotaTv.setText("借款总额度" + result.data.TotalAmount + "已借" + result.data.ceaseAmount + "剩余" + result.data.leftAmount);
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

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bum_txt:// 选择部门
                setOnClick();
                break;
            case R.id.shiyong_date_tv://日期
                selectClickDate();
                break;

            case R.id.save_btn:// 提交
                if (isCheck()) {
                    payAdd();
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
        }
    }


    /**
     * 提交还款申请
     */
    private void payAdd() {
        showLoading();
        HttpUtil.loan_payAdd(getPer()).execute(new JsonCallback<Result<String>>() {

            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                msgDialogBuilder("恭喜您,还款成功!", (dialog, which) -> {
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


    /**
     * 选择日期
     *
     * @param
     */
    private void selectClickDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String desc = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
            mBinding.shiyongDateTv.setText(desc);

        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


    private void setOnClick() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) ->

                mBinding.bumTxt.setText(orgList.get(options1)))
                .setTitleText("请选择部门")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(18)
                .setOutSideCancelable(false)//点击外部dismiss default true
                .setContentTextSize(16)//滚轮文字大小
                .setSubCalSize(16)//确定和取消文字大小
                .setLineSpacingMultiplier(2.4f)
                .build();
        // 三级选择器
        pvOptions.setPicker(orgList, null, null);
        pvOptions.show();

    }

    /**
     * 还款申请参数
     *
     * @return
     */
    private HashMap<String, Object> getPer() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", getUserId());
        map.put("cause", mBinding.causeContent.getText().toString());
        map.put("Amount", mBinding.shenqingMoneyEdt.getText().toString());
        map.put("payDate", mBinding.shiyongDateTv.getText().toString());
        map.put("TaskId", TaskId);
        map.put("appname", mBinding.shenqingrenEdt.getText().toString());
        ////审核人
        map.put("auditor", auditorList);
        //抄送人
        map.put("copier", copierList);
        return map;

    }

    private boolean isCheck() {
     /*   if (TextUtils.isEmpty(mBinding.shenqingrenEdt.getText().toString())) {
            showToast("请填写申请人");
            return false;
        } else if (TextUtils.isEmpty(mBinding.bumTxt.getText().toString())) {
            showToast("请选择部门");
            return false;
        } else */

        if (TextUtils.isEmpty(mBinding.causeContent.getText().toString())) {
            showToast("请填写申请事由");
            return false;
        } else if (TextUtils.isEmpty(mBinding.shenqingMoneyEdt.getText().toString())) {
            showToast("请填写还款金额");
            return false;
        } else if (TextUtils.isEmpty(mBinding.shiyongDateTv.getText().toString())) {
            showToast("请填写日期");
            return false;
        }
      /*  else if (auditorList.size() == 0) {
            showToast("请选择审批人");
            return false;
        } else if (copierList.size() == 0) {
            showToast("请选择抄送人");
            return false;
        } */
        else {
            return true;
        }

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
}
