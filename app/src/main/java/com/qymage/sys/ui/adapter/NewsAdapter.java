package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
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
        helper.setImageResource(R.id.msg_cate_img, item.ioc);
        helper.setText(R.id.msg_cate_title, item.title)
                .setText(R.id.date_time_tv, item.date)
                .setText(R.id.msg_cate_content, item.content)
                .setText(R.id.msg_cate_num, item.num + "");
        if (helper.getAdapterPosition() == 0) {
            search_layout.setVisibility(View.VISIBLE);
        } else {
            search_layout.setVisibility(View.GONE);
        }

    }


}
