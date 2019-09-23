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
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivityBiddingMarginZhiBinding;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.adapter.FileListAdapter;
import com.qymage.sys.ui.entity.CompanyMoneyPaymentVOS;
import com.qymage.sys.ui.entity.CompanyMoneyTicketVOS;
import com.qymage.sys.ui.entity.FileListEnt;
import com.qymage.sys.ui.entity.GetCompanyInfoEnt;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.PaymentInfo;
import com.qymage.sys.ui.entity.ProjecInfoEnt;
import com.qymage.sys.ui.entity.ReceiverInfo;
import com.qymage.sys.ui.entity.TouBS_LvSProjecEnt;

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
    FileListAdapter fileListAdapter;// 文件适配器
    private Bundle bundle;
    List<ProjecInfoEnt> infoEnts = new ArrayList<>();// 项目编号信息包含名称合同名称
    List<String> proList = new ArrayList<>();// 项目编号 项目名称
    List<GetCompanyInfoEnt> companyInfoEnts = new ArrayList<>();//单位信息
    List<TouBS_LvSProjecEnt> sProjecEnts = new ArrayList<>();// 3.2投标/履约保证金-收查询对应支数据的接口 ，根据项目编号 和名称获得


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
        mBinding.metitle.setrTxtClick(new View.OnClickListener() {
            @SingleClick(2000)
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putString("type", "0" + type);
                openActivity(BidPerformanceLvYueSZActivity.class, bundle);
            }
        });
        mBinding.startDateTv.setOnClickListener(this);
        mBinding.endDateTv.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);
        mBinding.sprImg.setOnClickListener(this);
        mBinding.csrImg.setOnClickListener(this);
        mBinding.gsmcEdt.setOnClickListener(this);
        mBinding.fujianImg.setOnClickListener(this);
        LinearLayoutManager layouta = new LinearLayoutManager(this);
        layouta.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        LinearLayoutManager layoutb = new LinearLayoutManager(this);
        layoutb.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        LinearLayoutManager layoutc = new LinearLayoutManager(this);
        layoutc.setOrientation(LinearLayoutManager.VERTICAL);//设置为横向排列
        mBinding.sprRecyclerview.setLayoutManager(layouta);
        mBinding.csrRecyclerview.setLayoutManager(layoutb);
        mBinding.fujianRecyclerview.setLayoutManager(new LinearLayoutManager(this));
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
        // 文件适配器
        fileListAdapter = new FileListAdapter(R.layout.item_file_list, fileList);
        mBinding.fujianRecyclerview.setAdapter(fileListAdapter);
        fileListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.file_del_img:
                    fileList.remove(position);
                    fileListAdapter.notifyDataSetChanged();
                    break;
            }
        });
        // 搜索项目编号
        mBinding.baoxiaoMoneyEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProjectNo(1, mBinding.baoxiaoMoneyEdt.getText().toString());
                return true;
            }
            return false;
        });
        // 搜索项目名称
        mBinding.shenqingMoneyEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getProjectNo(2, mBinding.shenqingMoneyEdt.getText().toString());
                return true;
            }
            return false;
        });
        // 搜索收款方名称
        mBinding.skdwmcEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getCompanyInfo(mBinding.skdwmcEdt.getText().toString());
                return true;
            }
            return false;
        });

    }


    @Override
    protected void initData() {
        super.initData();
        switch (type) {
            case "1":// 投标保证金支
                mBinding.metitle.setcTxt(this.getResources().getString(R.string.toubiaozhi_txt));
                mBinding.bzjmcLayout.setVisibility(View.GONE);
                //添加投标保证金支默认审批人
                getAuditQuery(MainActivity.processDefId(AppConfig.btnType10));
                mBinding.startTimeLayout.setVisibility(View.GONE);
                mBinding.fileLayout.setVisibility(View.GONE);
                break;
            case "2"://投标保证金收
                mBinding.metitle.setcTxt("投标保证金收");
                mBinding.skdwmcTv.setText("付款单位名称");
                mBinding.skdwmcEdt.setHint("请输入付款单位名称");
                mBinding.startTimeLayout.setVisibility(View.GONE);
                mBinding.fileLayout.setVisibility(View.GONE);
                //添加投标保证金收默认审批人
                getAuditQuery(MainActivity.processDefId(AppConfig.btnType9));
                break;
            case "3":// 履约保证金支
                mBinding.metitle.setcTxt(this.getResources().getString(R.string.lybzjz_txt));
                mBinding.bzjmcLayout.setVisibility(View.GONE);
                //添加履约保证金支默认审批人
                getAuditQuery(MainActivity.processDefId(AppConfig.btnType11));
                break;
            case "4"://履约保证金收
                mBinding.metitle.setcTxt(this.getResources().getString(R.string.lybzjs_txt));
                mBinding.skdwmcTv.setText("付款单位名称");
                mBinding.skdwmcEdt.setHint("请输入付款单位名称");
                //添加履约保证金收默认审批人
                getAuditQuery(MainActivity.processDefId(AppConfig.btnType12));
                break;
        }
    }

    @Override
    protected void getAuditQuerySuccess(List<GetTreeEnt> listdata) {
        auditorList.addAll(listdata);
        auditorListAdapter.notifyDataSetChanged();
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
                openFileChoose();
                break;
            case R.id.gsmc_edt:
                // 选择公司
                findAllCompany(mBinding.gsmcEdt.getText().toString());
                break;
            case R.id.save_btn:
                if (isCheck()) {
                    subMitData();
                }
                break;

        }
    }

    /**
     * 拿到上传成功的文件数据
     *
     * @param fileListEnts
     * @return
     */
    @Override
    protected void updateFileSuccess(List<FileListEnt> fileListEnts) {
        fileList.addAll(fileListEnts);
        fileListAdapter.notifyDataSetChanged();
    }

    /**
     * 搜索公司名称
     *
     * @param toString
     */
    private void findAllCompany(String toString) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("companyName", toString);
        showLoading();
        HttpUtil.bid_findAllCompany(hashMap).execute(new JsonCallback<Result<List<ProjecInfoEnt>>>() {
            @Override
            public void onSuccess(Result<List<ProjecInfoEnt>> result, Call call, Response response) {
                closeLoading();
                infoEnts.clear();
                infoEnts.addAll(result.data);
                proList.clear();
                for (int i = 0; i < infoEnts.size(); i++) {
                    proList.add(infoEnts.get(i).company_name);
                }
                setProDialog(3, proList);
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
     * 搜索项目编号或者项目名称
     */
    private void getProjectNo(int cate, String contnet) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (cate == 1) {
            hashMap.put("projectNo", contnet);//项目编号
        }
        if (cate == 2) {
            hashMap.put("projectName", contnet);//项目名称
        }
        if (type.equals("2") || type.equals("4")) {
            hashMap.put("bidType", "0" + type);
        }
        showLoading();
        HttpUtil.getProjectNo(hashMap).execute(new JsonCallback<Result<List<ProjecInfoEnt>>>() {
            @Override
            public void onSuccess(Result<List<ProjecInfoEnt>> result, Call call, Response response) {
                closeLoading();
                if (result.data != null && result.data.size() > 0) {
                    infoEnts.clear();
                    infoEnts.addAll(result.data);
                    if (cate == 1) {
                        proList.clear();
                        for (int i = 0; i < infoEnts.size(); i++) {
                            proList.add(infoEnts.get(i).projectNo);
                        }
                        setProDialog(cate, proList);
                    } else if (cate == 2) {
                        proList.clear();
                        for (int i = 0; i < infoEnts.size(); i++) {
                            proList.add(infoEnts.get(i).projectName);
                        }
                        setProDialog(cate, proList);
                    }
                } else {
                    showToast("尚未查询到项目编号或项目名称");
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
                closeLoading();
            }
        });

    }


    /**
     * 选择合同编号或者合同名称 公司名称
     *
     * @param cate
     * @param proList
     */
    private void setProDialog(int cate, List<String> proList) {
        String title = "请选择项目编号";
        if (cate == 1) {
            title = "请选择项目编号";
        } else if (cate == 2) {
            title = "请选择项目名称";
        } else if (cate == 3) {
            title = "请选择公司名称";
        } else if (cate == 4) {
            title = "请选择收款单位名称";
        } else if (cate == 5) {
            title = "请选择保证金名称";
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            if (cate == 1 || cate == 2) {
                mBinding.baoxiaoMoneyEdt.setText(infoEnts.get(options1).projectNo);
                mBinding.shenqingMoneyEdt.setText(infoEnts.get(options1).projectName);
                projectId = infoEnts.get(options1).id;
                // 自动加载对应的收的数据
                if (type.equals("2") || type.equals("4")) {
                    bid_findByProject(projectId);
                }
            } else if (cate == 3) {
                companyId = infoEnts.get(options1).id;
                mBinding.gsmcEdt.setText(infoEnts.get(options1).company_name);
            } else if (cate == 4) { // 选择收款单位信息
                mBinding.skdwmcEdt.setText(companyInfoEnts.get(options1).name);
                mBinding.yhzhEdt.setText(companyInfoEnts.get(options1).account);
                mBinding.khhEdt.setText(companyInfoEnts.get(options1).bank);
            } else if (cate == 5) { // 填写保证金相关信息
                mBinding.bzjmcEdt.setText(sProjecEnts.get(options1).amountName);
                mBinding.skdwmcEdt.setText(sProjecEnts.get(options1).name);
                mBinding.yhzhEdt.setText(sProjecEnts.get(options1).account);
                mBinding.khhEdt.setText(sProjecEnts.get(options1).bank);
                mBinding.bzjjeEdt.setText(sProjecEnts.get(options1).amount);
                mBinding.startDateTv.setText(sProjecEnts.get(options1).date);
                mBinding.endDateTv.setText(sProjecEnts.get(options1).endDate);
                mBinding.gsmcEdt.setText(sProjecEnts.get(options1).companyName);
                companyId = sProjecEnts.get(options1).companyId;
                mBinding.baoxiaoContent.setText(sProjecEnts.get(options1).remark);

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
     * 3.2投标/履约保证金-收查询对应支数据的接口
     */
    private void bid_findByProject(String projectId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("projectId", projectId);
        map.put("bidType", "0" + type);
        HttpUtil.bid_findByProject(map).execute(new JsonCallback<Result<List<TouBS_LvSProjecEnt>>>() {
            @Override
            public void onSuccess(Result<List<TouBS_LvSProjecEnt>> result, Call call, Response response) {
                closeLoading();
                if (result.data != null && result.data.size() > 0) {
                    sProjecEnts.clear();
                    sProjecEnts.addAll(result.data);
                    proList.clear();
                    for (int i = 0; i < sProjecEnts.size(); i++) {
                        proList.add(sProjecEnts.get(i).amountName);
                    }
                    setProDialog(5, proList);
                } else {
                    showToast("未查询到对应的保证金名称,请重新选择");
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                showToast("未查询到对应的保证金名称,请重新选择");
            }
        });
    }

    /**
     * 搜索单位名称（包含银行账户 开户行 信用代码等...）
     */
    private void getCompanyInfo(String companyName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("companyName", companyName);
        showLoading();
        HttpUtil.getCompanyInfo(hashMap).execute(new JsonCallback<Result<List<GetCompanyInfoEnt>>>() {
            @Override
            public void onSuccess(Result<List<GetCompanyInfoEnt>> result, Call call, Response response) {
                if (result.data != null && result.data.size() > 0) {
                    closeLoading();
                    companyInfoEnts.clear();
                    companyInfoEnts.addAll(result.data);
                    proList.clear();
                    for (int i = 0; i < companyInfoEnts.size(); i++) {
                        proList.add(companyInfoEnts.get(i).name);
                    }
                    setProDialog(4, proList);
                } else {
                    showToast("暂未搜索到相关内容");
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
     * 提交数据
     */
    private void subMitData() {
        showLoading();
        HttpUtil.bid_submit(getPer()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgDialogBuilder("申请提交成功,立即查看!", (dialog, which) -> {
                    bundle = new Bundle();
                    bundle.putString("type", "0" + type);
                    openActivity(BidPerformanceLvYueSZActivity.class, bundle);
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
        hashMap.put("projectId", projectId);
        hashMap.put("companyId", companyId);
        hashMap.put("bidType", "0" + type);
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
        } else if (TextUtils.isEmpty(mBinding.endDateTv.getText().toString())) {
            showToast("请选择结束日期");
            return false;
        } else if (auditorList.size() == 0) {
            showToast("请选择审批人");
            return false;
        } else {
            return true;
        }
    }


}

