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
import com.qymage.sys.ui.act.MyLoanActivity;
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


        String mType = ((MyLoanActivity) mContext).getmTypes();

        TextView bnt1 = helper.getView(R.id.bnt1);
        TextView bnt2 = helper.getView(R.id.bnt2);
        TextView bnt3 = helper.getView(R.id.bnt3);
        TextView bnt4 = helper.getView(R.id.btn4);
        String typename = VerifyUtils.isEmpty(item.type) ? "借款" : item.type.equals("1") ? "借款" : item.type.equals("2") ? "还款" : "";

        helper.setText(R.id.status_tv, item.actStatus)
                .setText(R.id.user_name, item.personName + typename)
                .setText(R.id.crate_time, item.useDate)
                .setText(R.id.proj_type, "申请事由：" + item.cause)
                .setText(R.id.proj_name, "申请金额：" + item.amount)
                .setText(R.id.appaly_content_tv, "借款时间：" + item.createTime)
                .setText(R.id.date_time_tv, "所属部门：" + item.deptName);

        PicasooUtil.setImageResource(item.portrait, R.mipmap.def_logo_headimg, helper.getView(R.id.head_img), 360);
        if (item.personName != null) {
            if (item.personName.length() >= 3) {
                String strh = item.personName.substring(item.personName.length() - 2, item.personName.length());   //截取
                helper.setText(R.id.name_tv_bg, strh);
            } else {
                helper.setText(R.id.name_tv_bg, item.personName);
            }
        }

        switch (MyLoanActivity.mType) {
            case 1:
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.colorAccent));
                bnt1.setVisibility(View.GONE);
                if (item.returnStatus == 1) {
                    bnt4.setVisibility(View.VISIBLE);
                    bnt2.setVisibility(View.GONE);
                    bnt3.setVisibility(View.GONE);
                } else {
                    bnt2.setVisibility(View.VISIBLE);
                    bnt3.setVisibility(View.VISIBLE);
                    bnt4.setVisibility(View.GONE);
                }
                if (item.read == 0) {
                    helper.setVisible(R.id.unread_msg_img, true);
                } else {
                    helper.setVisible(R.id.unread_msg_img, false);
                }
                break;
            case 2:
                bnt1.setVisibility(View.GONE);
                bnt2.setVisibility(View.GONE);
                bnt3.setVisibility(View.GONE);
                bnt4.setVisibility(View.GONE);
                helper.setVisible(R.id.unread_msg_img, false);
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.orange_e86));
                break;
            case 3:
                bnt1.setVisibility(View.GONE);
                bnt2.setVisibility(View.GONE);
                bnt3.setVisibility(View.GONE);
                bnt4.setVisibility(View.GONE);
                helper.setVisible(R.id.unread_msg_img, false);
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.orange_e86));
                break;
            case 4:
                bnt2.setVisibility(View.GONE);
                bnt3.setVisibility(View.GONE);
                if (item.canCancelTask == 1) {
                    bnt1.setVisibility(View.VISIBLE);
                } else {
                    bnt1.setVisibility(View.GONE);
                }
                bnt4.setVisibility(View.GONE);
                helper.setVisible(R.id.unread_msg_img, false);
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.orange_e86));
                break;
            default:
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.colorAccent));
                break;
        }
        helper.addOnClickListener(R.id.bnt1).addOnClickListener(R.id.bnt2).addOnClickListener(R.id.bnt3).addOnClickListener(R.id.btn4);

    }

}
