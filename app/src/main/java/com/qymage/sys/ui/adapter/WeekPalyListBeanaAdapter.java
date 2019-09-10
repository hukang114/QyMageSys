package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.DateUtil;
import com.qymage.sys.ui.entity.YesQueryEny;

import java.util.List;

/**
 * 类名：
 * 类描述：周计划 周总结 适配器
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1014:00
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class WeekPalyListBeanaAdapter extends BaseQuickAdapter<YesQueryEny.WeekPalyListBean, BaseViewHolder> {


    public WeekPalyListBeanaAdapter(int layoutResId, @Nullable List<YesQueryEny.WeekPalyListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, YesQueryEny.WeekPalyListBean item) {
        helper.setText(R.id.crate_time_tv, item.createTime);
        helper.setText(R.id.weekday_tv, item.nextWeek);
    }


}
