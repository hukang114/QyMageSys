package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.inputmethod.EditorInfo;
import android.widget.RadioGroup;

import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.KeyBordUtil;
import com.qymage.sys.databinding.ActivityBidPerformanceLvYueSzBinding;
import com.qymage.sys.ui.adapter.BidPerFormLogAdapter;
import com.qymage.sys.ui.adapter.ProjectApprovaLogAdapter;
import com.qymage.sys.ui.entity.BidLvYueSZListEnt;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 投标支收 履约收支记录
 */
public class BidPerformanceLvYueSZActivity extends BBActivity<ActivityBidPerformanceLvYueSzBinding> implements RadioGroup.OnCheckedChangeListener {


    private String bidType;
    private Intent mIntent;
    private int page = 1;
    public static int mType = 1;
    List<BidLvYueSZListEnt> listdata = new ArrayList<>();
    BidPerFormLogAdapter adapter;
    private String keyword = "";
    Bundle bundle;
    public static String Tag = "BidPerformanceLvYueSZActivity";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bid_performance_lv_yue_sz;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        bidType = mIntent.getStringExtra("type");
        if (bidType == null) {
            return;
        }
        switch (bidType) {
            case "01":  // 投-支
                mBinding.metitle.setcTxt("投标保证金支记录");
                break;
            case "02": // 投- 收
                mBinding.metitle.setcTxt("投标保证金收记录");
                break;
            case "03":// 履约-支
                mBinding.metitle.setcTxt("履约保证金支记录");
                break;
            case "04":// 履约 -收
                mBinding.metitle.setcTxt("履约保证金收记录");
                break;
        }
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
        adapter = new BidPerFormLogAdapter(R.layout.item_list_projectapprovalog, listdata);
        mBinding.recyclerview.setAdapter(adapter);
        mBinding.radioGroup.setOnCheckedChangeListener(this);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            bundle = new Bundle();
            bundle.putString("Tag", Tag);
            bundle.putString("id", listdata.get(position).id);
            bundle.putString("type", bidType);
            openActivity(BidPerFormTbLySZLoglDetActivity.class, bundle);
            if (listdata.get(position).read == 0) {
                msgUdate(listdata.get(position).msgId, position);
            }
        });
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
            ProjectAppLogEnt appLogEnt = new ProjectAppLogEnt();
            appLogEnt.id = listdata.get(position).id;
            appLogEnt.processInstId = listdata.get(position).processInstId;

            switch (view.getId()) {
                case R.id.bnt1:
                    switch (bidType) { // 撤销
                        case "01":  // 投-支
                            auditAdd("3", AppConfig.status.value3, appLogEnt);
                            break;
                        case "02": // 投- 收
                            auditAdd("3", AppConfig.status.value4, appLogEnt);
                            break;
                        case "03":// 履约-支
                            auditAdd("3", AppConfig.status.value5, appLogEnt);
                            break;
                        case "04":// 履约 -收
                            auditAdd("3", AppConfig.status.value6, appLogEnt);
                            break;
                    }
                    break;
                case R.id.bnt2:// 拒绝
                    switch (bidType) {
                        case "01":
                            auditAdd("2", AppConfig.status.value3, appLogEnt);
                            break;
                        case "02":
                            auditAdd("2", AppConfig.status.value4, appLogEnt);
                            break;
                        case "03":
                            auditAdd("2", AppConfig.status.value5, appLogEnt);
                            break;
                        case "04":
                            auditAdd("2", AppConfig.status.value6, appLogEnt);
                            break;
                    }
                    break;
                case R.id.bnt3:// 同意
                    switch (bidType) {
                        case "01":
                            auditAdd("1", AppConfig.status.value3, appLogEnt);
                            break;
                        case "02":
                            auditAdd("1", AppConfig.status.value4, appLogEnt);
                            break;
                        case "03":
                            auditAdd("1", AppConfig.status.value5, appLogEnt);
                            break;
                        case "04":
                            auditAdd("1", AppConfig.status.value6, appLogEnt);
                            break;
                    }
                    break;
            }
            if (listdata.get(position).read == 0) {
                msgUdate(listdata.get(position).msgId, position);
            }
        });
        mBinding.pendingBtn.setChecked(true);

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
                mBinding.refreshlayout.setEnableLoadMore(false);
                break;
            case R.id.processed_btn://已处理
                mType = 2;
                mBinding.refreshlayout.setEnableLoadMore(true);
                break;
            case R.id.copy_to_me_btn://抄送给我的
                mType = 3;
                mBinding.refreshlayout.setEnableLoadMore(true);
                break;
            case R.id.yitijioa_btn:// 已提交的
                mType = 4;
                mBinding.refreshlayout.setEnableLoadMore(true);
                break;
        }
        page = 1;
        getListData(Constants.RequestMode.FRIST);
    }

    /**
     * 消息更新成功
     *
     * @param position
     */
    @Override
    protected void msgUpdateSuccess(int position) {
        super.msgUpdateSuccess(position);
        listdata.get(position).read = 1;
        adapter.notifyDataSetChanged();
    }

    private String getKeyWord() {
        return mBinding.etSearch.getText().toString().trim();
    }

    private void getListData(Constants.RequestMode mode) {
        showLoading();
        HttpUtil.bid_findByUser(getPer()).execute(new JsonCallback<Result<List<BidLvYueSZListEnt>>>() {
            @Override
            public void onSuccess(Result<List<BidLvYueSZListEnt>> result, Call call, Response response) {
                mBinding.emptylayout.showContent();
                mBinding.refreshlayout.finishRefresh(); // 刷新完成
                mBinding.refreshlayout.finishLoadMore();
                closeLoading();
                if (mType == 1) {
                    if (result.data != null && result.data.size() > 0) {
                        mBinding.pendingBtn.setText("待处理(" + result.data.size() + ")");
                    } else {
                        mBinding.pendingBtn.setText("待处理");
                    }
                }
                if (mode == Constants.RequestMode.FRIST) {
                    listdata.clear();
                    if (result.data != null && result.data.size() > 0) {
                        listdata.addAll(result.data);
                    } else {
                        mBinding.emptylayout.showEmpty();
                    }
                } else if (mode == Constants.RequestMode.LOAD_MORE) {
                    if (result.data != null && result.data.size() > 0) {
                        List<BidLvYueSZListEnt> list = result.data;
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
//        page = 1;
////        getListData(Constants.RequestMode.FRIST);

    }


    /**
     * 参数
     *
     * @return
     */
    private HashMap<String, Object> getPer() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("stats", mType + "");
        map.put("keyword", keyword);
        map.put("page", page + "");
        map.put("bidType", bidType);
        return map;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            page = 1;
            getListData(Constants.RequestMode.FRIST);
        }
    }

    @Override
    protected void successTreatment() {
        super.successTreatment();
        page = 1;
        getListData(Constants.RequestMode.FRIST);
    }
}
