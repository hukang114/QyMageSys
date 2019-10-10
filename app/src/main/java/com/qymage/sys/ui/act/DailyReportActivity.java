package com.qymage.sys.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.DateUtil;
import com.qymage.sys.common.util.MeventKey;
import com.qymage.sys.common.util.MyEvtnTools;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityDailyReportBinding;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.entity.DayLogListEnt;
import com.qymage.sys.ui.entity.DayWeekMonthDet;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.HasClockOutEnt;
import com.qymage.sys.ui.entity.ProjecInfoEnt;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 日报提交
 */
public class DailyReportActivity extends BBActivity<ActivityDailyReportBinding> implements View.OnClickListener {


    // 项目编号下的所有合同数据
    List<ProjecInfoEnt.ContractListBean> contractListBeans = new ArrayList<>();
    private List<String> contractListString = new ArrayList<>();
    // 合同类型下面的合同编号
    List<ProjecInfoEnt.ContractListBean.ContractDetalBean> detalBeanList = new ArrayList<>();
    List<String> detaList = new ArrayList<>();

    List<ProjecInfoEnt> infoEnts = new ArrayList<>();

    List<GetTreeEnt> auditorList = new ArrayList<>();// 审核人
    List<GetTreeEnt> copierList = new ArrayList<>();// 抄送人
    AuditorListAdapter auditorListAdapter;// 审批人适配器
    CopierListAdapter copierListAdapter;// 抄送人适配器

    List<DayLogListEnt> dayLogListEnts = new ArrayList<>();// 合同明细数据
    List<DayLogListEnt.SubMoneyListEntity> defLogList;// 默认合同明细里面的报销明细数据
    List<String> defpiclist;// 默认报销数据里面的多图数据
    LogDailyDetailsAdapter adapter;
    Bundle bundle;
    List<String> proList = new ArrayList<>();// 合同编号 项目编号

    DayWeekMonthDet info;//日报详情页传递过来的复制数据
    private Intent mIntent;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_daily_report;
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
        mBinding.addHtmxBtn.setOnClickListener(this);
        mBinding.deleteBtn.setOnClickListener(this);
        LinearLayoutManager layouta = new LinearLayoutManager(this);
        layouta.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        LinearLayoutManager layoutb = new LinearLayoutManager(this);
        layoutb.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        mBinding.sprRecyclerview.setLayoutManager(layouta);
        mBinding.csrRecyclerview.setLayoutManager(layoutb);
        mBinding.sprImg.setOnClickListener(this);
        mBinding.csrImg.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);
        mBinding.htmxRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LogDailyDetailsAdapter(R.layout.item_log_daily_details, dayLogListEnts);
        mBinding.htmxRecyclerview.setAdapter(adapter);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            setOnItemChildClick(view, position);
        });
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
        // 默认初始化一条默认的合同数据
        defLogList = new ArrayList<>();
        defpiclist = new ArrayList<>();
        // 合同里面的报销明细
        defLogList.add(new DayLogListEnt.SubMoneyListEntity(
                ""
                , ""
                , ""
                , ""
                , defpiclist, ""));
        // 添加一条默认的合同空数据
        dayLogListEnts.add(new DayLogListEnt(
                ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , defLogList));
        adapter.notifyDataSetChanged();
        gethasClockOut();

        //添加日报提交默认审批人
        getAuditQuery(MainActivity.processDefId(AppConfig.btnType15));

        if (info != null) {
            setCopyData();
        }

    }

    /**
     * 设置复制数据的显示
     */
    private void setCopyData() {
        if (info.logList != null && info.logList.size() > 0) {
            dayLogListEnts.clear();
            for (int i = 0; i < info.logList.size(); i++) {
                DayLogListEnt dayLogListEnt = new DayLogListEnt();
                dayLogListEnt.contractName = info.logList.get(i).contractName;
                dayLogListEnt.contractNo = info.logList.get(i).contractNo;
                dayLogListEnt.contractType = info.logList.get(i).contractType;
                dayLogListEnt.contractTypeName = info.logList.get(i).contractTypeName;
                dayLogListEnt.projectName = info.logList.get(i).projectName;
                dayLogListEnt.projectNo = info.logList.get(i).projectNo;
                dayLogListEnt.subMoney = info.logList.get(i).subMoney;
                dayLogListEnt.manHours = info.logList.get(i).manHours;
                dayLogListEnt.workContent = info.logList.get(i).workContent;
                if (info.logList.get(i).subMoneyList != null && info.logList.get(i).subMoneyList.size() > 0) {
                    List<DayLogListEnt.SubMoneyListEntity> entities = new ArrayList<>();
                    for (int k = 0; k < info.logList.get(i).subMoneyList.size(); k++) {
                        DayLogListEnt.SubMoneyListEntity listEntity = new DayLogListEnt.SubMoneyListEntity();
                        listEntity.typeName = info.logList.get(i).subMoneyList.get(k).typeName;
                        listEntity.type = info.logList.get(i).subMoneyList.get(k).type;
                        listEntity.amount = info.logList.get(i).subMoneyList.get(k).amount;
                        listEntity.detailed = info.logList.get(i).subMoneyList.get(k).detailed;
                        listEntity.photo = "";
                        List<String> strings = new ArrayList<>();
                        listEntity.stringList = strings;
                        entities.add(listEntity);
                    }
                    dayLogListEnt.subMoneyList = entities;
                }
                dayLogListEnts.add(dayLogListEnt);
            }
        }
        adapter.notifyDataSetChanged();
        mBinding.mingriGongzuoJihua.setText(VerifyUtils.isEmpty(info.wordplay) ? "暂无" : info.wordplay);
        mBinding.benzhouGongzuozongjie.setText(VerifyUtils.isEmpty(info.jobWord) ? "暂无" : info.jobWord);
        mBinding.xiazhouJihua.setText(VerifyUtils.isEmpty(info.nextWork) ? "暂无" : info.nextWork);

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

    //查询是否有下班考勤数据
    private void gethasClockOut() {
        showLoading();
        HttpUtil.attendance_HasClockOut(new HashMap<>()).execute(new JsonCallback<Result<HasClockOutEnt>>() {
            @Override
            public void onSuccess(Result<HasClockOutEnt> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    mBinding.tv4x.setText(result.data.weekDay);
                    mBinding.tv42x.setText(result.data.dayWork);
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
            case R.id.add_htmx_btn:// 添加合同明细
                // 默认初始化一条默认的合同数据
                defLogList = new ArrayList<>();
                defpiclist = new ArrayList<>();
                // 合同里面的报销明细
                defLogList.add(new DayLogListEnt.SubMoneyListEntity(
                        ""
                        , ""
                        , ""
                        , ""
                        , defpiclist, ""));
                // 添加一条默认的合同空数据
                dayLogListEnts.add(new DayLogListEnt(
                        ""
                        , ""
                        , ""
                        , ""
                        , ""
                        , ""
                        , ""
                        , ""
                        , ""
                        , defLogList));
                adapter.notifyDataSetChanged();
                mBinding.deleteBtn.setVisibility(View.VISIBLE);
                break;
            case R.id.delete_btn:// 删除合同明细
                if (dayLogListEnts.size() > 1) { // 两条及以上数据
                    mBinding.deleteBtn.setVisibility(View.VISIBLE);
                    dayLogListEnts.remove(dayLogListEnts.size() - 1);
                    // 剩最后一条数据 隐藏删除按钮
                    if (dayLogListEnts.size() == 1) {
                        mBinding.deleteBtn.setVisibility(View.INVISIBLE);
                    }
                    adapter.notifyDataSetChanged();
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
//                showToast(DateUtil.DateToWeek(System.currentTimeMillis()));
                if (isCheck()) {
                    subMitData();
                }
                break;
        }

    }

    private void subMitData() {
        showLoading();
        HttpUtil.log_logAdd(getPer()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                closeLoading();
                // 更新日志列表
                EventBus.getDefault().post(new MyEvtnTools(MeventKey.ORDERUPDATE));
                msgDialogBuilder("日报请提交成功", (dialog, which) -> {
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


    private HashMap<String, Object> getPer() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userCode", getUserId());
        hashMap.put("date", DateUtil.formatMills(System.currentTimeMillis()));
        hashMap.put("logList", dayLogListEnts);
        hashMap.put("wordPlay", mBinding.mingriGongzuoJihua.getText().toString());
        hashMap.put("jobWord", mBinding.benzhouGongzuozongjie.getText().toString());
        hashMap.put("nextWork", mBinding.xiazhouJihua.getText().toString());
        hashMap.put("auditor", auditorList);
        hashMap.put("copier", copierList);
        return hashMap;
    }

    /**
     * 检查日志是否填写完成
     *
     * @return
     */
    private boolean isHtisEmpty() {
        boolean isEmpty = false;
        if (dayLogListEnts.size() > 0) {
            for (int i = 0; i < dayLogListEnts.size(); i++) {
                if (dayLogListEnts.get(i).contractNo == null || dayLogListEnts.get(i).contractNo.equals("")) {
                    showToast("请填写合同编号");
                    isEmpty = false;
                } else if (dayLogListEnts.get(i).manHours == null || dayLogListEnts.get(i).manHours.equals("")) {
                    showToast("请填写工时比例");
                    isEmpty = false;
                } else if (dayLogListEnts.get(i).workContent == null || dayLogListEnts.get(i).workContent.equals("")) {
                    showToast("请填写工作内容");
                    isEmpty = false;
                } else {
                    isEmpty = true;
                  /*  if (dayLogListEnts.get(i).subMoneyList.size() >= 0) {
                        for (int k = 0; k < dayLogListEnts.get(i).subMoneyList.size(); k++) {
                            if (dayLogListEnts.get(i).subMoneyList.get(k).amount == null || dayLogListEnts.get(i).subMoneyList.get(k).amount.equals("")) {
                                showToast("请填写工报销金额");
                                isEmpty = false;
                            } else if (dayLogListEnts.get(i).subMoneyList.get(k).typeName == null || dayLogListEnts.get(i).subMoneyList.get(k).typeName.equals("")) {
                                showToast("请填写工报销类型");
                                isEmpty = false;
                            } else if (dayLogListEnts.get(i).subMoneyList.get(k).detailed == null || dayLogListEnts.get(i).subMoneyList.get(k).detailed.equals("")) {
                                showToast("请填写明细描述");
                                isEmpty = false;
                            } else {
                                isEmpty = true;
                            }
                        }
                    } else {
                        isEmpty = true;
                    }*/
                }
            }
        } else {
            isEmpty = true;
        }
        return isEmpty;
    }


    private boolean isCheck() {
        if (!isHtisEmpty()) {
            return false;
        } else if (TextUtils.isEmpty(mBinding.mingriGongzuoJihua.getText().toString())) {
            showToast("请填写明日工作计划");
            return false;
        } else {
            if (DateUtil.DateToWeek(System.currentTimeMillis()).contains("星期五")) {
                if (TextUtils.isEmpty(mBinding.benzhouGongzuozongjie.getText().toString())) {
                    showToast("请填写本周工作总结");
                    return false;
                } else if (TextUtils.isEmpty(mBinding.xiazhouJihua.getText().toString())) {
                    showToast("请填写下周工作总结");
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 800) {
            List<DayLogListEnt.SubMoneyListEntity> list = (List<DayLogListEnt.SubMoneyListEntity>) data.getSerializableExtra("data");
            int select_Position = data.getIntExtra("select_Position", 0);
            dayLogListEnts.get(select_Position).subMoneyList = list;
            adapter.notifyDataSetChanged();
        } else if (resultCode == 300) { // 审核人
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

    //===============================================================================================

    /**
     * 合同明细适配器处理
     */
    public class LogDailyDetailsAdapter extends BaseQuickAdapter<DayLogListEnt, BaseViewHolder> {

        public LogDailyDetailsAdapter(int layoutResId, @Nullable List<DayLogListEnt> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DayLogListEnt item) {
            helper.setText(R.id.day_log_htbh_tv, item.contractNo)
                    .setText(R.id.hetong_mingchen, item.contractName)
                    .setText(R.id.contracttype, item.contractTypeName)
                    .setText(R.id.xiangmu_bianhao, item.projectNo)
                    .setText(R.id.xiangmu_mingchen, item.projectName)
                    .setText(R.id.gongshi_bili, item.manHours)
                    .setText(R.id.gongzuo_neirong, item.workContent)
                    .setText(R.id.baoxiao_feiyong, df.format(statisticsMoney(item.subMoneyList)));
            helper.addOnClickListener(R.id.baoxiao_feiyong).addOnClickListener(R.id.day_log_htbh_tv)
                    .addOnClickListener(R.id.hetong_mingchen).addOnClickListener(R.id.xiangmu_bianhao)
                    .addOnClickListener(R.id.xiangmu_mingchen);
            EditText gongshi_bili = helper.getView(R.id.gongshi_bili);
            EditText gongzuo_neirong = helper.getView(R.id.gongzuo_neirong);

            EditText xiangmu_bianhao = helper.getView(R.id.xiangmu_bianhao);
            EditText xiangmu_mingchen = helper.getView(R.id.xiangmu_mingchen);
            TextView contracttype = helper.getView(R.id.contracttype);

            xiangmu_bianhao.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!xiangmu_bianhao.getText().toString().equals("")) {
                        getProjectNo(1, xiangmu_bianhao.getText().toString(), helper.getAdapterPosition());
                    } else {
                        showToast("请输入搜索关键字");
                    }
                    return true;
                }
                return false;
            });
            xiangmu_mingchen.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!xiangmu_mingchen.getText().toString().equals("")) {
                        getProjectNo(2, xiangmu_mingchen.getText().toString(), helper.getAdapterPosition());
                    } else {
                        showToast("请输入搜索关键字");
                    }
                    return true;
                }
                return false;
            });

            contracttype.setOnClickListener(v -> {
                if (contractListBeans != null && contractListBeans.size() > 0) {
                    contractTypeNameDialog(1, helper.getAdapterPosition());
                }
            });

            gongshi_bili.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString() != null && !s.equals("")) {
                        dayLogListEnts.get(helper.getAdapterPosition()).manHours = s.toString();
                    } else {
                        dayLogListEnts.get(helper.getAdapterPosition()).manHours = "";
                    }
                }
            });
            gongzuo_neirong.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString() != null && !s.equals("")) {
                        dayLogListEnts.get(helper.getAdapterPosition()).workContent = s.toString();
                    } else {
                        dayLogListEnts.get(helper.getAdapterPosition()).workContent = "";
                    }
                }
            });

        }

    }

    /**
     * @param view
     * @param position
     */
    private void setOnItemChildClick(View view, int position) {
        switch (view.getId()) {
            case R.id.baoxiao_feiyong:
                bundle = new Bundle();
                bundle.putInt("select_Position", position);
                bundle.putSerializable("data", (Serializable) dayLogListEnts.get(position).subMoneyList);
                openActivity(ReimbursementActivity.class, bundle);
                break;
        }
    }


    /**
     * 统计每一条合同明细的报销总费用
     *
     * @param list
     * @return
     */
    private double statisticsMoney(List<DayLogListEnt.SubMoneyListEntity> list) {
        double money = 0;
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).amount != null && !list.get(i).amount.equals("")) {
                    money = money + Double.parseDouble(list.get(i).amount);
                }
            }
        }
        return money;
    }

    /**
     * 2.6根据合同编号/合同名称查询
     *
     * @param
     * @param contetn 内容
     */
    private void getProjectNo(int type, String contetn, int postion) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (type == 1) {
            hashMap.put("projectNo", contetn); // 项目编号
        }
        if (type == 2) {
            hashMap.put("projectName", contetn);//项目名称查询
        }
        showLoading();
        HttpUtil.getProjectNo(hashMap).execute(new JsonCallback<Result<List<ProjecInfoEnt>>>() {
            @Override
            public void onSuccess(Result<List<ProjecInfoEnt>> result, Call call, Response response) {
                closeLoading();
                if (result.data != null && result.data.size() > 0) {
                    infoEnts.clear();
                    infoEnts.addAll(result.data);
                    if (type == 1) {
                        proList.clear();
                        for (int i = 0; i < infoEnts.size(); i++) {
                            proList.add(infoEnts.get(i).projectNo);
                        }
                        setProDialog(type, proList, postion);
                    } else if (type == 2) {
                        proList.clear();
                        for (int i = 0; i < infoEnts.size(); i++) {
                            proList.add(infoEnts.get(i).projectName);
                        }
                        setProDialog(type, proList, postion);
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

    /**
     * 选择合同编号或者合同名称
     *
     * @param type
     * @param proList
     * @param postion
     */
    private void setProDialog(int type, List<String> proList, int postion) {
        String title = "请选择项目编号";
        if (type == 1) {
            title = "请选择项目编号";
        } else if (type == 2) {
            title = "请选择项目名称";
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            dayLogListEnts.get(postion).projectNo = infoEnts.get(options1).projectNo;
            dayLogListEnts.get(postion).projectName = infoEnts.get(options1).projectName;
            dayLogListEnts.get(postion).contractNo = infoEnts.get(options1).contractNo;
            dayLogListEnts.get(postion).contractName = infoEnts.get(options1).contractName;
            dayLogListEnts.get(postion).contractType = infoEnts.get(options1).contractType;
            dayLogListEnts.get(postion).contractTypeName = infoEnts.get(options1).contractTypeName;
            adapter.notifyDataSetChanged();
            if (infoEnts.get(options1).contractList != null && infoEnts.get(options1).contractList.size() > 0) {
                contractListBeans.clear();
                contractListBeans.addAll(infoEnts.get(options1).contractList);
                contractListString.clear();
                for (int i = 0; i < infoEnts.get(options1).contractList.size(); i++) {
                    contractListString.add(infoEnts.get(options1).contractList.get(i).contractTypeName);
                }
                contractTypeNameDialog(1, postion);
            }
        })
                .setTitleText(title)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(18)
                .setOutSideCancelable(false)//点击外部dismiss default true
                .setContentTextSize(16)//滚轮文字大小
                .setSubCalSize(16)//确定和取消文字大小
                .setLineSpacingMultiplier(2.4f)
                .build();
        // 三级选择器
        pvOptions.setPicker(proList, null, null);
        pvOptions.show();
    }

    /**
     * 选择合同类型
     */
    private void contractTypeNameDialog(int cate, int postion) {

        String title = "请选择合同类型";
        if (cate == 1) {
            title = "请选择合同类型";
        } else {
            title = "请选择合同编号";
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            if (cate == 1) {
                dayLogListEnts.get(postion).contractType = contractListBeans.get(options1).contractType;
                dayLogListEnts.get(postion).contractTypeName = contractListBeans.get(options1).contractTypeName;
                if (contractListBeans.get(options1).contractDetal != null && contractListBeans.get(options1).contractDetal.size() > 0) {
                    detalBeanList.clear();
                    detalBeanList.addAll(contractListBeans.get(options1).contractDetal);
                    detaList.clear();
                    for (int i = 0; i < detalBeanList.size(); i++) {
                        detaList.add(detalBeanList.get(i).contractNo);
                    }
                    contractTypeNameDialog(2, postion);
                }

            } else if (cate == 2) {
                dayLogListEnts.get(postion).contractNo = detalBeanList.get(options1).contractNo;
                dayLogListEnts.get(postion).contractName = detalBeanList.get(options1).contractName;
            }
            adapter.notifyDataSetChanged();
        })
                .setTitleText(title)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(18)
                .setOutSideCancelable(false)//点击外部dismiss default true
                .setContentTextSize(16)//滚轮文字大小
                .setSubCalSize(16)//确定和取消文字大小
                .setLineSpacingMultiplier(2.4f)
                .build();
        // 三级选择器
        if (cate == 1) {
            pvOptions.setPicker(contractListString, null, null);
            pvOptions.show();
        } else {
            pvOptions.setPicker(detaList, null, null);
            pvOptions.show();
        }

    }
    //------------------------------------------------------------------------------------------------


    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUtil.cancel(HttpConsts.ATTENDANCE_HASCLOCKOUT);
        HttpUtil.cancel(HttpConsts.LOG_LOGADD);
    }
}
