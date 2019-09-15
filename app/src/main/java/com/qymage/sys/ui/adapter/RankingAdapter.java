package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.ui.entity.RankingKaoQing;

import java.util.List;

/**
 * Created by admin on 2019/9/16.
 * 排行
 */

public class RankingAdapter extends BaseQuickAdapter<RankingKaoQing.RankListEntity, BaseViewHolder> {


    public RankingAdapter(int layoutResId, @Nullable List<RankingKaoQing.RankListEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RankingKaoQing.RankListEntity item) {
        helper.setText(R.id.tv1, item.ranking).setText(R.id.tv2, item.userName)
                .setText(R.id.tv3, item.hours).setText(R.id.tv4, item.cindex);

    }


}
