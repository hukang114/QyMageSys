package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.PicasooUtil;
import com.qymage.sys.ui.entity.GetTreeEnt;

import java.util.List;

/**
 * 类名：
 * 类描述：选择审批人 抄送人
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/516:49
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class SelectionDeparAdapter extends BaseQuickAdapter<GetTreeEnt, BaseViewHolder> {


    public SelectionDeparAdapter(int layoutResId, @Nullable List<GetTreeEnt> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetTreeEnt item) {
        ImageView head_img = helper.getView(R.id.head_img);
        PicasooUtil.setImageResource(item.portrait, helper.getView(R.id.head_img), 360);
        helper.setText(R.id.user_userpost_tv, item.userPost)
                .setText(R.id.user_name_tv, item.userName);
        helper.addOnClickListener(R.id.frg_selc_all);

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
        } else {
            head_img.setVisibility(View.VISIBLE);
            name_tv_bg.setVisibility(View.GONE);
        }

        helper.setChecked(R.id.frg_selc_all, item.isCheck);


    }


}
