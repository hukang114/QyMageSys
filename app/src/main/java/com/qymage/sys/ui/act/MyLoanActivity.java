package com.qymage.sys.ui.act;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.KeyBordUtil;
import com.qymage.sys.databinding.ActivityMyLoanBinding;
import com.qymage.sys.ui.adapter.MyLoanListAdapter;
import com.qymage.sys.ui.entity.MyLoanEnt;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 我的借款记录
 */
public class MyLoanActivity extends BBActivity<ActivityMyLoanBinding> implements RadioGroup.OnCheckedChangeListener {


    private int page = 1;
    private int mType = 1;
    List<MyLoanEnt> listdata = new ArrayList<>();
    MyLoanListAdapter adapter;
    private String stats = "1";// 1-待处理 2-已处理 3-抄送我  4-已提交
    private String keyword = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_loan;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.refreshlayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            getListData(Constants.RequestMode.FRIST);

        });
        mBinding.refreshlayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            getListData(Constants.RequestMode.LOAD_MORE);
        });
        mBinding.refreshlayout.setEnableLoadMore(false);
        adapter = new MyLoanListAdapter(R.layout.item_myloan_list, listdata);
        mBinding.recyclerview.setAdapter(adapter);
        mBinding.radioGroup.setOnCheckedChangeListener(this);
        mBinding.pendingBtn.setChecked(true);
        mBinding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
             /*   if (!getKeyWord().equals("")) {
                } else {
                    showToast("请输入搜索关键字");
                }*/
                KeyBordUtil.hideSoftKeyboard(mBinding.etSearch);
                keyword = getKeyWord();
                page = 1;
                getListData(Constants.RequestMode.FRIST);
                return true;
            }
            return false;
        });
        adapter.setOnItemChildClickListener((adapter, view, position) -> {

            switch (view.getId()) {
                case R.id.bnt1: // 申请还款
                    openActivity(ApplicationRepaymentActivity.class);
                    break;
                case R.id.bnt2:// 拒绝
                    msgDialogBuilder("拒绝审批？", (dialog, which) -> {
                        auditAdd("2", listdata.get(position).TaskId);
                    }).create().show();
                    break;
                case R.id.bnt3:// 同意
                    msgDialogBuilder("同意审批？", (dialog, which) -> {
                        auditAdd("1", listdata.get(position).TaskId);
                    }).create().show();

                    break;
            }
        });

    }

    /**
     * 审批操作
     * type：类型  1—通过   2-拒绝 3-撤回
     *
     * @param type
     */
    private void auditAdd(String type, String taskId) {
        showLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userCode", getUserId());
        hashMap.put("remarks", "");
        hashMap.put("type", type);
        hashMap.put("taskId", taskId);
        HttpUtil.audit_auditAdd(HttpConsts.audit_auditAdd, hashMap).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgDialogBuilder(result.message, (dialog, which) -> {
                    dialog.dismiss();
                    setResult(200);
                    finish();
                }).setCancelable(false).create().show();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                showToast(e.getMessage());
            }
        });


    }

    private String getKeyWord() {
        return mBinding.etSearch.getText().toString().trim();
    }


    public String getmTypes() {
        return stats;
    }


    /**
     * 获取数据
     *
     * @param mode
     */
    private void getListData(Constants.RequestMode mode) {
        showLoading();
        HttpUtil.loan_loanQuery(getPer()).execute(new JsonCallback<Result<List<MyLoanEnt>>>() {
            @Override
            public void onSuccess(Result<List<MyLoanEnt>> result, Call call, Response response) {
                mBinding.emptylayout.showContent();
                mBinding.refreshlayout.finishRefresh(); // 刷新完成
                mBinding.refreshlayout.finishLoadMore();
                closeLoading();
                if (mode == Constants.RequestMode.FRIST) {
                    listdata.clear();
                    if (result.data != null && result.data.size() > 0) {
                        listdata.addAll(result.data);
                    } else {
                        mBinding.emptylayout.showEmpty();
                    }
                } else if (mode == Constants.RequestMode.LOAD_MORE) {
                    if (result.data != null && result.data.size() > 0) {
                        List<MyLoanEnt> list = result.data;
                        listdata.addAll(list);
                    } else {
                        // 全部加载完成,没有数据了调用此方法
                        mBinding.refreshlayout.finishLoadMoreWithNoMoreData();
                        page--;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                mBinding.refreshlayout.finishRefresh(); // 刷新完成
                mBinding.refreshlayout.finishLoadMore();
                showToast(e.getMessage());
                mBinding.emptylayout.showError();
                mBinding.emptylayout.setRetryListener(() -> {
                    page = 1;
                    getListData(Constants.RequestMode.FRIST);
                });
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
        page = 1;
        getListData(Constants.RequestMode.FRIST);
    }


    /**
     * 每一种状态的点击事件处理
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.pending_btn:// 待处理
                mType = 1;
                break;
            case R.id.processed_btn://已处理
                mType = 2;
                break;
            case R.id.copy_to_me_btn://抄送给我的
                mType = 3;
                break;
            case R.id.yitijioa_btn:// 已提交的
                mType = 4;
                break;
        }
        page = 1;
        getListData(Constants.RequestMode.FRIST);
    }


    /**
     * 参数
     *
     * @return
     */
    private HashMap<String, Object> getPer() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("stats", stats);
        map.put("keyword", keyword);
        map.put("userCode", getUserId());
        map.put("page", page + "");
        return map;
    }


}



