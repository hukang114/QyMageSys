package com.qymage.sys.ui.act;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.KeyBordUtil;
import com.qymage.sys.common.widget.FrameEmptyLayout;
import com.qymage.sys.databinding.ActivityProjectApprovaLoglBinding;
import com.qymage.sys.ui.adapter.MyLoanListAdapter;
import com.qymage.sys.ui.adapter.ProjectApprovaLogAdapter;
import com.qymage.sys.ui.entity.ChoiceContractLogEnt;
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
 * 立项记录
 */
public class ProjectApprovaLoglActivity extends BBActivity<ActivityProjectApprovaLoglBinding> {


    private int page = 1;
    public static int mType = 1;
    List<ProjectAppLogEnt> listdata = new ArrayList<>();
    ProjectApprovaLogAdapter adapter;
    private String keyword = "";
    Bundle bundle;
    public static String Tag = "ProjectApprovaLoglActivity";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_approva_logl;
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
        adapter = new ProjectApprovaLogAdapter(R.layout.item_list_projectapprovalog, listdata);
        mBinding.recyclerview.setAdapter(adapter);
        mBinding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
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
        });
        mBinding.pendingBtn.setChecked(true);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            bundle = new Bundle();
            bundle.putString("Tag", Tag);
            bundle.putString("id", listdata.get(position).id);
            openActivity(ProjectApprovaLoglDetActivity.class, bundle);
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
            switch (view.getId()) {
                case R.id.bnt1:// 撤回
                    auditAdd("3", AppConfig.status.value1, listdata.get(position));
                    break;
                case R.id.bnt2:// 拒绝
                    auditAdd("2", AppConfig.status.value1, listdata.get(position));
                    break;
                case R.id.bnt3:// 同意
                    auditAdd("1", AppConfig.status.value1, listdata.get(position));
                    break;
            }
            if (listdata.get(position).read == 0) {
                msgUdate(listdata.get(position).msgId, position);
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

    /**
     * 审批成功处理
     */
    @Override
    protected void successTreatment() {
        super.successTreatment();
        page = 1;
        getListData(Constants.RequestMode.FRIST);
    }

    private String getKeyWord() {
        return mBinding.etSearch.getText().toString().trim();
    }

    private void getListData(Constants.RequestMode mode) {
        showLoading();
        HttpUtil.project_findByUser(getPer()).execute(new JsonCallback<Result<List<ProjectAppLogEnt>>>() {
            @Override
            public void onSuccess(Result<List<ProjectAppLogEnt>> result, Call call, Response response) {
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
                        List<ProjectAppLogEnt> list = result.data;
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
//        getListData(Constants.RequestMode.FRIST);

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
}
