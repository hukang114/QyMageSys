package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.ui.entity.ContractTypeEnt;
import com.qymage.sys.ui.entity.CoustContractTypeEnt;

import java.util.List;

/**
 * 类名：
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1110:00
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ChoiceContractTypeAdapter extends BaseQuickAdapter<CoustContractTypeEnt, BaseViewHolder> {

    ChoiceContractTypeChiAdapter chiAdapter;

    public ChoiceContractTypeAdapter(int layoutResId, @Nullable List<CoustContractTypeEnt> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoustContractTypeEnt item) {
        helper.setText(R.id.title_tv, item.title);
        RecyclerView chirecyclerview = helper.getView(R.id.chirecyclerview);
        chirecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        chiAdapter = new ChoiceContractTypeChiAdapter(R.layout.item_list_choicecontracttypechi, item.typeEnts);
        chirecyclerview.setAdapter(chiAdapter);
    }


}
