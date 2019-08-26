package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.ui.entity.MyMenuEnt;

import java.util.List;

/**
 * Created by admin on 2019/8/25.
 */

public class MyMenuAdapter extends BaseQuickAdapter<MyMenuEnt, BaseViewHolder> {


    public MyMenuAdapter(int layoutResId, @Nullable List<MyMenuEnt> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyMenuEnt item) {

        helper.setText(R.id.menu_title, item.menu_name);
        helper.setImageResource(R.id.menu_img, item.menu_ioc);

    }

}
