package com.qymage.sys.ui.act;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.RadioGroup;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.databinding.ActivityProjectApprovaLoglBinding;
import com.qymage.sys.ui.adapter.MyLoanListAdapter;
import com.qymage.sys.ui.adapter.ProjectApprovaLogAdapter;
import com.qymage.sys.ui.entity.MyLoanEnt;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 立项记录
 */
public class ProjectApprovaLoglActivity extends BBActivity<ActivityProjectApprovaLoglBinding> implements RadioGroup.OnCheckedChangeListener {


    private int page = 1;
    private int mType = 1;
    List<ProjectAppLogEnt> listdata = new ArrayList<>();
    ProjectApprovaLogAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_approva_logl;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getListData(Constants.RequestMode.FRIST);

            }
        });
        mBinding.refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getListData(Constants.RequestMode.LOAD_MORE);
            }
        });
        adapter = new ProjectApprovaLogAdapter(R.layout.item_list_projectapprovalog, listdata);
        mBinding.recyclerview.setAdapter(adapter);
        mBinding.radioGroup.setOnCheckedChangeListener(this);
        mBinding.pendingBtn.setChecked(true);


    }

    private void getListData(Constants.RequestMode mode) {

    }

    @Override
    protected void initData() {
        super.initData();
        listdata.add(new ProjectAppLogEnt("http://b-ssl.duitang.com/uploads/item/201901/15/20190115181136_ZLMrV.thumb.700_0.jpeg",
                "张三采购申请",
                "2019-08-28 10:20",
                "市场部",
                "5000元",
                "企业管理项目采购",
                2));

        listdata.add(new ProjectAppLogEnt("http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20190316/a2b4fe8ce83a453a91354d6e88751f2a.jpeg",
                "三毛购申请",
                "2019-08-26 10:20",
                "销售",
                "10000元",
                "企业管理项目采购",
                2));

        adapter.notifyDataSetChanged();

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
    private HashMap<String, String> getPer() {
        HashMap<String, String> map = new HashMap<>();
        return map;
    }
}
