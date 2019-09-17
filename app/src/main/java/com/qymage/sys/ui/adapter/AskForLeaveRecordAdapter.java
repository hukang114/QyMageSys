package com.qymage.sys.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.PicasooUtil;
import com.qymage.sys.ui.act.AskForLeaveRecordlActivity;
import com.qymage.sys.ui.act.MyLoanActivity;
import com.qymage.sys.ui.entity.AskForLeaveEntity;
import com.qymage.sys.ui.entity.JournalEntity;

import java.util.List;

/**
 * Created by admin on 2019/8/24.
 * 请假记录
 */

public class AskForLeaveRecordAdapter extends BaseQuickAdapter<AskForLeaveEntity, BaseViewHolder> {


    public AskForLeaveRecordAdapter(int layoutResId, @Nullable List<AskForLeaveEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AskForLeaveEntity item) {
//        PicasooUtil.setImageResource(item.headimg, helper.getView(R.id.afl_head_img), 360);
        TextView bnt1 = helper.getView(R.id.bnt1);
        TextView bnt2 = helper.getView(R.id.bnt2);
        TextView bnt3 = helper.getView(R.id.bnt3);
        if (item.name != null) {
            if (item.name.length() >= 3) {
                String strh = item.name.substring(item.name.length() - 2, item.name.length());   //截取
                helper.setText(R.id.name_tv_bg, strh);
            } else {
                helper.setText(R.id.name_tv_bg, item.name);
            }
        }
        helper.setText(R.id.status_tv, item.actStatus)
                .setText(R.id.user_name, item.name + "请假申请")
                .setText(R.id.crate_time, item.createDate)
                .setText(R.id.proj_type, "请假原因：" + item.cause)
                .setText(R.id.proj_name, "请假类型：" + item.leaveName)
                .setText(R.id.appaly_content_tv, "请假时间：" + item.startDate + "-" + item.endDate)
                .setText(R.id.date_time_tv, " 时长：" + item.ofTime);

        switch (AskForLeaveRecordlActivity.mType) {
            case 1:
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.colorAccent));
                bnt1.setVisibility(View.GONE);
                bnt2.setVisibility(View.VISIBLE);
                bnt3.setVisibility(View.VISIBLE);
                break;
            case 2:
                bnt1.setVisibility(View.GONE);
                bnt2.setVisibility(View.GONE);
                bnt3.setVisibility(View.GONE);
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.orange_e86));
                break;
            case 3:
                bnt1.setVisibility(View.GONE);
                bnt2.setVisibility(View.GONE);
                bnt3.setVisibility(View.GONE);
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.orange_e86));
                break;
            case 4:
                bnt1.setVisibility(View.VISIBLE);
                bnt2.setVisibility(View.GONE);
                bnt3.setVisibility(View.GONE);
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.orange_e86));
                break;
            default:
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.colorAccent));
                break;
        }
        helper.addOnClickListener(R.id.bnt1).addOnClickListener(R.id.bnt2).addOnClickListener(R.id.bnt3).addOnClickListener(R.id.btn4);

    }


}
