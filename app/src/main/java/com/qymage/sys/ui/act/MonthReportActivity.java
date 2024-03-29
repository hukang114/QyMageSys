package com.qymage.sys.ui.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.MeventKey;
import com.qymage.sys.common.util.MyEvtnTools;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityMonthReportBinding;
import com.qymage.sys.ui.Test;
import com.qymage.sys.ui.Test2;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.entity.DayLogListEnt;
import com.qymage.sys.ui.entity.DayWeekMonthDet;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.YesQueryEny;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 月报提交
 */
public class MonthReportActivity extends BBActivity<ActivityMonthReportBinding> implements View.OnClickListener {


    List<YesQueryEny.WeekPalyListBean> weekPalyListBeans = new ArrayList<>();
    List<YesQueryEny.WeekTatalListBean> weekTatalListBeans = new ArrayList<>();
    List<GetTreeEnt> auditorList = new ArrayList<>();// 审核人
    List<GetTreeEnt> copierList = new ArrayList<>();// 抄送人
    AuditorListAdapter auditorListAdapter;// 审批人适配器
    CopierListAdapter copierListAdapter;// 抄送人适配器
    Bundle bundle;
    List<String> strings = new ArrayList<>();
    private String selg = "";
    DayWeekMonthDet info;// 月报详情复制操作传递过来的数据
    private Intent mIntent;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUtil.cancel(HttpConsts.LOG_MONADD);
        HttpUtil.cancel(HttpConsts.GETENUM);
        HttpUtil.cancel(HttpConsts.LOG_YESQUERY);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_month_report;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        try {
            info = (DayWeekMonthDet) mIntent.getSerializableExtra("data");
        } catch (Exception e) {

        }
        mBinding.zhoujihua.setOnClickListener(this);
        mBinding.zhouzongjie.setOnClickListener(this);
        LinearLayoutManager layouta = new LinearLayoutManager(this);
        layouta.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        LinearLayoutManager layoutb = new LinearLayoutManager(this);
        layoutb.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        mBinding.sprRecyclerview.setLayoutManager(layouta);
        mBinding.csrRecyclerview.setLayoutManager(layoutb);
        mBinding.sprImg.setOnClickListener(this);
        mBinding.csrImg.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);
        mBinding.xiangmuBianhao.setOnClickListener(this);
        // 审批人
        auditorListAdapter = new AuditorListAdapter(R.layout.item_list_auditor, auditorList);
        mBinding.sprRecyclerview.setAdapter(auditorListAdapter);
        auditorListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.caccel_ioc:
                    if (auditorList.get(position).isDef) {
                        showToast("默认审批人不能移除");
                    } else {
                        auditorList.remove(position);
                        auditorListAdapter.notifyDataSetChanged();
                    }
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
        // 获取打分数据
        getleadType();
        //添加月报提交默认审批人
        getAuditQuery(MainActivity.processDefId(AppConfig.btnType16));
        // 获取月报数据展示
        yesQuery();
        if (info != null) {
            setCopyData();
        }

    }

    /**
     * 设置复制操作的数据显示
     */
    private void setCopyData() {
        mBinding.tv42x.setText(VerifyUtils.isEmpty(info.submonthWork) ? "暂无" : info.submonthWork);
        mBinding.tv42xm.setText(VerifyUtils.isEmpty(info.nextMonthWeek) ? "暂无" : info.nextMonthWeek);
        mBinding.xiangmuBianhao.setText(VerifyUtils.isEmpty(info.selg) ? "暂无" : info.selg);
    }

    /**
     * 审批人的获取回调
     *
     * @param listdata
     */
    @Override
    protected void getAuditQuerySuccess(List<GetTreeEnt> listdata) {
        // 加上默认审批人的标识
        for (int i = 0; i < listdata.size(); i++) {
            GetTreeEnt ent = new GetTreeEnt();
            ent.isDef = true; // 默认审批人
            ent.userId = listdata.get(i).userId;
            ent.userName = listdata.get(i).userName;
            auditorList.add(ent);
        }
        auditorListAdapter.notifyDataSetChanged();
    }

    /**
     * 月报提交参数
     *
     * @return
     */
    private HashMap<String, Object> getPar() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("usercode", getUserId());
        hashMap.put("submonthWork", mBinding.tv42x.getText().toString());
        hashMap.put("nextMonthWeek", mBinding.tv42xm.getText().toString());
        hashMap.put("selg", mBinding.xiangmuBianhao.getText().toString());
        hashMap.put("auditor", auditorList);
        hashMap.put("copier", copierList);
        return hashMap;
    }


    /**
     * 获取月报的数据展示
     */
    private void yesQuery() {
        showLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userCode", getUserId());
        HttpUtil.yesQuery(hashMap).execute(new JsonCallback<Result<YesQueryEny>>() {
            @Override
            public void onSuccess(Result<YesQueryEny> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    if (result.data.weekPalyList != null) {
                        weekPalyListBeans.clear();
                        weekPalyListBeans.addAll(result.data.weekPalyList);
                    }
                    if (result.data.weekTatalList != null) {
                        weekTatalListBeans.clear();
                        weekTatalListBeans.addAll(result.data.weekTatalList);
                    }
                    if (result.data.monthWork != null) {
                        mBinding.tv4x.setText(result.data.monthWork);
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
            case R.id.zhoujihua:// 周计划
                if (weekPalyListBeans.size() > 0) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) weekPalyListBeans);
                    bundle.putString("type", "1");
                    openActivity(WeeklySummWeeklyPlanActivity.class, bundle);
                } else {
                    showToast("暂无周计划数据记录");
                }
                break;
            case R.id.zhouzongjie:// 周总结
                if (weekTatalListBeans.size() > 0) {
                    List<YesQueryEny.WeekPalyListBean> list = new ArrayList<>();
                    for (int i = 0; i < weekTatalListBeans.size(); i++) {
                        YesQueryEny.WeekPalyListBean bean = new YesQueryEny.WeekPalyListBean();
                        bean.createTime = weekTatalListBeans.get(i).createTime;
                        bean.nextWeek = weekTatalListBeans.get(i).weekDay;
                        list.add(bean);
                    }
                    bundle = new Bundle();
                    bundle.putString("type", "2");
                    bundle.putSerializable("data", (Serializable) list);
                    openActivity(WeeklySummWeeklyPlanActivity.class, bundle);
                } else {
                    showToast("暂无周总结数据");
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
            case R.id.save_btn:
                if (isCheck()) {
                    subMitData();
                }
                break;
            case R.id.xiangmu_bianhao:
                setMemberLevel();
                break;
        }
    }


    private void setMemberLevel() {
        strings.clear();
        for (int i = 0; i < leaveTypes.size(); i++) {
            strings.add(leaveTypes.get(i).label);
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            selg = leaveTypes.get(options1).value;
            mBinding.xiangmuBianhao.setText(strings.get(options1));
        })
                .setTitleText("请选择评级等级")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(18)
                .setOutSideCancelable(false)//点击外部dismiss default true
                .setContentTextSize(16)//滚轮文字大小
                .setSubCalSize(16)//确定和取消文字大小
                .setLineSpacingMultiplier(2.4f)
                .build();
        // 三级选择器
        pvOptions.setPicker(strings, null, null);
        pvOptions.show();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 300) { // 审核人
            List<GetTreeEnt> list = (List<GetTreeEnt>) data.getSerializableExtra("data");
            if (list.size() > 0) {
                if (auditorList.size() > 0) {
                    for (GetTreeEnt gteTar : list) {
                        boolean flag = false;
                        for (GetTreeEnt gte : auditorList) {
                            if (gteTar.userId.toString().trim().equals(gte.userId.toString().trim())) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            auditorList.add(gteTar);
                        }
                    }
                } else {
                    auditorList.clear();
                    auditorList.addAll(list);
                }
            }
            auditorListAdapter.notifyDataSetChanged();

        } else if (resultCode == 400) { // 抄送人
            List<GetTreeEnt> list = (List<GetTreeEnt>) data.getSerializableExtra("data");
            copierList.clear();
            copierList.addAll(list);
            copierListAdapter.notifyDataSetChanged();
        }

    }


    private void subMitData() {
        showLoading();
        HttpUtil.monAdd(getPar()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                // 更新月报列表
                EventBus.getDefault().post(new MyEvtnTools(MeventKey.ORDERUPDATE));
                msgNocanseDialogBuilder("月报提交成功！", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).create().show();
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
        if (TextUtils.isEmpty(mBinding.tv42x.getText().toString())) {
            showToast("请填写本周工作总结");
            return false;
        } else if (TextUtils.isEmpty(mBinding.tv42xm.getText().toString())) {
            showToast("请填写下周工作计划");
            return false;
        } else if (auditorList.size() == 0) {
            showToast("请填写审批人");
            return false;
        } else {
            return true;
        }
    }

}
