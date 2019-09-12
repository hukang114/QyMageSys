package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.ui.entity.NewsEntity;

import java.util.List;

/**
 * Created by admin on 2019/8/24.
 * 消息通知列表
 */

public class NewsAdapter extends BaseQuickAdapter<NewsEntity, BaseViewHolder> {


    public NewsAdapter(int layoutResId, @Nullable List<NewsEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsEntity item) {
        LinearLayout search_layout = helper.getView(R.id.search_layout);
        if (item.msgType == 15) {
            helper.setImageResource(R.id.msg_cate_img, R.mipmap.msg_cate2);
        } else {
            helper.setImageResource(R.id.msg_cate_img, R.mipmap.msg_cate1);
        }
        TextView msg_cate_num = helper.getView(R.id.msg_cate_num);

        String status = VerifyUtils.isEmpty(item.msgType) ? "" : 1 == item.msgType ? "[立项类]" : 2 == item.msgType ? "[日报类]" :
                3 == item.msgType ? "[投标保证金支]" : 4 == item.msgType ? "[投标保证金收]" : 5 == item.msgType ? "[履约保证金支]" :
                        6 == item.msgType ? "[履约保证金收]" : 7 == item.msgType ? "[请假类]" :
                                8 == item.msgType ? "[合同类]" :
                                        9 == item.msgType ? "[付款类]" :
                                                10 == item.msgType ? "[收款类]" :
                                                        11 == item.msgType ? "[开票类]" :
                                                                12 == item.msgType ? "[收票类]" :
                                                                        13 == item.msgType ? "[借款类]" :
                                                                                14 == item.msgType ? "[月报类]" :
                                                                                        15 == item.msgType ? "" : "";

        if (item.total <= 0) {
            msg_cate_num.setVisibility(View.GONE);
        } else {
            msg_cate_num.setVisibility(View.VISIBLE);
        }
        helper.setText(R.id.msg_cate_title, item.msgTitle)
                .setText(R.id.date_time_tv, item.createTime)
                .setText(R.id.msg_cate_content, status + item.msgContent)
                .setText(R.id.msg_cate_num, item.total + "");
        if (helper.getAdapterPosition() == 0) {
            search_layout.setVisibility(View.GONE);
        } else {
            search_layout.setVisibility(View.GONE);
        }

    }


}
