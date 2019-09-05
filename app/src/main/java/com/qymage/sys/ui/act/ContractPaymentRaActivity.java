package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

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
        if (listdata == null) {
            return;
        }
        setAdapter();

        for (int i = 1; i <= 100; i++) {
            shuilv_list.add(i + "%");
        }
    }


    @Override
    protected void initData() {
        super.initData();
        getEnum();

    }

    /**
     * 获取税率
     */
    private void getEnum() {
        showLoading();
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "taxrateType");
        HttpUtil.getEnum(HttpConsts.GETENUM, map).execute(new JsonCallback<Result<String>>() {
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
                listdata.add(new ContractPayEnt(listdata.size() + 1, "", 0, ""));
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
                holder.setText(R.id.num_tv, item.id + "");
                EditText jine_edt = holder.getView(R.id.shuijin_tv);
                jine_edt.setText(item.money);
                holder.setText(R.id.jine_edt, item.date);
                if (item.bili == 0) {
                    holder.setText(R.id.shui_lv_tv, "");
                } else {
                    holder.setText(R.id.shui_lv_tv, item.bili + "%");
                }
                holder.setIsRecyclable(false);
                jine_edt.setSelection(jine_edt.length());//将光标移至文字末尾

                holder.setOnClickListener(R.id.jine_edt, v -> {
                    selectClickDate(position);
                });

            }
        };
        mBinding.recyclerview.setAdapter(adapter);
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
