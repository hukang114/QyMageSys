package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.DateUtil;
import com.qymage.sys.databinding.ActivityOpenAfterWorkBinding;
import com.qymage.sys.ui.entity.CheckSettingInfo;
import com.qymage.sys.ui.entity.DaySearchEnt;

import java.util.Calendar;
import java.util.HashMap;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 上下班打卡
 */
public class OpenAfterWorkActivity extends BBActivity<ActivityOpenAfterWorkBinding> implements View.OnClickListener {


    private int clockType = 1;//考勤类型 1-在岗  2-出差  3-请假
    private int clockMode = 1;////考勤模式 1-上班打卡  2-下班打卡
    CheckSettingInfo settingInfo;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_after_work;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mBinding.dateWindow.setFormat24Hour("HH:mm:ss");
        }
        mBinding.kaoqingDateTv.setText(DateUtil.formatNYR(System.currentTimeMillis()));
        mBinding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.on_duty_btn:// 在岗
                    clockType = 1;
                    break;
                case R.id.business_travel_btn:// 出差
                    clockType = 2;
                    break;

                case R.id.leave_btn:// 请假
                    clockType = 3;
                    break;
            }
        });
        mBinding.clockImgBg.setOnClickListener(this);
        mBinding.locateBtn.setOnClickListener(this);
        mBinding.remarksBtn.setOnClickListener(this);
        mBinding.kaoqingDateTv.setOnClickListener(this);


    }

    @Override
    protected void initData() {
        super.initData();
        getSettingInfo();
        getDaySearch();
    }

    /**
     * 按天查询考勤
     */
    private void getDaySearch() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("clockDate", mBinding.kaoqingDateTv.getText().toString());
        showLoading();
        HttpUtil.attendance_DaySearch(map).execute(new JsonCallback<Result<DaySearchEnt>>() {
            @Override
            public void onSuccess(Result<DaySearchEnt> result, Call call, Response response) {
                closeLoading();
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
     * 获取考勤配置
     */
    private void getSettingInfo() {
        showLoading();
        HttpUtil.getSettingInfo(new HashMap<>()).execute(new JsonCallback<Result<CheckSettingInfo>>() {
            @Override
            public void onSuccess(Result<CheckSettingInfo> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    settingInfo = result.data;
                    mBinding.userNameTv.setText(result.data.userName);
                    mBinding.kaoqinRulesTv.setText("负责人:" + result.data.dutyUser + " " + result.data.clockGroup);
                    if (result.data.userName != null) {
                        if (result.data.userName.length() >= 3) {
                            String strh = result.data.userName.substring(result.data.userName.length() - 2, result.data.userName.length());   //截取
                            mBinding.nameTvBg.setText(strh);
                        } else {
                            mBinding.nameTvBg.setText(result.data.userName);
                        }
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

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clock_img_bg:// 打卡

                break;
            case R.id.locate_btn:// 从新定位

                break;
            case R.id.remarks_btn:// 备注
                showNotesDialog();
                break;
            case R.id.kaoqing_date_tv:// 选择日期
                showDateDialog();
                break;
        }
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            int months = month + 1;
            String monStr = months + "";
            int dayOfMonths = dayOfMonth;
            String dayOfMontStr = dayOfMonth + "";
            if (month + 1 < 10) {
                monStr = "0" + months;
            }
            if (dayOfMonths < 10) {
                dayOfMontStr = "0" + dayOfMonths;
            }
//            String desc = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
            mBinding.kaoqingDateTv.setText(year + "-" + monStr + "-" + dayOfMontStr);
            getDaySearch();
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


    /**
     * 显示备注输入框
     */
    private void showNotesDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.dialog_open_notes_layout, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        EditText editText = dialog.findViewById(R.id.notes_edt);
        TextView submit_btn = dialog.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(v -> {
            mBinding.remarksBtn.setText(editText.getText().toString());
            dialog.dismiss();
        });

    }


}
