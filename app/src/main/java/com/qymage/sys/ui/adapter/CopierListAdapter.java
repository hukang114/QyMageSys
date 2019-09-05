package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.PicasooUtil;
import com.qymage.sys.ui.entity.GetTreeEnt;

import java.util.List;

/**
 * Created by admin on 2019/9/5.
 * 已近选择的抄送让人适配器
 */

public class CopierListAdapter extends BaseQuickAdapter<GetTreeEnt, BaseViewHolder> {


    public CopierListAdapter(int layoutResId, @Nullable List<GetTreeEnt> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetTreeEnt item) {
        PicasooUtil.setImageResource(item.portrait, R.mipmap.def_logo_headimg, helper.getView(R.id.headimg), 360);
        helper.setText(R.id.user_name_tv, item.name);
        helper.addOnClickListener(R.id.caccel_ioc);

    }


}
