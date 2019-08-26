package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.PicasooUtil;
import com.qymage.sys.ui.entity.WorkNoticeEnt;

import java.util.List;

/**
 * Created by admin on 2019/8/25.
 * 工作通知
 */

public class WorkNoticeListAdapter extends BaseQuickAdapter<WorkNoticeEnt, BaseViewHolder> {


    public WorkNoticeListAdapter(int layoutResId, @Nullable List<WorkNoticeEnt> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkNoticeEnt item) {
        PicasooUtil.setImageResource(item.user_pic, helper.getView(R.id.head_img), 360);
        helper.setText(R.id.user_name, item.username)
                .setText(R.id.proj_name, item.proj_name)
                .setText(R.id.proj_type, item.type_name)
                .setText(R.id.appaly_content_tv, item.app_money)
                .setText(R.id.date_time_tv, item.date_time);

    }


}
