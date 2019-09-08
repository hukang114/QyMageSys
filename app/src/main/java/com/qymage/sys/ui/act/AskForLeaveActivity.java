package com.qymage.sys.ui.act;

import android.content.DialogInterface;
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
import com.qymage.sys.common.datepicker.CustomDatePicker;
import com.qymage.sys.common.datepicker.DateFormatUtils;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivityAskForLeaveBinding;
import com.qymage.sys.databinding.ActivityChangePasswordBinding;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.entity.FileListEnt;
import com.qymage.sys.ui.entity.GetTreeEnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 请假申请
 */

public class AskForLeaveActivity extends BBActivity<ActivityAskForLeaveBinding> implements View.OnClickListener {


    List<GetTreeEnt> auditorList = new ArrayList<>();// 审核人
    List<GetTreeEnt> copierList = new ArrayList<>();// 抄送人
    AuditorListAdapter auditorListAdapter;// 审批人适配器
    CopierListAdapter copierListAdapter;// 抄送人适配器
    List<FileListEnt> fileList = new ArrayList<>();// 上传附件

    private Bundle bundle;
    private List<String> protypelist = new ArrayList<>();
    private String leaveType = "1";// /请假类型
    private String department = "技术部";
    private CustomDatePicker mDatePicker, mTimerPicker;
    private long begintime = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ask_for_leave;
    }


    @Override
    protected void initView() {
        // 关闭当前的页面
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.metitle.setrTxtClick(v -> {
            openActivity(AskForLeaveRecordlActivity.class);
        });
        LinearLayoutManager layouta = new LinearLayoutManager(this);
        layouta.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        LinearLayoutManager layoutb = new LinearLayoutManager(this);
        layoutb.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        mBinding.sprImg.setOnClickListener(this);
        mBinding.csrImg.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);
        mBinding.xuanzeShijianEnd.setOnClickListener(this);
        mBinding.xuanzeShijianStart.setOnClickListener(this);
        mBinding.xuanzeQingjialeixing.setOnClickListener(this);
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
        protypelist.add("年假");
        protypelist.add("事假");
        protypelist.add("病假");
        fileList.add(new FileListEnt("123", "www.baidu.com"));
    }


    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xuanze_shijian_start:
                initTimerPicker(1);
                mTimerPicker.show("yyyy-MM-dd HH:mm");
                break;
            case R.id.xuanze_shijian_end:
                initTimerPicker(2);
                mTimerPicker.show("yyyy-MM-dd HH:mm");
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
            case R.id.save_btn:
                if (isCheck()) {
                    subMitData();
                }
                break;
            case R.id.xuanze_qingjialeixing:// 选择请假类型
                setOnClick();
                break;
        }
    }


    /**
     * 请假类型
     */
    private void setOnClick() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) ->
                mBinding.xuanzeQingjialeixing.setText(protypelist.get(options1)))
                .setTitleText("请选择请假类型")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(18)
                .setOutSideCancelable(false)//点击外部dismiss default true
                .setContentTextSize(16)//滚轮文字大小
                .setSubCalSize(16)//确定和取消文字大小
                .setLineSpacingMultiplier(2.4f)
                .build();
        // 三级选择器
        pvOptions.setPicker(protypelist, null, null);
        pvOptions.show();

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


    private HashMap<String, Object> getPer() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", getUserId());
        hashMap.put("department", department);
        hashMap.put("leaveType", leaveType);
        hashMap.put("startDate", mBinding.xuanzeShijianStart.getText().toString());
        hashMap.put("endDate", mBinding.xuanzeShijianEnd.getText().toString());
        hashMap.put("ofTime", mBinding.shuruShichang.getText().toString());
        hashMap.put("cause", mBinding.qingjiaShiyou.getText().toString());
        hashMap.put("fileList", fileList);
        hashMap.put("auditor", auditorList);
        hashMap.put("copier", copierList);
        return hashMap;
    }


    private void subMitData() {
        showLoading();
        HttpUtil.leave_Submit(getPer()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgDialogBuilder("请假申请提交成功", (dialog, which) -> {
                    dialog.dismiss();
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


    private boolean isCheck() {
        if (TextUtils.isEmpty(mBinding.xuanzeQingjialeixing.getText().toString())) {
            showToast("请选择请假类型");
            return false;
        } else if (TextUtils.isEmpty(mBinding.xuanzeShijianStart.getText().toString())) {
            showToast("请选择开始时间");
            return false;
        } else if (TextUtils.isEmpty(mBinding.xuanzeShijianEnd.getText().toString())) {
            showToast("请选择结束时间");
            return false;
        } else if (TextUtils.isEmpty(mBinding.shuruShichang.getText().toString())) {
            showToast("请输入时长");
            return false;
        } else if (TextUtils.isEmpty(mBinding.qingjiaShiyou.getText().toString())) {
            showToast("请输请假事由");
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

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        mBinding.xuanzeShijianStart.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mBinding.xuanzeShijianStart.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }

    private void initTimerPicker(int type) {

        String beginTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        String endTime = "2030-12-31 00:00";
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                if (type == 1) {
                    begintime = timestamp;
                    mBinding.xuanzeShijianStart.setText(DateFormatUtils.long2Str(timestamp, true));
                } else {
                    if (timestamp < begintime) {
                        showToast("结束时间不能小于开始时间");
                    } else {
                        mBinding.xuanzeShijianEnd.setText(DateFormatUtils.long2Str(timestamp, true));
                    }
                }
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(false);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mDatePicker.onDestroy();
    }

}
