package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.PicasooUtil;
import com.qymage.sys.ui.act.ChoiceContractLogActivity;
import com.qymage.sys.ui.act.MyLoanActivity;
import com.qymage.sys.ui.entity.JournalEntity;
import com.qymage.sys.ui.fragment.JournalFragment;

import java.util.List;

/**
 * Created by admin on 2019/8/24.
 * 日报 周报，月报
 */

public class JournalAdapter extends BaseQuickAdapter<JournalEntity, BaseViewHolder> {


    public JournalAdapter(int layoutResId, @Nullable List<JournalEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, JournalEntity item) {
//        PicasooUtil.setImageResource(item.headimg, helper.getView(R.id.head_img), 360);
        int mType = JournalFragment.workType;
        LinearLayout bottom_bnt_layout = helper.getView(R.id.bottom_bnt_layout);

        TextView bnt1 = helper.getView(R.id.bnt1);
        TextView bnt2 = helper.getView(R.id.bnt2);
        TextView bnt3 = helper.getView(R.id.bnt3);

        if (mType == 1) {
            helper.setText(R.id.date_time_tv, item.createDate)
                    .setText(R.id.user_name, item.userName)
                    .setText(R.id.today_plan_tv, item.dayWork)
                    .setText(R.id.today_content_tv, item.wordPlay)
                    .setText(R.id.tomorrow_plan_tv, item.nextWork);
            if (item.userName != null && item.userName.length() >= 3) {
                String strh = item.userName.substring(item.userName.length() - 2, item.userName.length());   //截取
                helper.setText(R.id.name_tv_bg, strh);
            } else {
                helper.setText(R.id.name_tv_bg, item.userName);
            }
            bottom_bnt_layout.setVisibility(View.VISIBLE);

            if (item.read == 0) {
                helper.setVisible(R.id.unread_msg_img, true);
            } else {
                helper.setVisible(R.id.unread_msg_img, false);
            }


        } else if (mType == 2) {
            helper.setText(R.id.jrgzjh_tv, "上周工作计划");
            helper.setText(R.id.mrgzjh_tv, "本周工作总结");
            helper.setText(R.id.mrgzjh_tv, "下周工作计划");
            helper.setText(R.id.date_time_tv, item.date)
                    .setText(R.id.user_name, item.userName)
                    .setText(R.id.today_plan_tv, item.lastWeek)
                    .setText(R.id.today_content_tv, item.weekDay)
                    .setText(R.id.tomorrow_plan_tv, item.nextWeek);
            bottom_bnt_layout.setVisibility(View.GONE);
            helper.setVisible(R.id.unread_msg_img, false);
        } else if (mType == 3) {
            helper.setText(R.id.jrgzjh_tv, "本月工作计划");
            helper.setText(R.id.mrgzjh_tv, "本月工作总结");
            helper.setText(R.id.mrgzjh_tv, "本月工作总结");
            helper.setText(R.id.date_time_tv, item.createTime)
                    .setText(R.id.user_name, item.userName)
                    .setText(R.id.today_plan_tv, item.monthWork)
                    .setText(R.id.today_content_tv, item.submonthWork)
                    .setText(R.id.tomorrow_plan_tv, item.nextMonthWeek);
            bottom_bnt_layout.setVisibility(View.VISIBLE);
            if (item.read == 0) {
                helper.setVisible(R.id.unread_msg_img, true);
            } else {
                helper.setVisible(R.id.unread_msg_img, false);
            }
        }
        if (item.userName != null && item.userName.length() >= 3) {
            String strh = item.userName.substring(item.userName.length() - 2, item.userName.length());   //截取
            helper.setText(R.id.name_tv_bg, strh);
        } else {
            helper.setText(R.id.name_tv_bg, item.userName);
        }
        helper.addOnClickListener(R.id.bnt1).addOnClickListener(R.id.bnt2).addOnClickListener(R.id.bnt3);

        if (JournalFragment.logType == 1) {
            bnt1.setVisibility(View.GONE);
            bnt2.setVisibility(View.VISIBLE);
            bnt3.setVisibility(View.VISIBLE);
        } else if (JournalFragment.logType == 4) {
            bnt1.setVisibility(View.VISIBLE);
            bnt2.setVisibility(View.GONE);
            bnt3.setVisibility(View.GONE);
        } else {
            bnt1.setVisibility(View.GONE);
            bnt2.setVisibility(View.GONE);
            bnt3.setVisibility(View.GONE);
        }

    }


}
