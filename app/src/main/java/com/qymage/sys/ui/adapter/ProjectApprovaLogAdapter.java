package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
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
        ImageView head_img = helper.getView(R.id.head_img);

        String status = VerifyUtils.isEmpty(item.actStatus) ? "" : 1 == item.actStatus ? "待处理" : 2 == item.actStatus ? "已处理" :
                3 == item.actStatus ? "抄送给我" : 4 == item.actStatus ? "已处理" : "";

        helper.setText(R.id.status_tv, status)
                .setText(R.id.user_name, item.persion + item.projectType)
                .setText(R.id.proj_type, "项目类型：" + item.projectType)
                .setText(R.id.proj_name, "项目名称：" + item.projectName)
                .setText(R.id.appaly_content_tv, "预算申请：" + item.amount)
                .setText(R.id.date_time_tv, "申请日期：" + item.date);
        PicasooUtil.setImageResource(item.portrait, helper.getView(R.id.head_img), 360);

        TextView name_tv_bg = helper.getView(R.id.name_tv_bg);

        if (item.persion == null) {
            head_img.setVisibility(View.GONE);
            name_tv_bg.setVisibility(View.VISIBLE);
            if (item.persion.length() >= 3) {
                String strh = item.persion.substring(item.persion.length() - 2, item.persion.length());   //截取
                name_tv_bg.setText(strh);
            } else {
                name_tv_bg.setText(item.persion);
            }
        }

        switch (item.actStatus) {
            case 1:
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.colorAccent));
                haikuan_btn.setVisibility(View.GONE);
                break;
            case 2:
                haikuan_btn.setVisibility(View.GONE);
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.orange_e86));
                break;

            case 3:
                haikuan_btn.setVisibility(View.VISIBLE);
                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.orange_e86));
                haikuan_btn.setText("同意");
                break;

        }
    }


}
