package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.ui.entity.ReimbursementLogEnt;

import java.util.List;

/**
 * 类名：
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1711:14
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ReimbursementLogAdapter extends BaseQuickAdapter<ReimbursementLogEnt, BaseViewHolder> {


    public ReimbursementLogAdapter(int layoutResId, @Nullable List<ReimbursementLogEnt> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReimbursementLogEnt item) {
        if (item.reimburserName != null) {
            if (item.reimburserName.length() >= 3) {
                String strh = item.reimburserName.substring(item.reimburserName.length() - 2, item.reimburserName.length());   //截取
                helper.setText(R.id.name_tv_bg, strh);
            } else {
                helper.setText(R.id.name_tv_bg, item.reimburserName);
            }
        }
        helper.setText(R.id.status_tv, item.actStatus)
                .setText(R.id.user_name, item.reimburserName + "报销申请")
                .setText(R.id.crate_time, item.date)
                .setText(R.id.proj_type, "部门：" + item.departmentName)
                .setText(R.id.proj_name, "项目编号：" + item.projectNo)
                .setText(R.id.appaly_content_tv, "项目名称：" + item.projectName)
                .setText(R.id.date_time_tv, "报销金额：" + item.amount);


    }


}
