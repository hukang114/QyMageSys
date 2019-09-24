package com.qymage.sys.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qymage.sys.R;
import com.qymage.sys.common.base.baseFragment.BBFragment;
import com.qymage.sys.common.base.baseFragment.FragmentLazy;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.FragmentWorkBinding;
import com.qymage.sys.ui.act.MainActivity;
import com.qymage.sys.ui.adapter.WorkListAdapter;
import com.qymage.sys.ui.entity.WorkListEnt;

import java.util.ArrayList;
import java.util.List;

/**
 * A 工作
 */
public class WorkFragment extends FragmentLazy<FragmentWorkBinding> {


    List<WorkListEnt> workListEnts = new ArrayList<>();
    WorkListAdapter listAdapter;


    public static WorkFragment newInstance() {
        WorkFragment fragment = new WorkFragment();
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
        return R.layout.fragment_work;
    }


    @Override
    protected void baseInit() {
        super.baseInit();
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        listAdapter = new WorkListAdapter(R.layout.item_list_work, workListEnts);
        mBinding.recyclerview.setAdapter(listAdapter);
        //设置是否开启纯滚动模式
        mBinding.refreshlayout.setEnablePureScrollMode(true);
    }

    @Override
    protected void initData() {

        mBinding.corporateNameTv.setText(VerifyUtils.isEmpty(MainActivity.infoBean.companyName) ? this.getResources().getString(R.string.app_name) : MainActivity.infoBean.companyName);


        //infoBean 根据用户权限 控制不同的工作模块按钮

        List<WorkListEnt.DataBaen> baenList1 = new ArrayList<>();
        baenList1.add(new WorkListEnt.DataBaen("shangban", "上班打卡", R.mipmap.shangban));
        baenList1.add(new WorkListEnt.DataBaen("xiaban", "下班打卡", R.mipmap.xiaban));
        //--------------

        List<WorkListEnt.DataBaen> baenList2 = new ArrayList<>();


//        baenList2.add(new WorkListEnt.DataBaen("huankuan", "还款申请", R.mipmap.huankuan));

        //0-------------------------
        List<WorkListEnt.DataBaen> baenList3 = new ArrayList<>();


        if (MainActivity.infoBean != null && MainActivity.infoBean.processAppBtnVo != null) {

            for (int i = 0; i < MainActivity.infoBean.processAppBtnVo.size(); i++) {
                switch (MainActivity.infoBean.processAppBtnVo.get(i).btnType) {
                    case "01": // 上班打卡

                        break;
                    case "02":// 下班打开

                        break;
                    case "03":// 立项申请
                        baenList1.add(new WorkListEnt.DataBaen("lixiang", "立项申请", R.mipmap.lixiang));
                        break;
                    case "04":// 合同申请
                        baenList1.add(new WorkListEnt.DataBaen("hetong", "合同申请", R.mipmap.hetong));
                        break;
                    case "05":// 付款申请
                        baenList2.add(new WorkListEnt.DataBaen("fukuan", "付款申请", R.mipmap.fukuan));
                        break;
                    case "06":// 收款申请
                        baenList2.add(new WorkListEnt.DataBaen("shoukuan", "收款申请", R.mipmap.shoukuan));
                        break;
                    case "07":// 开票申请
                        baenList2.add(new WorkListEnt.DataBaen("kaipiao", "开票申请", R.mipmap.kaipiao));
                        break;
                    case "08":// 收票申请
                        baenList2.add(new WorkListEnt.DataBaen("shoupiao", "收票申请", R.mipmap.shoupiao));
                        break;
                    case "09":// 投标保证金收
                        baenList2.add(new WorkListEnt.DataBaen("toubiaoz", "投标保证金支", R.mipmap.toubiaoz));
                        break;
                    case "10"://投标保证金支
                        baenList2.add(new WorkListEnt.DataBaen("toubiaos", "投标保证金收", R.mipmap.toubiaos));
                        break;
                    case "11":// 履约保证金支
                        baenList2.add(new WorkListEnt.DataBaen("lvyuez", "履约保证金支", R.mipmap.lvyuez));
                        break;
                    case "12":// 履约保证金收
                        baenList2.add(new WorkListEnt.DataBaen("lvyues", "履约保证金收", R.mipmap.lvyues));
                        break;
                    case "13":// 借款申请
                        baenList2.add(new WorkListEnt.DataBaen("jiekuan", "借款申请", R.mipmap.jiekuan));
                        break;
                    case "14":// 请假申请
                        baenList3.add(new WorkListEnt.DataBaen("qingjia", "请假申请", R.mipmap.qingjia));
                        break;
                    case "15":// 日报
                        baenList3.add(new WorkListEnt.DataBaen("ribao", "日报", R.mipmap.ribao));
                        break;
                    case "16"://月报
                        baenList3.add(new WorkListEnt.DataBaen("yue", "月报", R.mipmap.yue));
                        break;

                }

            }
        }

        workListEnts.add(new WorkListEnt("日常工作中心", baenList1));
        workListEnts.add(new WorkListEnt("财务相关", baenList2));
        workListEnts.add(new WorkListEnt("行政管理", baenList3));

        listAdapter.notifyDataSetChanged();

    }


}
