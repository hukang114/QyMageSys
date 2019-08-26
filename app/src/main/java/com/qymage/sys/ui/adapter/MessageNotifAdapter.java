package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.DisplayUtil;
import com.qymage.sys.common.util.PicasooUtil;
import com.qymage.sys.ui.entity.MessageNotifEnt;

import java.util.List;

/**
 * Created by admin on 2019/8/25.
 */

public class MessageNotifAdapter extends BaseQuickAdapter<MessageNotifEnt, BaseViewHolder> {


    public MessageNotifAdapter(int layoutResId, @Nullable List<MessageNotifEnt> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageNotifEnt item) {

        ImageView ivGoodsPicture = helper.getView(R.id.msg_img);
        // 设置商品缩略图宽高比
        ViewGroup.LayoutParams imageParams = ivGoodsPicture.getLayoutParams();
//        imageParams.width = (DisplayUtil.getScreenWidth(mContext) - DisplayUtil.dip2px(80)) / 2;
        imageParams.height = (DisplayUtil.getScreenWidth(mContext)) / 2;
        ivGoodsPicture.setLayoutParams(imageParams);

        PicasooUtil.setImageResource(item.msg_pic, helper.getView(R.id.msg_img));
        helper.setText(R.id.msg_title, item.msg_title)
                .setText(R.id.date_time_tv,item.date_time);

    }
}
