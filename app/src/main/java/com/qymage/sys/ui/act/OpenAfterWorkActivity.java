package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
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

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hjq.permissions.XXPermissions;
import com.qymage.sys.AppApplication;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.http.LogUtils;
import com.qymage.sys.common.util.DateUtil;
import com.qymage.sys.common.util.SjLocationService;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityOpenAfterWorkBinding;
import com.qymage.sys.ui.entity.CheckSettingInfo;
import com.qymage.sys.ui.entity.DaySearchEnt;
import com.qymage.sys.ui.entity.RankBackEnt;
import com.qymage.sys.ui.entity.RankCalculateEnt;

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
    private double latitude = 0;// 维度
    private double longitude = 0;// 精度
    private String locationaddress = "";// 定位的当前位置
    DaySearchEnt dayEnt;// 当天的考勤信息
    RankCalculateEnt calculateEnt;// 计算过的考勤信息

    //-----------------------
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener;
    private boolean isFirst = true;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        if (mLocClient != null && myListener != null) {
            mLocClient.stop();
            mLocClient.unRegisterLocationListener(myListener);
            myListener = null;
        }

    }

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
            getDaySearch();
        });
        mBinding.clockImgBg.setOnClickListener(this);
        mBinding.locateBtn.setOnClickListener(this);
        mBinding.remarksBtn.setOnClickListener(this);
        mBinding.kaoqingDateTv.setOnClickListener(this);
        mBinding.locateBtn.setOnClickListener(this);
        initLocation();

    }

    @Override
    protected void initData() {
        super.initData();
        getSettingInfo();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner extends BDAbstractLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            closeLoading();
            if (location == null || location.getLatitude() == 4.9E-324 || location.getAddrStr() == null) {
                mBinding.locationAddressTv.setText("手机定位获取位置失败,检查定位服务是否开启高精度模式");
                msgDialogBuilder("请打开系统设置查看手机是否允许获取位置权限?", (dialog, which) -> {
                    //如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.gotoPermissionSettings(OpenAfterWorkActivity.this);
                }).create().show();
                LogUtils.e("定位失败");
                return;
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            locationaddress = location.getAddrStr();
            mBinding.locationAddressTv.setText("当前位置" + locationaddress);
            LogUtils.e("当前位置：" + locationaddress);
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
            if (mLocClient != null) {
                mLocClient.stop();
                mLocClient.unRegisterLocationListener(myListener);
                myListener = null;
            }
            if (!isFirst) {
                showToast("位置刷新成功");
            }
            isFirst = false;
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }


    private void initLocation() {
        // 定位初始化
        mLocClient = new LocationClient(this);
        myListener = new MyLocationListenner();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        option.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mLocClient.setLocOption(option);
        showLoading();
        mLocClient.start();

    }

    // handler对象，用来接收消息~
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    getDaySearch();
                    break;
            }

        }
    };


    /**
     * 获取考情的计算内容相关
     */
    private void attendance_Calculate() {
        HttpUtil.attendance_Calculate(getPer()).execute(new JsonCallback<Result<RankCalculateEnt>>() {
            @Override
            public void onSuccess(Result<RankCalculateEnt> result, Call call, Response response) {
                if (result.data != null) {
                    calculateEnt = result.data;
                    if (clockMode == 1) {
                        if (clockType == 1) { // 在岗
                            if (result.data.clockInScope == 1) { // 考勤范围 1-在范围、0-不在范围
                                if (result.data.clockInStatus == 1) { //考勤状态：1-正常、2-迟到、3-早退、4-旷工
                                    mBinding.clockImgBg.setImageResource(R.mipmap.normal_dk_bg);
                                    mBinding.toWorkTv.setText("上班打卡");
                                } else if (result.data.clockInStatus == 2) {
                                    mBinding.toWorkTv.setText("迟到打卡");
                                    mBinding.clockImgBg.setImageResource(R.mipmap.abnormal_dk_bg);
                                } else if (result.data.clockInStatus == 3) {
                                    mBinding.toWorkTv.setText("早退打卡");
                                    mBinding.clockImgBg.setImageResource(R.mipmap.abnormal_dk_bg);
                                } else if (result.data.clockInStatus == 4) {
                                    mBinding.clockImgBg.setImageResource(R.mipmap.abnormal_dk_bg);
                                    mBinding.toWorkTv.setText("旷工打卡");
                                }
                                mBinding.locationAddressTv.setText("已进入考勤范围" + settingInfo.address);
                            } else {
                                mBinding.clockImgBg.setImageResource(R.mipmap.abnormal_dk_bg);
                                mBinding.toWorkTv.setText("异常打开");
                                String clockInAddr = VerifyUtils.isEmpty(settingInfo) ? "" : settingInfo.address;
                                mBinding.locationAddressTv.setText("不在打卡考勤范围内" + "\n考勤范围:" + clockInAddr);
                            }
                        } else if (clockType == 2) {
                            mBinding.toWorkTv.setText("出差打卡");
                            mBinding.clockImgBg.setImageResource(R.mipmap.normal_dk_bg);
                        } else if (clockType == 3) {
                            mBinding.toWorkTv.setText("请假打卡");
                            mBinding.clockImgBg.setImageResource(R.mipmap.normal_dk_bg);
                        }

                    } else if (clockMode == 2) { // 下班
                        if (clockType == 1) { // 在岗
                            if (result.data.clockOutScope == 1) { // 考勤范围 1-在范围、0-不在范围
                                if (result.data.clockOutStatus == 1) { //考勤状态：1-正常、2-迟到、3-早退、4-旷工
                                    mBinding.clockImgBg.setImageResource(R.mipmap.normal_dk_bg);
                                    mBinding.toWorkTv.setText("下班打卡");
                                } else if (result.data.clockOutStatus == 2) {
                                    mBinding.toWorkTv.setText("迟到打卡");
                                    mBinding.clockImgBg.setImageResource(R.mipmap.abnormal_dk_bg);
                                } else if (result.data.clockOutStatus == 3) {
                                    mBinding.toWorkTv.setText("早退打卡");
                                    mBinding.clockImgBg.setImageResource(R.mipmap.abnormal_dk_bg);
                                } else if (result.data.clockOutStatus == 4) {
                                    mBinding.clockImgBg.setImageResource(R.mipmap.abnormal_dk_bg);
                                    mBinding.toWorkTv.setText("旷工打卡");
                                }
                                mBinding.locationAddressTv.setText("已进入考勤范围" + settingInfo.address);
                            } else {
                                mBinding.clockImgBg.setImageResource(R.mipmap.abnormal_dk_bg);
                                mBinding.toWorkTv.setText("异常打开");
                                String clockInAddr = VerifyUtils.isEmpty(settingInfo) ? "" : settingInfo.address;
                                mBinding.locationAddressTv.setText("不在打卡考勤范围内" + "\n考勤范围:" + clockInAddr);
                            }
                        } else if (clockType == 2) {
                            mBinding.toWorkTv.setText("出差打卡");
                            mBinding.clockImgBg.setImageResource(R.mipmap.normal_dk_bg);
                        } else if (clockType == 3) {
                            mBinding.toWorkTv.setText("请假打卡");
                            mBinding.clockImgBg.setImageResource(R.mipmap.normal_dk_bg);
                        }

                    }
                }


            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 考勤计算参数
     *
     * @return
     */
    private HashMap<String, Object> getPer() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("clockType", clockType);// //考勤类型 1-在岗  2-出差  3-请假
        hashMap.put("clockMode", clockMode);//考勤模式 1-上班打卡  2-下班打卡
        hashMap.put("address", locationaddress);
        hashMap.put("longitude", longitude);
        hashMap.put("latitude", latitude);
        return hashMap;
    }


    /**
     * 按天查询考勤
     */
    private void getDaySearch() {
        showLoading();
        LogUtils.e("定位成功按天查询考勤");
        HashMap<String, Object> map = new HashMap<>();
        map.put("clockDate", mBinding.kaoqingDateTv.getText().toString());
        HttpUtil.attendance_DaySearch(map).execute(new JsonCallback<Result<DaySearchEnt>>() {
            @Override
            public void onSuccess(Result<DaySearchEnt> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    dayEnt = result.data;
                    //是否当天时间 1-是、0-否
                    if (dayEnt.isCurrentDate == 1) {
                        // 显示上班信息
                        mBinding.workShiftClockTime.setVisibility(View.GONE);
                        mBinding.workShiftAddressTv.setVisibility(View.GONE);
                        mBinding.centetDiv.setVisibility(View.GONE);
                        // 下班
                        // 显示下班信息
                        mBinding.tvDotE.setVisibility(View.GONE);
                        mBinding.closingRelayout.setVisibility(View.GONE);
                        //-------------
                        mBinding.dakaRelayout.setVisibility(View.VISIBLE);
                        mBinding.radioGroup.setVisibility(View.VISIBLE);
                        if (dayEnt.clockInTime == null || dayEnt.clockInTime.equals("")) { // 上班打卡时间不存在，
                            clockMode = 1;// 显示上班打卡
                            mBinding.toWorkTv.setText("上班打开");
                            mBinding.metitle.setcTxt("上班打开");
                        } else {
                            // 显示下班打卡
                            // 显示下班信息
                            mBinding.workShiftClockTime.setVisibility(View.VISIBLE);
                            mBinding.workShiftAddressTv.setVisibility(View.VISIBLE);
                            mBinding.centetDiv.setVisibility(View.VISIBLE);
                            if (dayEnt.clockOutTime == null || dayEnt.clockOutTime.equals("")) {
                                clockMode = 2;
                                mBinding.toWorkTv.setText("下班打开");
                                mBinding.metitle.setcTxt("下班打开");
                                // 显示下班打开时间
                                mBinding.tvDotE.setVisibility(View.VISIBLE);
                                mBinding.closingRelayout.setVisibility(View.VISIBLE);
                            } else { // 打卡流程已完成
                                // 显示下班信息
                                mBinding.tvDotE.setVisibility(View.VISIBLE);
                                mBinding.closingRelayout.setVisibility(View.VISIBLE);
                                clockMode = 1;
                                // 隐藏打卡按钮
                                mBinding.dakaRelayout.setVisibility(View.GONE);
                                // 隐藏打卡类型
                                mBinding.radioGroup.setVisibility(View.GONE);
                                mBinding.metitle.setcTxt("考勤打卡");
                            }
                        }
                    } else { // 不是当天
                        // 隐藏打卡按钮
                        mBinding.dakaRelayout.setVisibility(View.GONE);
                        // 隐藏打卡类型
                        mBinding.radioGroup.setVisibility(View.GONE);
                        mBinding.metitle.setcTxt("考勤打卡");
                        // 显示上班信息
                        mBinding.workShiftClockTime.setVisibility(View.VISIBLE);
                        mBinding.workShiftAddressTv.setVisibility(View.VISIBLE);
                        mBinding.centetDiv.setVisibility(View.VISIBLE);
                        // 下班
                        // 显示下班信息
                        mBinding.tvDotE.setVisibility(View.VISIBLE);
                        mBinding.closingRelayout.setVisibility(View.VISIBLE);
                    }

                    if (dayEnt.isCurrentDate == 1) {
                        attendance_Calculate();
                    }
                    setDayInfoShow();
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
     * 设置当前查询的考勤信息的显示
     */
    private void setDayInfoShow() {
        //============上班相关======================
        mBinding.workShiftTimeTv.setText("上班时间：" + dayEnt.beginTime);

        String clockInTime = VerifyUtils.isEmpty(dayEnt.clockInTime) ? "打卡时间：" : "打卡时间：" + dayEnt.clockInTime;
        mBinding.workShiftClockTime.setText(clockInTime);
        // 上班考勤类型 1-在岗  2-出差  3-请假
        String clockInType = VerifyUtils.isEmpty(dayEnt.clockInType) ? "" : 1 == dayEnt.clockInType ? "在岗" : 2 == dayEnt.clockInType ? "出差" :
                3 == dayEnt.clockInType ? "请假" : "";
        // 上班考勤状态
        String clockInStatus = VerifyUtils.isEmpty(dayEnt.clockInStatus) ? "" : 1 == dayEnt.clockInStatus ? "正常" : 2 == dayEnt.clockInStatus ? "迟到" :
                3 == dayEnt.clockInStatus ? "早退" : 4 == dayEnt.clockInStatus ? "旷工" : "";
        //上班考勤结果
        String clockInResult = VerifyUtils.isEmpty(dayEnt.clockInResult) ? "" : "迟到" + dayEnt.clockInResult + "分钟";
        if (dayEnt.clockInStatus == 1) {
            clockInResult = "";
        } else if (dayEnt.clockInStatus == 2) {
            clockInResult = "迟到" + dayEnt.clockInResult + "分钟";
        } else if (dayEnt.clockInStatus == 3) {
            clockInResult = "早退" + dayEnt.clockInResult + "分钟";
        } else if (dayEnt.clockInStatus == 4) {
            clockInResult = "旷工" + dayEnt.clockInResult + "天";
        }
        // 上班考勤地址
        String clockInAddr = VerifyUtils.isEmpty(dayEnt.clockInAddr) ? "" : "打卡地址：" + dayEnt.clockInAddr;
        // 上班打卡备注
        String clockInRemark = VerifyUtils.isEmpty(dayEnt.clockInRemark) ? "" : "备注：" + dayEnt.clockInRemark;
        // 设置上班打卡状态显示
        mBinding.workShiftAddressTv.setText(clockInType + " " + clockInStatus + " " + clockInResult + "\n" + clockInAddr + " \n" + clockInRemark);

        // =============下班相关==================

        mBinding.closingTimeTv.setText("下班时间：" + dayEnt.endTime);
        String clockOutTime = VerifyUtils.isEmpty(dayEnt.clockOutTime) ? "打卡时间：" : "打卡时间：" + dayEnt.clockOutTime;
        mBinding.closingClockTime.setText(clockOutTime);

        // 下班考勤类型 1-在岗  2-出差  3-请假
        String clockOutType = VerifyUtils.isEmpty(dayEnt.clockOutType) ? "" : 1 == dayEnt.clockOutType ? "在岗" : 2 == dayEnt.clockOutType ? "出差" :
                3 == dayEnt.clockOutType ? "请假" : "";
        // 下班考勤状态
        String clockOutStatus = VerifyUtils.isEmpty(dayEnt.clockOutStatus) ? "" : 1 == dayEnt.clockOutStatus ? "正常" : 2 == dayEnt.clockOutStatus ? "迟到" :
                3 == dayEnt.clockOutStatus ? "早退" : 4 == dayEnt.clockOutStatus ? "旷工" : "";
        //下班考勤结果
        String clockOutResult = VerifyUtils.isEmpty(dayEnt.clockOutResult) ? "" : "早退" + dayEnt.clockOutResult + "分钟";
        if (dayEnt.clockOutStatus == 1) {
            clockOutResult = "";
        } else if (dayEnt.clockOutStatus == 2) {
            clockOutResult = "迟到" + dayEnt.clockOutResult + "分钟";
        } else if (dayEnt.clockOutStatus == 3) {
            clockOutResult = "早退" + dayEnt.clockOutResult + "分钟";
        } else if (dayEnt.clockOutStatus == 4) {
            clockOutResult = "旷工" + dayEnt.clockOutResult + "天";
        }
        // 下班考勤地址
        String clockOutAddr = VerifyUtils.isEmpty(dayEnt.clockOutAddr) ? "" : "打卡地址：" + dayEnt.clockOutAddr;
        // 下班打卡备注
        String clockOutRemark = VerifyUtils.isEmpty(dayEnt.clockOutRemark) ? "" : "备注：" + dayEnt.clockOutRemark;
        // 设置下班打卡状态显示
        mBinding.closingAddressTv.setText(clockOutType + " " + clockOutStatus + " " + clockOutResult + "\n" + clockOutAddr + " \n" + clockOutRemark);

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
                msgDialogBuilder(e.getMessage(), (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).create().show();
            }
        });


    }

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clock_img_bg:// 打卡
                if (locationaddress != null && !locationaddress.equals("") && calculateEnt != null) {
                    attendance_Clock();
                } else {
                    showToast("获取打卡信息失败");
                }

                break;
            case R.id.locate_btn:// 从新定位
                initLocation();
                break;
            case R.id.remarks_btn:// 备注
                showNotesDialog();
                break;
            case R.id.kaoqing_date_tv:// 选择日期
                showDateDialog();
                break;
        }
    }

    /**
     * 打卡
     */
    private void attendance_Clock() {
        showLoading();
        HttpUtil.attendance_Clock(dakaPer()).execute(new JsonCallback<Result<RankBackEnt>>() {
            @Override
            public void onSuccess(Result<RankBackEnt> result, Call call, Response response) {
                closeLoading();
                getDaySearch();
                showToast("打卡成功");
                if (clockMode == 2) { // 下班打卡成功，填写日志
                    openActivity(DailyReportActivity.class);
                } else { // 上班打卡成功
                    String dayWork = VerifyUtils.isEmpty(result.data.dayWork) ? "暂无" : result.data.dayWork;
                    msgNoTitleDialog("今日工作计划 \n\n[" + dayWork + "]");
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

    private HashMap<String, Object> dakaPer() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("clockType", clockType);
        hashMap.put("clockMode", clockMode);
        hashMap.put("address", locationaddress);
        hashMap.put("remarks", mBinding.remarksBtn.getText().toString());
        hashMap.put("latitude", latitude);
        hashMap.put("longitude", longitude);
        if (clockMode == 1) { // 上班
            hashMap.put("scope", calculateEnt.clockInScope);
        } else if (clockMode == 2) { // 下班
            hashMap.put("scope", calculateEnt.clockOutScope);
        }
        return hashMap;

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
