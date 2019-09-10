package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.PicasooUtil;
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
        } else if (mType == 2) {
            helper.setText(R.id.jrgzjh_tv, "上周工作计划");
            helper.setText(R.id.mrgzjh_tv, "本周工作总结");
            helper.setText(R.id.mrgzjh_tv, "下周工作计划");
            helper.setText(R.id.date_time_tv, item.date)
                    .setText(R.id.user_name, item.userName)
                    .setText(R.id.today_plan_tv, item.lastWeek)
                    .setText(R.id.today_content_tv, item.weekDay)
                    .setText(R.id.tomorrow_plan_tv, item.nextWeek);
        } else if (mType == 3) {

        }
        if (item.userName != null && item.userName.length() >= 3) {
            String strh = item.userName.substring(item.userName.length() - 2, item.userName.length());   //截取
            helper.setText(R.id.name_tv_bg, strh);
        } else {
            helper.setText(R.id.name_tv_bg, item.userName);
        }

    }


}
