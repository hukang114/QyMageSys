package com.qymage.sys.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qymage.sys.R;
import com.qymage.sys.common.base.baseFragment.FragmentLazy;
import com.qymage.sys.common.util.PicasooUtil;
import com.qymage.sys.databinding.FragmentMyBinding;
import com.qymage.sys.ui.act.LoginActivity;
import com.qymage.sys.ui.act.MyAttendanceActivity;
import com.qymage.sys.ui.act.MyLoanActivity;
import com.qymage.sys.ui.act.PersonalActivity;
import com.qymage.sys.ui.act.SetUpActivity;
import com.qymage.sys.ui.adapter.MyMenuAdapter;
import com.qymage.sys.ui.entity.MyMenuEnt;

import java.util.ArrayList;
import java.util.List;

import cn.leo.click.SingleClick;

/**
 * A 我的
 */
public class MyFragment extends FragmentLazy<FragmentMyBinding> implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener {


    List<MyMenuEnt> myMenuEnts = new ArrayList<>();
    MyMenuAdapter menuAdapter;


    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_my;
    }


    @Override
    protected void baseInit() {
        super.baseInit();
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        menuAdapter = new MyMenuAdapter(R.layout.item_list_mymenu, myMenuEnts);
        mBinding.recyclerview.setAdapter(menuAdapter);
        mBinding.refreshlayout.setEnablePureScrollMode(true);
        menuAdapter.setOnItemClickListener(this);
        mBinding.infoRelayout.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        myMenuEnts.add(new MyMenuEnt("wdht", "我的合同", R.mipmap.my_1));
        myMenuEnts.add(new MyMenuEnt("wdbx", "我的报销", R.mipmap.my_2));
        myMenuEnts.add(new MyMenuEnt("wdjhk", "我的借还款", R.mipmap.my_3));
        myMenuEnts.add(new MyMenuEnt("wdkq", "我的考勤", R.mipmap.my_4));
        myMenuEnts.add(new MyMenuEnt("seting", "设置", R.mipmap.my_5));
        menuAdapter.notifyDataSetChanged();

        PicasooUtil.setImageResource("http://img.jf258.com/uploads/2014-08-13/061816616.jpg", mBinding.userHead, 360);


    }


    @SingleClick(2000)
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (myMenuEnts.get(position).keyname) {
            case "wdht": // 我的合同
                openActivity(LoginActivity.class);

                break;
            case "wdbx": // 我的报销

                break;

            case "wdjhk": // 我的借还款
                openActivity(MyLoanActivity.class);
                break;
            case "wdkq": // 我的考勤
                openActivity(MyAttendanceActivity.class);

                break;
            case "seting": // 设置
                openActivity(SetUpActivity.class);
                break;
        }
    }

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_relayout:
                openActivity(PersonalActivity.class);
                break;
        }

    }
}
