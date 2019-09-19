package com.qymage.sys.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.http.LogUtils;
import com.qymage.sys.databinding.ActivityContractDetailsAddBinding;
import com.qymage.sys.ui.entity.ContractDetAddEnt;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 合同明细 ，添加合同明细
 */
public class ContractDetailsAddActivity extends BBActivity<ActivityContractDetailsAddBinding> implements View.OnClickListener {


    List<ContractDetAddEnt> listdata;
    private Intent mInstant;
    CommonAdapter<ContractDetAddEnt> adapter;
    List<String> shuilv_list = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.00");
    private Bundle bundle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_contract_details_add;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.newAddImg.setOnClickListener(this);
        mBinding.delImg.setOnClickListener(this);
        mInstant = getIntent();
        listdata = (List<ContractDetAddEnt>) mInstant.getSerializableExtra("data");
        if (listdata == null) {
            return;
        }
        setAdapter();

//        for (int i = 1; i <= 100; i++) {
//            shuilv_list.add(i + "%");
//        }
        shuilv_list.add(3 + "%");
        shuilv_list.add(6 + "%");
        shuilv_list.add(9 + "%");
        shuilv_list.add(11 + "%");
        shuilv_list.add(13 + "%");

        mBinding.metitle.setrTxtClick(v -> {
            bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) listdata);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(200, intent);
            finish();
        });

    }

    /**
     * 税率计算
     *
     * @param num
     * @return
     */
    private double number(float num, double amount) {
        double taxrate = 0.00;

        double b = (num / 100);
        LogUtils.e("b=" + b);

        if (num == 3) {
            taxrate = amount / 1.03 * b;
        } else if (num == 6) {
            taxrate = amount / 1.06 * b;
        } else if (num == 9) {
            taxrate = amount / 1.09 * b;
        } else if (num == 11) {
            taxrate = amount / 1.11 * b;
        } else if (num == 13) {
            taxrate = amount / 1.13 * b;
        }
        return taxrate;

    }


    @Override
    protected void initData() {
        super.initData();
        getTaxrateType();

    }

    /**
     * 获取合同的税率
     */
    private void getTaxrateType() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "taxrateType");
        HttpUtil.getEnum(map).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });

    }


    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_add_img:// 新增
                listdata.add(new ContractDetAddEnt("", 0, ""));
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
        adapter = new CommonAdapter<ContractDetAddEnt>(this, R.layout.item_add_list_contractdet, listdata) {
            @Override
            protected void convert(ViewHolder holder, ContractDetAddEnt item, int position) {

                EditText jine_edt = holder.getView(R.id.jine_edt);
                jine_edt.setText(item.amount);

                holder.setText(R.id.shuijin_tv, item.taxes);

                if (item.taxRate == 0) {
                    holder.setText(R.id.shui_lv_tv, "");
                } else {
                    holder.setText(R.id.shui_lv_tv, item.taxRate + "%");
                }
                holder.setOnClickListener(R.id.shui_lv_tv, v -> {
                    if (!listdata.get(position).amount.equals("")) {
                        setOnClick(position);
                    } else {
                        showToast("请先填写金额");
                    }

                });

                holder.setIsRecyclable(false);
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
                            if (listdata.get(position).taxRate != 0) {
                                setCalculation(position);
                                jine_edt.clearFocus();
                            }
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
                listdata.get(position).taxRate = Integer.parseInt(shuilv_list.get(options1).replace("%", ""));
                listdata.get(position).taxes = df.format(number(listdata.get(position).taxRate, Double.parseDouble(listdata.get(position).amount)));
                adapter.notifyDataSetChanged();
            }
        })
                .setTitleText("请选择利率")
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


    private void setCalculation(int position) {
        listdata.get(position).taxes = df.format(number(listdata.get(position).taxRate, Double.parseDouble(listdata.get(position).amount)));
        if (mBinding.recyclerview.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (mBinding.recyclerview.isComputingLayout() == false)) {
        }
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                adapter.notifyItemChanged(position);
//            }
//        }, 1000);    //延时1s执行
        adapter.notifyDataSetChanged();

    }


}
