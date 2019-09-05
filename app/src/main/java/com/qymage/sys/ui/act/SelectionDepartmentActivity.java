package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.databinding.ActivitySelectionDepartmentBinding;
import com.qymage.sys.ui.adapter.SelectionDeparAdapter;
import com.qymage.sys.ui.entity.GetTreeEnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 选择审批人 抄送人
 */
public class SelectionDepartmentActivity extends BBActivity<ActivitySelectionDepartmentBinding> {


    private Intent mIntent;
    private String type;
    List<GetTreeEnt> listdata = new ArrayList<>();
    SelectionDeparAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_selection_department;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        type = mIntent.getStringExtra("type");
        if (type == null) {
            return;
        }
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectionDeparAdapter(R.layout.item_list_selectiondepar, listdata);
        mBinding.recyclerview.setAdapter(adapter);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.frg_selc_all:// 单选
                    if (listdata.get(position).isCheck) {
                        listdata.get(position).isCheck = false;
                    } else {
                        listdata.get(position).isCheck = true;
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        showLoading();
        HttpUtil.getTree(HttpConsts.GETTREE, new HashMap<>()).execute(new JsonCallback<Result<List<GetTreeEnt>>>() {
            @Override
            public void onSuccess(Result<List<GetTreeEnt>> result, Call call, Response response) {
                closeLoading();
                if (result.data != null) {
                    listdata.clear();
                    listdata.addAll(result.data);
                    adapter.notifyDataSetChanged();
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
}
