package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.PicasooUtil;
import com.qymage.sys.ui.entity.JournalEntity;

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
        PicasooUtil.setImageResource(item.headimg, helper.getView(R.id.head_img), 360);
        helper.setText(R.id.date_time_tv, item.datetime)
                .setText(R.id.user_name, item.username)
                .setText(R.id.today_plan_tv, item.today_plan)
                .setText(R.id.today_content_tv, item.today_content)
                .setText(R.id.tomorrow_plan_tv, item.tomorrow_plan);

    }


}
