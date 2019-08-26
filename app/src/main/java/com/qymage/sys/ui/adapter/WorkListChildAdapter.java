package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.ui.entity.WorkListEnt;

import java.util.List;


/**
 * Created by admin on 2019/8/25.
 */

public class WorkListChildAdapter extends BaseQuickAdapter<WorkListEnt.DataBaen, BaseViewHolder> {


    public WorkListChildAdapter(int layoutResId, @Nullable List<WorkListEnt.DataBaen> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkListEnt.DataBaen item) {
        helper.setImageResource(R.id.cate_img, item.child_ioc);
        helper.setText(R.id.cate_title, item.child_name);
    }


}
