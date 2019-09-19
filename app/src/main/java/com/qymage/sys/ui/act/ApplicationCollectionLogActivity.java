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
import com.qymage.sys.databinding.ActivityApplicationCollectionLogBinding;
import com.qymage.sys.ui.adapter.AppCollectioLogAdapter;
import com.qymage.sys.ui.adapter.BidPerFormLogAdapter;
import com.qymage.sys.ui.entity.ApplicationCollectionLogList;
import com.qymage.sys.ui.entity.BidLvYueSZListEnt;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/***
 *收款申请记录，付款申请记录 ，开票申请记录，收票申请记录 申请记录
 */
public class ApplicationCollectionLogActivity extends BBActivity<ActivityApplicationCollectionLogBinding> implements RadioGroup.OnCheckedChangeListener {


    private String bidType;
    private Intent mIntent;
    private int page = 1;
    public static int mType = 1;
    List<ApplicationCollectionLogList> listdata = new ArrayList<>();
    AppCollectioLogAdapter adapter;
    private String keyword = "";
    Bundle bundle;
    public static String Tag = "ApplicationCollectionLogActivity";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_application_collection_log;
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
            case "1":  //收款
                mBinding.metitle.setcTxt("收款申请记录");
                break;
            case "2": // 付款
                mBinding.metitle.setcTxt("付款申请记录");
                break;
            case "3":// 开票
                mBinding.metitle.setcTxt("开票申请记录");
                break;
            case "4":// 收票
                mBinding.metitle.setcTxt("收票申请记录");
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
        adapter = new AppCollectioLogAdapter(R.layout.item_list_projectapprovalog, listdata);
        mBinding.recyclerview.setAdapter(adapter);
        mBinding.radioGroup.setOnCheckedChangeListener(this);
        mBinding.pendingBtn.setChecked(true);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            bundle = new Bundle();
            bundle.putString("Tag", Tag);
            bundle.putString("id", listdata.get(position).id);
            bundle.putString("type", bidType);
            openActivity(AppColletionLoglDetActivity.class, bundle);
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
            appLogEnt.processInstId = listdata.get(position).processInstanceId;

            switch (view.getId()) {
                case R.id.bnt1:
                    switch (bidType) { // 撤销
                        case "1":  // 收款
                            auditAdd("3", AppConfig.status.value10, appLogEnt);
                            break;
                        case "2": //付款
                            auditAdd("3", AppConfig.status.value9, appLogEnt);
                            break;
                        case "3":// 开票
                            auditAdd("3", AppConfig.status.value11, appLogEnt);
                            break;
                        case "4":// 收票
                            auditAdd("3", AppConfig.status.value12, appLogEnt);
                            break;
                    }
                    break;
                case R.id.bnt2:// 拒绝
                    switch (bidType) {
                        case "1":
                            auditAdd("2", AppConfig.status.value10, appLogEnt);
                            break;
                        case "2":
                            auditAdd("2", AppConfig.status.value9, appLogEnt);
                            break;
                        case "3":
                            auditAdd("2", AppConfig.status.value11, appLogEnt);
                            break;
                        case "4":
                            auditAdd("2", AppConfig.status.value12, appLogEnt);
                            break;
                    }
                    break;
                case R.id.bnt3:// 同意
                    switch (bidType) {
                        case "1":
                            auditAdd("1", AppConfig.status.value10, appLogEnt);
                            break;
                        case "2":
                            auditAdd("1", AppConfig.status.value9, appLogEnt);
                            break;
                        case "3":
                            auditAdd("1", AppConfig.status.value11, appLogEnt);
                            break;
                        case "4":
                            auditAdd("1", AppConfig.status.value12, appLogEnt);
                            break;
                    }
                    break;
            }
        });
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
        HttpUtil.money_listMoneyQuery(getPer()).execute(new JsonCallback<Result<List<ApplicationCollectionLogList>>>() {
            @Override
            public void onSuccess(Result<List<ApplicationCollectionLogList>> result, Call call, Response response) {
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
                        List<ApplicationCollectionLogList> list = result.data;
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
        map.put("stats", mType + "");
        map.put("keyword", keyword);
        map.put("page", page + "");
        map.put("type", bidType);
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

    /**
     * 审批处理成功
     */
    @Override
    protected void successTreatment() {
        super.successTreatment();
        page = 1;
        getListData(Constants.RequestMode.FRIST);
    }


}
