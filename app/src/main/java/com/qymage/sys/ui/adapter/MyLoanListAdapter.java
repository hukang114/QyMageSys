package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.PicasooUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.ui.entity.MyLoanEnt;

import java.util.List;

/**
 * Created by admin on 2019/8/28.
 * 我的借款列表
 */

public class MyLoanListAdapter extends BaseQuickAdapter<MyLoanEnt, BaseViewHolder> {


    public MyLoanListAdapter(int layoutResId, @Nullable List<MyLoanEnt> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyLoanEnt item) {

        TextView haikuan_btn = helper.getView(R.id.bnt1);


        String status = VerifyUtils.isEmpty(item.status) ? "" : 1 == item.status ? "待还款" : 2 == item.status ? "正在审批中" : "";
        helper.setText(R.id.status_tv, status)
                .setText(R.id.user_name, item.username + "借款")
                .setText(R.id.crate_time, item.shiyong_date)
                .setText(R.id.proj_type, "申请事由：" + item.shenqing_ly)
                .setText(R.id.proj_name, "申请金额：" + item.shenqin_money)
                .setText(R.id.appaly_content_tv, "使用时间：" + item.shiyong_date)
                .setText(R.id.date_time_tv, "备注：" + item.remake_text);

        PicasooUtil.setImageResource(item.imgpath, helper.getView(R.id.head_img), 360);

        switch (item.status) {
            case 1:
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.colorAccent));
                haikuan_btn.setVisibility(View.VISIBLE);
                break;
            case 2:
                haikuan_btn.setVisibility(View.GONE);
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.orange_e86));
                break;
        }


    }

}
