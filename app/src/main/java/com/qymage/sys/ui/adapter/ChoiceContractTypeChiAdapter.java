package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.ui.entity.ContractTypeEnt;

import java.util.List;

/**
 * 类名：
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1110:06
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ChoiceContractTypeChiAdapter extends BaseQuickAdapter<ContractTypeEnt, BaseViewHolder> {


    public ChoiceContractTypeChiAdapter(int layoutResId, @Nullable List<ContractTypeEnt> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContractTypeEnt item) {
        helper.setChecked(R.id.frg_selc_all, item.isCheck);
        helper.setText(R.id.name_tv_bg, item.label);
        helper.addOnClickListener(R.id.frg_selc_all);
    }


}
