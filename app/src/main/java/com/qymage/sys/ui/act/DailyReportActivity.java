package com.qymage.sys.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.DateUtil;
import com.qymage.sys.databinding.ActivityDailyReportBinding;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.entity.DayLogListEnt;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.ProjecInfoEnt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 日报
 */
public class DailyReportActivity extends BBActivity<ActivityDailyReportBinding> implements View.OnClickListener {


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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_daily_report;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
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
        // 默认初始化一条默认的合同数据
        defLogList = new ArrayList<>();
        defpiclist = new ArrayList<>();
        // 合同里面的报销明细
        defLogList.add(new DayLogListEnt.SubMoneyListEntity(
                ""
                , ""
                , ""
                , ""
                , defpiclist));
        // 添加一条默认的合同空数据
        dayLogListEnts.add(new DayLogListEnt(
                ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , ""
                , defLogList));
        adapter.notifyDataSetChanged();


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
                        , defpiclist));
                // 添加一条默认的合同空数据
                dayLogListEnts.add(new DayLogListEnt(
                        ""
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
                if (isCheck()) {
                    subMitData();
                }

        }

    }

    private void subMitData() {
        showLoading();
        HttpUtil.log_logAdd(getPer()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                closeLoading();
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


    private boolean isCheck() {
        return true;
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
            auditorList.clear();
            auditorList.addAll(list);
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

            EditText day_log_htbh_tv = helper.getView(R.id.day_log_htbh_tv);
            EditText hetong_mingchen = helper.getView(R.id.hetong_mingchen);

            day_log_htbh_tv.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!day_log_htbh_tv.getText().toString().equals("")) {
                        getContract(1, day_log_htbh_tv.getText().toString(), helper.getAdapterPosition());
                    } else {
                        showToast("请输入搜索关键字");
                    }
                    return true;
                }
                return false;
            });
            hetong_mingchen.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!hetong_mingchen.getText().toString().equals("")) {
                        getContract(2, hetong_mingchen.getText().toString(), helper.getAdapterPosition());
                    } else {
                        showToast("请输入搜索关键字");
                    }
                    return true;
                }
                return false;
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
    private void getContract(int type, String contetn, int postion) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (type == 1) {
            hashMap.put("contractNo", contetn); // 合同编号
        }
        if (type == 2) {
            hashMap.put("contractName", contetn);//合同名称查询
        }
        showLoading();
        HttpUtil.getContract(hashMap).execute(new JsonCallback<Result<List<ProjecInfoEnt>>>() {
            @Override
            public void onSuccess(Result<List<ProjecInfoEnt>> result, Call call, Response response) {
                closeLoading();
                if (result.data != null && result.data.size() > 0) {
                    infoEnts.clear();
                    infoEnts.addAll(result.data);
                    if (type == 1) {
                        proList.clear();
                        for (int i = 0; i < infoEnts.size(); i++) {
                            proList.add(infoEnts.get(i).contractNo);
                        }
                        setProDialog(type, proList, postion);
                    } else if (type == 2) {
                        proList.clear();
                        for (int i = 0; i < infoEnts.size(); i++) {
                            proList.add(infoEnts.get(i).contractName);
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
        String title = "请选择合同编号";
        if (type == 1) {
            title = "请选择合同编号";
        } else if (type == 2) {
            title = "请选择合同名称";
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            dayLogListEnts.get(postion).projectNo = infoEnts.get(options1).projectNo;
            dayLogListEnts.get(postion).projectName = infoEnts.get(options1).projectName;
            dayLogListEnts.get(postion).contractNo = infoEnts.get(options1).contractNo;
            dayLogListEnts.get(postion).contractName = infoEnts.get(options1).contractName;
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
        pvOptions.setPicker(proList, null, null);
        pvOptions.show();
    }

    //------------------------------------------------------------------------------------------------


}
