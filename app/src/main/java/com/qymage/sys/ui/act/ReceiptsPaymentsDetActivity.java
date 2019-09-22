package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityReceiptsPaymentsDetBinding;
import com.qymage.sys.ui.entity.CompanyMoneyPaymentVOS;
import com.qymage.sys.ui.entity.ContractDetAddEnt;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.leo.click.SingleClick;


/**
 * 已收款 已付款明细
 */
public class ReceiptsPaymentsDetActivity extends BBActivity<ActivityReceiptsPaymentsDetBinding> implements View.OnClickListener {

    List<String> shuilv_list = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.00");
    private Bundle bundle;
    private String type;
    List<CompanyMoneyPaymentVOS> paymentVOS;
    CommonAdapter<CompanyMoneyPaymentVOS> adapter;
    private Intent mInstant;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_receipts_payments_det;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.newAddImg.setOnClickListener(this);
        mBinding.delImg.setOnClickListener(this);
        mInstant = getIntent();
        paymentVOS = (List<CompanyMoneyPaymentVOS>) mInstant.getSerializableExtra("data");
        type = mInstant.getStringExtra("type");
        if (paymentVOS == null) {
            return;
        }
        if (type.equals("1")) {
            mBinding.metitle.setcTxt("本次收款");
        } else if (type.equals("2")) {
            mBinding.metitle.setcTxt("本次付款");
        }
        setAdapter();
        for (int i = 1; i <= 100; i++) {
            shuilv_list.add(i + "%");
        }
        mBinding.metitle.setrTxtClick(v -> {
            bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) paymentVOS);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(200, intent);
            finish();
        });

    }

    private void setAdapter() {
        adapter = new CommonAdapter<CompanyMoneyPaymentVOS>(this, R.layout.item_add_list_sfk, paymentVOS) {
            @Override
            protected void convert(ViewHolder holder, CompanyMoneyPaymentVOS item, int position) {

                EditText amount_edt = holder.getView(R.id.amount_edt);
                amount_edt.setText(item.amount);

                EditText remarks_edt = holder.getView(R.id.remarks_edt);
                remarks_edt.setText(item.remarks);
                if (item.date != null && item.date.length() > 11) {
                    holder.setText(R.id.date_tv, item.date.substring(0, 10));
                } else {
                    holder.setText(R.id.date_tv, item.date);
                }
                holder.setOnClickListener(R.id.date_tv, v -> {
                    setOnClick(position);
                });

                holder.setIsRecyclable(false);
                amount_edt.setSelection(amount_edt.length());//将光标移至文字末尾

                amount_edt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s != null && !"".equals(s.toString()) && !s.toString().substring(0, 1).equals(".") && !s.toString().equals("0.")) {
                            paymentVOS.get(position).amount = s.toString();

                        }
                    }
                });
                remarks_edt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s != null && !"".equals(s.toString())) {
                            paymentVOS.get(position).remarks = s.toString();
                        }
                    }
                });


            }
        };
        mBinding.recyclerview.setAdapter(adapter);
    }


    private void setOnClick(int position) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String desc = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
            paymentVOS.get(position).date = desc;
            adapter.notifyDataSetChanged();
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }


    @Override
    protected void initData() {
        super.initData();

    }


    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_add_img:// 新增
                paymentVOS.add(new CompanyMoneyPaymentVOS("", "", ""));
                adapter.notifyDataSetChanged();
                break;
            case R.id.del_img:// 删除
                if (paymentVOS.size() > 0) {
                    paymentVOS.remove(paymentVOS.size() - 1);
                    adapter.notifyDataSetChanged();
                } else {
                    showToast("请先添加一条数据");
                }
                break;
        }
    }
}
