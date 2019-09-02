package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.ui.entity.ReimburEntCoust;

import java.util.List;

/**
 * Created by admin on 2019/8/29.
 * 报销明细的
 */

public class ReimbntAdapter extends BaseQuickAdapter<ReimburEntCoust, BaseViewHolder> {

    List<ReimburEntCoust> data;


    public ReimbntAdapter(int layoutResId, @Nullable List<ReimburEntCoust> data) {
        super(layoutResId, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReimburEntCoust item) {

        TextView delete_btn = helper.getView(R.id.delete_btn);

        if (data.size() > 1) {
            delete_btn.setVisibility(View.VISIBLE);
        } else {
            delete_btn.setVisibility(View.GONE);
        }
        helper.setText(R.id.baoxiao_money_edt, item.money)
                .setText(R.id.baoxiao_cate_txt, item.cate_title)
                .setText(R.id.baoxiao_content, item.content)
                .setText(R.id.baoxiao_id_tv, "报销明细(" + (helper.getAdapterPosition() + 1) + ")");
        helper.addOnClickListener(R.id.delete_btn).addOnClickListener(R.id.baoxiao_cate_txt);

        EditText baoxiao_money_edt = helper.getView(R.id.baoxiao_money_edt);
        EditText baoxiao_content = helper.getView(R.id.baoxiao_content);

        baoxiao_money_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null && !s.equals("")) {
                    item.money = s.toString();
                } else {
                    item.money = "";
                }
            }
        });

        baoxiao_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null && !s.equals("")) {
                    item.content = s.toString();
                } else {
                    item.content = "";
                }
            }
        });

        RecyclerView recyc_pic = helper.getView(R.id.recyc_pic);



    }


}
