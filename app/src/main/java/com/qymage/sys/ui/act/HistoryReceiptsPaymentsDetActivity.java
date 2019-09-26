package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityHistoryReceiptsPaymentsDetBinding;
import com.qymage.sys.databinding.ActivityReceiptsPaymentsDetBinding;
import com.qymage.sys.ui.entity.CompanyMoneyPaymentVOS;
import com.qymage.sys.ui.entity.GetReceivedInfoEnt;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.leo.click.SingleClick;


/**
 * 历史已收款 历史已付款明细
 */
public class HistoryReceiptsPaymentsDetActivity extends BBActivity<ActivityHistoryReceiptsPaymentsDetBinding> {

    DecimalFormat df = new DecimalFormat("0.00");
    private Bundle bundle;
    private String type;

    public List<GetReceivedInfoEnt.CompanyMoneyPaymentVOBean> paymentVOS;
    CommonAdapter<GetReceivedInfoEnt.CompanyMoneyPaymentVOBean> adapter;

    private Intent mInstant;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history_receipts_payments_det;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mInstant = getIntent();
        paymentVOS = (List<GetReceivedInfoEnt.CompanyMoneyPaymentVOBean>) mInstant.getSerializableExtra("data");
        type = mInstant.getStringExtra("type");
        if (paymentVOS == null) {
            return;
        }
        if (type.equals("1")) {
            mBinding.metitle.setcTxt(this.getResources().getString(R.string.ysk_txt));
        } else if (type.equals("2")) {
            mBinding.metitle.setcTxt(this.getResources().getString(R.string.yfk_txt));
        }
        setAdapter();

    }

    private void setAdapter() {
        adapter = new CommonAdapter<GetReceivedInfoEnt.CompanyMoneyPaymentVOBean>(this, R.layout.item_add_list_sfk, paymentVOS) {
            @Override
            protected void convert(ViewHolder holder, GetReceivedInfoEnt.CompanyMoneyPaymentVOBean item, int position) {

                EditText amount_edt = holder.getView(R.id.amount_edt);
                amount_edt.setText(item.amount);
                EditText remarks_edt = holder.getView(R.id.remarks_edt);
                remarks_edt.setText(item.remarks);
                String paymentTime;
                if (item.paymentTime != null && item.paymentTime.length() > 11) {
                    paymentTime = item.paymentTime.substring(0, 10);
                } else {
                    paymentTime = item.paymentTime;
                }
                holder.setText(R.id.date_tv, paymentTime);
                mBinding.recyclerview.setAdapter(adapter);
            }

        };
        mBinding.recyclerview.setAdapter(adapter);
    }


}
