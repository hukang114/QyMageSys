package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivityChoiceContractTypeBinding;
import com.qymage.sys.ui.adapter.ChoiceContractTypeAdapter;
import com.qymage.sys.ui.adapter.ChoiceContractTypeChiAdapter;
import com.qymage.sys.ui.entity.ContractTypeEnt;
import com.qymage.sys.ui.entity.CoustContractTypeEnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 合同类型选择
 */
public class ChoiceContractTypeActivity extends BBActivity<ActivityChoiceContractTypeBinding> implements View.OnClickListener {


    List<CoustContractTypeEnt> listht = new ArrayList<>();
    ChoiceContractTypeAdapter typeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choice_contract_type;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        typeAdapter = new ChoiceContractTypeAdapter(R.layout.item_list_choicecontracttype, listht);
        mBinding.recyclerview.setAdapter(typeAdapter);
        mBinding.saveBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        getContractOutType(1);


    }

    private void getContractOutType(int type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (type == 1) {
            showLoading();
            hashMap.put("type", "ContractOutType");// 支出类
        } else if (type == 2) {
            hashMap.put("type", "ContractComeType");// 收入类
        }
        HttpUtil.getEnum(hashMap).execute(new JsonCallback<Result<List<ContractTypeEnt>>>() {
            @Override
            public void onSuccess(Result<List<ContractTypeEnt>> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    if (type == 1) {
                        CoustContractTypeEnt typeEnt = new CoustContractTypeEnt("支出类", result.data);
                        listht.add(typeEnt);
                        getContractOutType(2);
                    } else if (type == 2) {
                        CoustContractTypeEnt typeEnt = new CoustContractTypeEnt("收入类", result.data);
                        listht.add(typeEnt);
                    }
                    typeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                showToast(e.getMessage());
            }
        });

    }


    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn:
                if (numBer() == 0) {
                    showToast("您还没有选择类型");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("value", getValue());
                    bundle.putString("title", getTilte());
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(AppConfig.num1000, intent);
                    finish();
                }
                break;
        }
    }

    class ChoiceContractTypeAdapter extends BaseQuickAdapter<CoustContractTypeEnt, BaseViewHolder> {

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
            chiAdapter.setOnItemChildClickListener((adapter, view, position) -> {

                for (int i = 0; i < listht.size(); i++) {
                    if (i == helper.getAdapterPosition()) {
                        for (int k = 0; k < listht.get(i).typeEnts.size(); k++) {
                            if (k == position) {
                                if (listht.get(i).typeEnts.get(position).isCheck) {
                                    listht.get(i).typeEnts.get(position).isCheck = true;
                                } else {
                                    listht.get(i).typeEnts.get(position).isCheck = true;
                                }
                            } else {
                                listht.get(i).typeEnts.get(k).isCheck = false;
                            }
                        }
                    } else {
                        for (int k = 0; k < listht.get(i).typeEnts.size(); k++) {
                            listht.get(i).typeEnts.get(k).isCheck = false;
                        }
                    }
                }
                typeAdapter.notifyDataSetChanged();
            });
        }
    }


    private int numBer() {
        int num = 0;
        for (int i = 0; i < listht.size(); i++) {
            for (int k = 0; k < listht.get(i).typeEnts.size(); k++) {
                if (listht.get(i).typeEnts.get(k).isCheck) {
                    num++;
                }
            }
        }
        return num;
    }

    private String getValue() {
        String value = null;
        for (int i = 0; i < listht.size(); i++) {
            for (int k = 0; k < listht.get(i).typeEnts.size(); k++) {
                if (listht.get(i).typeEnts.get(k).isCheck) {
                    value = listht.get(i).typeEnts.get(k).value;
                }
            }
        }
        return value;
    }

    private String getTilte() {
        String tilte = null;
        for (int i = 0; i < listht.size(); i++) {
            for (int k = 0; k < listht.get(i).typeEnts.size(); k++) {
                if (listht.get(i).typeEnts.get(k).isCheck) {
                    tilte = listht.get(i).typeEnts.get(k).label;
                }
            }
        }
        return tilte;
    }


}
