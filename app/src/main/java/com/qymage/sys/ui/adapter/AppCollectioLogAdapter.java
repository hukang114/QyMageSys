package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.DateUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.ui.act.ApplicationCollectionLogActivity;
import com.qymage.sys.ui.act.BidPerformanceLvYueSZActivity;
import com.qymage.sys.ui.entity.ApplicationCollectionLogList;
import com.qymage.sys.ui.entity.BidLvYueSZListEnt;

import java.util.List;

/**
 * Created by admin on 2019/8/29.
 * 收款申请记录，付款申请记录 ，开票申请记录，收票申请记录 提交记录 适配器
 */

public class AppCollectioLogAdapter extends BaseQuickAdapter<ApplicationCollectionLogList, BaseViewHolder> {


    public AppCollectioLogAdapter(int layoutResId, @Nullable List<ApplicationCollectionLogList> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ApplicationCollectionLogList item) {

        TextView bnt1 = helper.getView(R.id.bnt1);
        TextView bnt2 = helper.getView(R.id.bnt2);
        TextView bnt3 = helper.getView(R.id.bnt3);
        ImageView head_img = helper.getView(R.id.head_img);

        String status = VerifyUtils.isEmpty(item.stats) ? "" : 1 == item.stats ? "待处理" : 2 == item.stats ? "已处理" :
                3 == item.stats ? "抄送给我" : 4 == item.stats ? "已处理" : "";

        helper.setText(R.id.status_tv, item.actStatus)
                .setText(R.id.user_name, item.userName + "提交" + "申请")
                .setText(R.id.proj_type, "项目类型：" + item.contractName)
                .setText(R.id.proj_name, "项目名称：" + item.projectName)
                .setText(R.id.appaly_content_tv, "金额：" + item.thisAmount)
                .setText(R.id.date_time_tv, "申请日期：" + DateUtil.formatMillsTo(item.createTime));
//        PicasooUtil.setImageResource(item.portrait, helper.getView(R.id.head_img), 360);

        TextView name_tv_bg = helper.getView(R.id.name_tv_bg);

        if (item.userName != null) {
            head_img.setVisibility(View.GONE);
            name_tv_bg.setVisibility(View.VISIBLE);
            if (item.userName.length() >= 3) {
                String strh = item.userName.substring(item.userName.length() - 2, item.userName.length());   //截取
                name_tv_bg.setText(strh);
            } else {
                name_tv_bg.setText(item.userName);
            }
        }


        helper.addOnClickListener(R.id.bnt1).addOnClickListener(R.id.bnt2).addOnClickListener(R.id.bnt3);
        if (ApplicationCollectionLogActivity.mType == 1) {
            bnt1.setVisibility(View.GONE);
            bnt2.setVisibility(View.VISIBLE);
            bnt3.setVisibility(View.VISIBLE);
            if (item.read == 0) {
                helper.setVisible(R.id.unread_msg_img, true);
            } else {
                helper.setVisible(R.id.unread_msg_img, false);
            }
        } else if (ApplicationCollectionLogActivity.mType == 4) {
            bnt2.setVisibility(View.GONE);
            bnt3.setVisibility(View.GONE);
            helper.setVisible(R.id.unread_msg_img, false);
            if (item.canCancelTask == 1) {
                bnt1.setVisibility(View.VISIBLE);
            } else {
                bnt1.setVisibility(View.GONE);
            }

        } else {
            bnt1.setVisibility(View.GONE);
            bnt2.setVisibility(View.GONE);
            bnt3.setVisibility(View.GONE);
            helper.setVisible(R.id.unread_msg_img, false);
        }
//
//        switch (item.actStatus) {
//            case 1:
//                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.colorAccent));
//                bnt1.setVisibility(View.GONE);
//                break;
//            case 2:
//                bnt1.setVisibility(View.GONE);
//                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.orange_e86));
//                break;
//
//            case 3:
//                bnt1.setVisibility(View.VISIBLE);
//                helper.setTextColor(R.id.status_tv, mContext.getResources().getColor(R.color.orange_e86));
//                bnt1.setText("同意");
//                break;
//
//        }
    }


}
