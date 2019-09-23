package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivityContractPaymentRaBinding;
import com.qymage.sys.ui.entity.ContractDetAddEnt;
import com.qymage.sys.ui.entity.ContractPayEnt;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 添加合同的付款比例
 */

public class ContractPaymentRaActivity extends BBActivity<ActivityContractPaymentRaBinding> implements View.OnClickListener {


    List<ContractPayEnt> listdata;
    private Intent mInstant;
    CommonAdapter<ContractPayEnt> adapter;
    List<String> shuilv_list = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.00");
    private Bundle bundle;
    private String type_det;
    private double htjeMoney = 0; // 合同金额


    @Override
    protected int getLayoutId() {
        return R.layout.activity_contract_payment_ra;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.newAddImg.setOnClickListener(this);
        mBinding.delImg.setOnClickListener(this);
        mInstant = getIntent();
        listdata = (List<ContractPayEnt>) mInstant.getSerializableExtra("data");
        try {
            type_det = mInstant.getStringExtra("type_det");
            htjeMoney = Double.parseDouble(mInstant.getStringExtra("htjeMoney"));
            if (type_det != null) {
                mBinding.metitle.setcTxt("");
                mBinding.bottonLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
        if (listdata == null) {
            return;
        }
        setAdapter();

        for (int i = 1; i <= 100; i++) {
            shuilv_list.add(i + "%");
        }
        mBinding.metitle.setrTxtClick(v -> {
            bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) listdata);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(700, intent);
            finish();
        });

    }


    @Override
    protected void initData() {
        super.initData();
//        getEnum();

    }

    /**
     * 获取税率
     */
    private void getEnum() {
        showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "taxrateType");
        HttpUtil.getEnum(map).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_add_img:// 新增
                listdata.add(new ContractPayEnt("", 0, ""));
                adapter.notifyDataSetChanged();
                break;
            case R.id.del_img:// 删除
                if (listdata.size() > 0) {
                    listdata.remove(listdata.size() - 1);
                    adapter.notifyDataSetChanged();
                } else {
                    showToast("请先添加一条数据");
                }
                break;
        }
    }

    private void setAdapter() {
        adapter = new CommonAdapter<ContractPayEnt>(this, R.layout.item_add_fkbl_list_contractdet, listdata) {
            @Override
            protected void convert(ViewHolder holder, ContractPayEnt item, int position) {
                holder.setText(R.id.num_tv, (position + 1) + "");
                EditText jine_edt = holder.getView(R.id.shuijin_tv);
                jine_edt.setText(item.amount);
                holder.setText(R.id.jine_edt, item.date);
                if (item.payScale == 0) {
                    holder.setText(R.id.shui_lv_tv, "");
                } else {
                    holder.setText(R.id.shui_lv_tv, item.payScale + "%");
                }
                holder.setIsRecyclable(false);
                jine_edt.setSelection(jine_edt.length());//将光标移至文字末尾

                holder.setOnClickListener(R.id.jine_edt, v -> {
                    selectClickDate(position);
                });
                holder.setOnClickListener(R.id.shui_lv_tv, v -> {
                    setOnClick(position);
                });
                jine_edt.setSelection(jine_edt.length());//将光标移至文字末尾

                jine_edt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s != null && !"".equals(s.toString()) && !s.toString().substring(0, 1).equals(".") && !s.toString().equals("0.")) {
                            listdata.get(position).amount = s.toString();
                        }
                    }
                });

            }
        };
        mBinding.recyclerview.setAdapter(adapter);
    }


    /**
     * 税率的选择
     *
     * @param position
     */
    private void setOnClick(int position) {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                listdata.get(position).payScale = Integer.parseInt(shuilv_list.get(options1).replace("%", ""));
                float pays = Integer.parseInt(shuilv_list.get(options1).replace("%", ""));
                listdata.get(position).amount = df.format(htjeMoney * (pays / 100));
                adapter.notifyDataSetChanged();
            }
        })
                .setTitleText("请选择付款比例")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(18)
                .setOutSideCancelable(false)//点击外部dismiss default true
                .setContentTextSize(16)//滚轮文字大小
                .setSubCalSize(16)//确定和取消文字大小
                .setLineSpacingMultiplier(2.4f)
                .build();
        // 三级选择器
        pvOptions.setPicker(shuilv_list, null, null);
        pvOptions.show();
    }


    /**
     * 选择日期
     *
     * @param position
     */
    private void selectClickDate(int position) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String desc = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
            listdata.get(position).date = desc;
            adapter.notifyDataSetChanged();
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


}
