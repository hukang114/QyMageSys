package com.qymage.sys.ui.act;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.RadioGroup;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.databinding.ActivityMyLoanBinding;
import com.qymage.sys.ui.adapter.MyLoanListAdapter;
import com.qymage.sys.ui.entity.MyLoanEnt;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 我的借款
 */
public class MyLoanActivity extends BBActivity<ActivityMyLoanBinding> implements RadioGroup.OnCheckedChangeListener {


    private int page = 1;
    private int mType = 1;
    List<MyLoanEnt> listdata = new ArrayList<>();
    MyLoanListAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_loan;
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
        adapter = new MyLoanListAdapter(R.layout.item_myloan_list, listdata);
        mBinding.recyclerview.setAdapter(adapter);
        mBinding.radioGroup.setOnCheckedChangeListener(this);
        mBinding.pendingBtn.setChecked(true);


    }

    /**
     * 获取数据
     *
     * @param mode
     */
    private void getListData(Constants.RequestMode mode) {

    }

    @Override
    protected void initData() {
        super.initData();
        listdata.add(new MyLoanEnt("http://b-ssl.duitang.com/uploads/item/201901/15/20190115181136_ZLMrV.thumb.700_0.jpeg",
                "张三",
                "2019-08-28 10:20",
                "企业管理项目采购",
                "5000",
                "2019-08-29",
                "客户急需用钱",
                1));

        listdata.add(new MyLoanEnt("http://5b0988e595225.cdn.sohucs.com/q_70,c_zoom,w_640/images/20190316/a2b4fe8ce83a453a91354d6e88751f2a.jpeg",
                "三毛",
                "2019-08-26 10:20",
                "企业管理项目开发费用",
                "10000",
                "2019-08-28",
                "员工需要养家糊口",
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



