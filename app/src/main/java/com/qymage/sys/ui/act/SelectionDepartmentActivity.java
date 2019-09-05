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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 选择审批人 抄送人
 */
public class SelectionDepartmentActivity extends BBActivity<ActivitySelectionDepartmentBinding> implements View.OnClickListener {


    private Intent mIntent;
    private String type;
    List<GetTreeEnt> listdata = new ArrayList<>();
    SelectionDeparAdapter adapter;
    Bundle bundle;
    // 选中的数据
    List<GetTreeEnt> selectList = new ArrayList<>();


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
        mBinding.trueBtn.setOnClickListener(this);
        adapter = new SelectionDeparAdapter(R.layout.item_list_selectiondepar, listdata);
        mBinding.recyclerview.setAdapter(adapter);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.frg_selc_all:// 单选
                    for (int i = 0; i < listdata.size(); i++) {
                        if (i == position) {
                            if (listdata.get(position).isCheck) {
                                listdata.get(position).isCheck = false;
                            } else {
                                listdata.get(position).isCheck = true;
                            }
                        }
                    }
                    CalculNum();
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
                    mBinding.trueBtn.setText("确定(0/" + listdata.size() + ")");
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
            case R.id.true_btn:
                if (CalculNum() == 0) {
                    showToast("至少选择一个人");
                } else {
                    setSelectList();
                    bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) selectList);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    if (type.equals("1")) {
                        setResult(300, intent);
                        finish();
                    } else if (type.equals("2")) {
                        setResult(400, intent);
                        finish();
                    }

                }
                break;
        }

    }


    /**
     * 计算选中的人数
     *
     * @return
     */
    private int CalculNum() {
        int num = 0;
        for (int i = 0; i < listdata.size(); i++) {
            if (listdata.get(i).isCheck) {
                num = num + 1;
            }
        }
        mBinding.selectPerTv.setText("以选择：" + num + "人");
        mBinding.trueBtn.setText("确定（" + num + "/" + listdata.size() + ")");
        return num;
    }

    /**
     * 获取选中的数据
     */
    public void setSelectList() {
        selectList.clear();
        for (int i = 0; i < listdata.size(); i++) {
            if (listdata.get(i).isCheck) {
                selectList.add(listdata.get(i));
            }
        }
    }


}
