package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.PicasooUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;

import java.util.List;

/**
 * Created by admin on 2019/8/29.
 * 立项记录
 */

public class ProjectApprovaLogAdapter extends BaseQuickAdapter<ProjectAppLogEnt, BaseViewHolder> {


    public ProjectApprovaLogAdapter(int layoutResId, @Nullable List<ProjectAppLogEnt> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectAppLogEnt item) {

        TextView haikuan_btn = helper.getView(R.id.bnt1);


        String status = VerifyUtils.isEmpty(item.status) ? "" : 1 == item.status ? "等待审批" : 2 == item.status ? "正在审批中" : "";
        helper.setText(R.id.status_tv, status)
                .setText(R.id.user_name, item.username)
                .setText(R.id.proj_type, "项目类型：" + item.shenqing_ly)
                .setText(R.id.proj_name, "项目名称：" + item.shiyong_date)
                .setText(R.id.appaly_content_tv, "预算申请：" + item.shenqin_money)
                .setText(R.id.date_time_tv, "申请日期：" + item.date_time);

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
