package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.qymage.sys.databinding.ActivityHistoryInvoicedCollectBinding;
import com.qymage.sys.databinding.ActivityInvoicedCollectBinding;
import com.qymage.sys.ui.entity.CompanyMoneyTicketVOS;
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
 * 历史已开票 历史已收票明细
 */
public class HistoryInvoicedCollectActivity extends BBActivity<ActivityHistoryInvoicedCollectBinding> {


    DecimalFormat df = new DecimalFormat("0.00");
    private Bundle bundle;
    private String type;
    List<GetReceivedInfoEnt.CompanyMoneyTicketVOBean> ticketVOS;
    CommonAdapter<GetReceivedInfoEnt.CompanyMoneyTicketVOBean> adapter;

    private Intent mInstant;


    @Override

    protected int getLayoutId() {
        return R.layout.activity_history_invoiced_collect;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mInstant = getIntent();
        ticketVOS = (List<GetReceivedInfoEnt.CompanyMoneyTicketVOBean>) mInstant.getSerializableExtra("data");
        type = mInstant.getStringExtra("type");
        if (ticketVOS == null) {
            return;
        }
        if (type.equals("3")) {
            mBinding.metitle.setcTxt(this.getResources().getString(R.string.ykp_txt));
        } else if (type.equals("4")) {
            mBinding.metitle.setcTxt(this.getResources().getString(R.string.ysp_txt));
        }
        setAdapter();

    }

    private void setAdapter() {
        adapter = new CommonAdapter<GetReceivedInfoEnt.CompanyMoneyTicketVOBean>(this, R.layout.itme_list_invoicedcollect, ticketVOS) {
            @Override
            protected void convert(ViewHolder holder, GetReceivedInfoEnt.CompanyMoneyTicketVOBean item, int position) {

                holder.setText(R.id.date_tv, item.paymentTime);
                holder.setText(R.id.shuijin_tv, df.format(item.taxes));

                EditText jine_edt = holder.getView(R.id.jine_edt);
                jine_edt.setText(item.amount);
                holder.setText(R.id.shui_lv_tv, item.taxeRate + "%");
                holder.setIsRecyclable(false);
                jine_edt.setSelection(jine_edt.length());//将光标移至文字末尾
                jine_edt.setSelection(jine_edt.length());//将光标移至文字末尾

            }
        };
        mBinding.recyclerview.setAdapter(adapter);

    }


    @Override
    protected void initData() {
        super.initData();

    }

}
