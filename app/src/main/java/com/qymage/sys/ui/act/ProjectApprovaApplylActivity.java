package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.DateUtil;
import com.qymage.sys.databinding.ActivityProjectApprovaApplylBinding;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.entity.CompanyMoneyPaymentVOS;
import com.qymage.sys.ui.entity.CompanyMoneyTicketVOS;
import com.qymage.sys.ui.entity.ContractTypeEnt;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.PaymentInfo;
import com.qymage.sys.ui.entity.ProjecInfoEnt;
import com.qymage.sys.ui.entity.ProjectApprovaLoglDetEnt;
import com.qymage.sys.ui.entity.ReceiverInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 立项申请
 */

public class ProjectApprovaApplylActivity extends BBActivity<ActivityProjectApprovaApplylBinding> implements View.OnClickListener {


    List<GetTreeEnt> auditorList = new ArrayList<>();// 审核人
    List<GetTreeEnt> copierList = new ArrayList<>();// 抄送人
    AuditorListAdapter auditorListAdapter;// 审批人适配器
    CopierListAdapter copierListAdapter;// 抄送人适配器
    private Bundle bundle;
    private List<String> protypelist = new ArrayList<>();
    List<ContractTypeEnt> typeEnts = new ArrayList<>();
    private String projectType;
    ProjectApprovaLoglDetEnt info;// 通过立项详情中的新建传递过来的信息
    private Intent mIntent;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_approva_applyl;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.metitle.setrTxtClick(new View.OnClickListener() {
            @SingleClick(2000)
            @Override
            public void onClick(View v) {
                openActivity(ProjectApprovaLoglActivity.class);
            }
        });
        mIntent = getIntent();
        try {
            info = (ProjectApprovaLoglDetEnt) mIntent.getSerializableExtra("data");
        } catch (Exception e) {

        }
        mBinding.shiyongDateTv.setOnClickListener(this);
        LinearLayoutManager layouta = new LinearLayoutManager(this);
        layouta.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        LinearLayoutManager layoutb = new LinearLayoutManager(this);
        layoutb.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        mBinding.sprImg.setOnClickListener(this);
        mBinding.csrImg.setOnClickListener(this);
        mBinding.baoxiaoCateTxt.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);
        mBinding.sprRecyclerview.setLayoutManager(layouta);
        mBinding.csrRecyclerview.setLayoutManager(layoutb);
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
        getProType();
        //添加立项默认审批人
        getAuditQuery(MainActivity.processDefId(AppConfig.btnType3));
        // 设置新建对应的数据
        if (info != null) {
            projectType = info.projectType;
            mBinding.baoxiaoCateTxt.setText(info.projectTypeName);
            mBinding.baoxiaoMoneyEdt.setText(info.projectNo);
            mBinding.shenqingMoneyEdt.setText(info.projectName);
            mBinding.fuzerenEdt.setText(info.persion);
            mBinding.yusuanjineEdt.setText(info.amount);
            String date;
            if (info.date != null && info.date.length() > 11) {
                date = info.date.substring(0, 10);
            } else {
                date = DateUtil.formatNYR(System.currentTimeMillis());
            }
            mBinding.shiyongDateTv.setText(date);
            mBinding.baoxiaoContent.setText(info.introduction);
        }
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
     * 获取项目类型
     */
    private void getProType() {
        showLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "ProjectType");
        HttpUtil.getEnum(hashMap).execute(new JsonCallback<Result<List<ContractTypeEnt>>>() {
            @Override
            public void onSuccess(Result<List<ContractTypeEnt>> result, Call call, Response response) {
                closeLoading();
                if (result.data != null && result.data.size() > 0) {
                    typeEnts.clear();
                    typeEnts.addAll(result.data);
                    for (int i = 0; i < typeEnts.size(); i++) {
                        protypelist.add(typeEnts.get(i).label);
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
            case R.id.shiyong_date_tv:
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
            case R.id.save_btn:
                if (isCheck()) {
                    subMitData();
                }
                break;
            case R.id.baoxiao_cate_txt:// 选择项目类型
                if (protypelist.size() > 0) {
                    setOnClick();
                } else {
                    showToast("暂无项目类型可以选择");
                }

                break;
        }
    }


    /**
     * 项目类型
     *
     * @param
     */
    private void setOnClick() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            mBinding.baoxiaoCateTxt.setText(protypelist.get(options1));
            projectType = typeEnts.get(options1).value;
            getProjectNo(typeEnts.get(options1).value);

        })
                .setTitleText("请选择项目类型")
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

    /**
     * 根据项目类型获取项目编号
     *
     * @param value
     */
    private void getProjectNo(String value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("projectType", value);
        showLoading();
        HttpUtil.project_getProjectNo(map).execute(new JsonCallback<Result<ProjecInfoEnt>>() {
            @Override
            public void onSuccess(Result<ProjecInfoEnt> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    mBinding.baoxiaoMoneyEdt.setText(result.data.projectNo);
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
     * 日期选择
     */
    private void showDateDialog() {
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


    private void subMitData() {
        showLoading();
        HttpUtil.project_Submit(HttpConsts.PROJECT_SUBMIT, getPer()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgNocanseDialogBuilder("申请提交成功", (dialog, which) -> {
                    dialog.dismiss();
                    openActivity(ProjectApprovaLoglActivity.class);
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

        if (TextUtils.isEmpty(mBinding.baoxiaoCateTxt.getText().toString())) {
            showToast("请选择项目类型");
            return false;
        } else if (TextUtils.isEmpty(mBinding.baoxiaoMoneyEdt.getText().toString())) {
            showToast("请填写项目编号");
            return false;
        } else if (TextUtils.isEmpty(mBinding.shenqingMoneyEdt.getText().toString())) {
            showToast("请填写项目名称");
            return false;
        } else if (TextUtils.isEmpty(mBinding.fuzerenEdt.getText().toString())) {
            showToast("请填写项目负责人");
            return false;
        } else if (TextUtils.isEmpty(mBinding.yusuanjineEdt.getText().toString())) {
            showToast("请填写项目预算金额");
            return false;
        } else if (TextUtils.isEmpty(mBinding.shiyongDateTv.getText().toString())) {
            showToast("请填写日期");
            return false;
        } else if (TextUtils.isEmpty(mBinding.baoxiaoContent.getText().toString())) {
            showToast("请填写项目说明");
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


    /**
     * 项目立项参数
     *
     * @return
     */
    private HashMap<String, Object> getPer() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("projectType", projectType);
        hashMap.put("projectNo", mBinding.baoxiaoMoneyEdt.getText().toString());
        hashMap.put("projectName", mBinding.shenqingMoneyEdt.getText().toString());
        hashMap.put("persion", mBinding.fuzerenEdt.getText().toString());
        hashMap.put("amount", mBinding.yusuanjineEdt.getText().toString());
        hashMap.put("date", mBinding.shiyongDateTv.getText().toString());
        hashMap.put("introduction", mBinding.baoxiaoContent.getText().toString());
        ////审核人
        hashMap.put("auditor", auditorList);
        //抄送人
        hashMap.put("copier", copierList);

        return hashMap;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) { // //收付款明细
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
        } else if (resultCode == 500) { // 获取收款方信息
        } else if (resultCode == 600) {// 获取付款方信息
        } else if (resultCode == 700) {//开收票明细

        }

    }


}

