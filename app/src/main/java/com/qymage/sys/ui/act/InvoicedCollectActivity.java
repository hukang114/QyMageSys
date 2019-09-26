package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.qymage.sys.common.http.LogUtils;
import com.qymage.sys.databinding.ActivityInvoicedCollectBinding;
import com.qymage.sys.ui.entity.CompanyMoneyPaymentVOS;
import com.qymage.sys.ui.entity.CompanyMoneyTicketVOS;
import com.qymage.sys.ui.entity.ContractPayEnt;
import com.qymage.sys.ui.entity.LeaveType;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.leo.click.SingleClick;


/**
 * 已开票 已收票明细
 */
public class InvoicedCollectActivity extends BBActivity<ActivityInvoicedCollectBinding> implements View.OnClickListener {


    List<String> shuilv_list = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.00");
    private Bundle bundle;
    private String type;
    List<CompanyMoneyTicketVOS> ticketVOS;
    CommonAdapter<CompanyMoneyTicketVOS> adapter;
    private Intent mInstant;
    List<LeaveType> leaveTypes = new ArrayList<>();
    private String type_det;


    @Override

    protected int getLayoutId() {
        return R.layout.activity_invoiced_collect;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.newAddImg.setOnClickListener(this);
        mBinding.delImg.setOnClickListener(this);
        mInstant = getIntent();
        ticketVOS = (List<CompanyMoneyTicketVOS>) mInstant.getSerializableExtra("data");
        type = mInstant.getStringExtra("type");
        if (ticketVOS == null) {
            return;
        }
        try {
            type_det = mInstant.getStringExtra("type_det");
            if (type_det != null) {
                mBinding.metitle.setrTxt("");
                mBinding.bottomBntLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
        if (type.equals("3")) {
            mBinding.metitle.setcTxt("本次已开票");
        } else if (type.equals("4")) {
            mBinding.metitle.setcTxt("本次已收票");
        }
        setAdapter();

        leaveTypes.add(new LeaveType("3%普", "1"));
        leaveTypes.add(new LeaveType("3%专", "2"));
        leaveTypes.add(new LeaveType("6%普", "3"));
        leaveTypes.add(new LeaveType("6%专", "4"));
        leaveTypes.add(new LeaveType("9%普", "5"));
        leaveTypes.add(new LeaveType("9%专", "6"));
        leaveTypes.add(new LeaveType("11%普", "7"));
        leaveTypes.add(new LeaveType("11%专", "8"));
        leaveTypes.add(new LeaveType("13%普", "9"));
        leaveTypes.add(new LeaveType("13%专", "10"));

        for (int i = 0; i < leaveTypes.size(); i++) {
            shuilv_list.add(leaveTypes.get(i).label);
        }

        mBinding.metitle.setrTxtClick(v -> {
            bundle = new Bundle();
            bundle.putSerializable("data", (Serializable) ticketVOS);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(700, intent);
            finish();
        });


    }

    private void setAdapter() {
        adapter = new CommonAdapter<CompanyMoneyTicketVOS>(this, R.layout.itme_list_invoicedcollect, ticketVOS) {
            @Override
            protected void convert(ViewHolder holder, CompanyMoneyTicketVOS item, int position) {

                if (item.paymentTime != null && item.paymentTime.length() > 11) {
                    holder.setText(R.id.date_tv, item.paymentTime.substring(0, 10));
                } else {
                    holder.setText(R.id.date_tv, item.paymentTime);
                }
                holder.setText(R.id.shuijin_tv, df.format(item.taxes));

                EditText jine_edt = holder.getView(R.id.jine_edt);
                jine_edt.setText(item.amount);
                holder.setText(R.id.shui_lv_tv, item.rateName);

                holder.setIsRecyclable(false);
                jine_edt.setSelection(jine_edt.length());//将光标移至文字末尾

                if (type_det == null) { // 显示详情页数据的时候不需要操作
                    holder.setOnClickListener(R.id.date_tv, v -> {
                        selectClickDate(position);
                    });
                    holder.setOnClickListener(R.id.shui_lv_tv, v -> {
                        if (!ticketVOS.get(position).amount.equals("")) {
                            setOnClick(position);
                        } else {
                            showToast("请先填写金额");
                        }

                    });
                }
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
                            ticketVOS.get(position).amount = s.toString();
                            if (ticketVOS.get(position).taxRate != 0) {
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
     * 选择日期
     *
     * @param position
     */
    private void selectClickDate(int position) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String desc = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
            ticketVOS.get(position).paymentTime = desc;
            adapter.notifyDataSetChanged();
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


    /**
     * 税率选择
     *
     * @param position
     */
    private void setOnClick(int position) {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                ticketVOS.get(position).taxRate = Integer.parseInt(leaveTypes.get(options1).value);
                ticketVOS.get(position).rateName = leaveTypes.get(options1).label;
                if (ticketVOS.get(position).rateName.equals("3%普")) {
                    ticketVOS.get(position).taxes = 0.00;
                } else {
                    ticketVOS.get(position).taxes = number(Integer.parseInt(ticketVOS.get(position).rateName.substring(0, ticketVOS.get(position).rateName.length() - 2)), Double.parseDouble(ticketVOS.get(position).amount));
                }
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


    /**
     * 输入金额计算税金
     *
     * @param position
     */
    private void setCalculation(int position) {
        ticketVOS.get(position).taxes = number(Integer.parseInt(ticketVOS.get(position).rateName.substring(0, ticketVOS.get(position).rateName.length() - 2)), Double.parseDouble(ticketVOS.get(position).amount));

        if (mBinding.recyclerview.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (mBinding.recyclerview.isComputingLayout() == false)) {
        }
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                adapter.notifyItemChanged(position);
//            }
//        }, 1000);    //延时1s执行
        adapter.notifyDataSetChanged();

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
                ticketVOS.add(new CompanyMoneyTicketVOS("", 0, 0, "", ""));
                adapter.notifyDataSetChanged();
                break;
            case R.id.del_img:// 删除
                if (ticketVOS.size() > 0) {
                    ticketVOS.remove(ticketVOS.size() - 1);
                    adapter.notifyDataSetChanged();
                } else {
                    showToast("请先添加一条数据");
                }
                break;
        }
    }
}
