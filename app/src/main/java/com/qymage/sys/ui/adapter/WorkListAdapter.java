package com.qymage.sys.ui.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.tools.ToastUtil;
import com.qymage.sys.ui.act.ApplicationCollectionActivity;
import com.qymage.sys.R;
import com.qymage.sys.ui.act.ApplicationRepaymentActivity;
import com.qymage.sys.ui.act.AskForLeaveActivity;
import com.qymage.sys.ui.act.BiddingMarginShouActivity;
import com.qymage.sys.ui.act.BiddingMarginZhiActivity;
import com.qymage.sys.ui.act.ContractApplicationActivity;
import com.qymage.sys.ui.act.DailyReportActivity;
import com.qymage.sys.ui.act.LoanApplicationActivity;
import com.qymage.sys.ui.act.MonthReportActivity;
import com.qymage.sys.ui.act.OpenAfterWorkActivity;
import com.qymage.sys.ui.act.PerformanceBondShouActivity;
import com.qymage.sys.ui.act.PerformanceBondZhiActivity;
import com.qymage.sys.ui.act.ProjectApprovaApplylActivity;
import com.qymage.sys.ui.entity.HasClockOutEnt;
import com.qymage.sys.ui.entity.WorkListEnt;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by admin on 2019/8/24.
 */

public class WorkListAdapter extends BaseQuickAdapter<WorkListEnt, BaseViewHolder> {


    Bundle bundle;


    public WorkListAdapter(int layoutResId, @Nullable List<WorkListEnt> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkListEnt item) {
        helper.setText(R.id.cate_title, item.tilte);

        RecyclerView recview5 = helper.getView(R.id.child_recyclerview);
        int tag = recview5.getTag(R.id.child_recyclerview) == null ? 0 : (int) recview5.getTag(R.id.child_recyclerview);
        if (tag != 1) {
            recview5.setLayoutManager(new GridLayoutManager(mContext, 4));
            // 创建适配器
            WorkListChildAdapter childAdapter = new WorkListChildAdapter(R.layout.item_list_child_work, item.baenList);
            recview5.setAdapter(childAdapter);
            recview5.setTag(R.id.child_recyclerview, 1);
            childAdapter.setOnItemClickListener((adapter, view, position) -> {
                setOnItemClick(item.baenList.get(position).keyname);
            });
        }

    }


    private void setOnItemClick(String keyname) {
        switch (keyname) {
            case "shangban":// 上班打卡
                openActivity(OpenAfterWorkActivity.class);
                break;

            case "xiaban"://下班打卡
                openActivity(OpenAfterWorkActivity.class);
                break;
            case "lixiang"://立项申请
                openActivity(ProjectApprovaApplylActivity.class);

                break;
            case "hetong"://合同申请
                openActivity(ContractApplicationActivity.class);

                break;

            case "shoukuan"://收款申请
                bundle = new Bundle();
                bundle.putString("type", "1");
                openActivity(ApplicationCollectionActivity.class, bundle);
                break;

            case "fukuan"://付款申请
                bundle = new Bundle();
                bundle.putString("type", "2");
                openActivity(ApplicationCollectionActivity.class, bundle);
                break;
            case "kaipiao"://开票申请
                bundle = new Bundle();
                bundle.putString("type", "3");
                openActivity(ApplicationCollectionActivity.class, bundle);
                break;
            case "shoupiao"://收票申请
                bundle = new Bundle();
                bundle.putString("type", "4");
                openActivity(ApplicationCollectionActivity.class, bundle);
                break;
            case "toubiaoz"://投标保证金支
                bundle = new Bundle();
                bundle.putString("type", "1");
                openActivity(BiddingMarginZhiActivity.class, bundle);

                break;
            case "toubiaos"://投标保证金收
                bundle = new Bundle();
                bundle.putString("type", "2");
                openActivity(BiddingMarginZhiActivity.class, bundle);

                break;
            case "lvyuez"://履约保证金支
                bundle = new Bundle();
                bundle.putString("type", "3");
                openActivity(BiddingMarginZhiActivity.class, bundle);
                break;
            case "lvyues"://履约保证金收
                bundle = new Bundle();
                bundle.putString("type", "4");
                openActivity(BiddingMarginZhiActivity.class, bundle);
                break;
            case "jiekuan"://借款申请
                openActivity(LoanApplicationActivity.class);

                break;
            case "huankuan"://还款申请
                openActivity(ApplicationRepaymentActivity.class);
                break;
            case "qingjia":// 请假
                openActivity(AskForLeaveActivity.class);
                break;
            case "yue":// 月报
                openActivity(MonthReportActivity.class);
                break;
            case "ribao":// 日报
                getChkout();
                break;
        }
    }

    private void getChkout() {
        HttpUtil.attendance_HasClockOut(new HashMap<>()).execute(new JsonCallback<Result<HasClockOutEnt>>() {
            @Override
            public void onSuccess(Result<HasClockOutEnt> result, Call call, Response response) {
                if (result.data != null && result.data.clockOut == 1) {
                    openActivity(DailyReportActivity.class);
                } else {
                    ToastUtil.showToast(mContext, "您今天还没有打过下班卡");
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                ToastUtil.showToast(mContext, e.getMessage());
            }
        });

    }


    /**
     * 跳转Activity
     *
     * @param activityClass 目标Activity类名称
     * @param bundle        Bundle 对象
     */
    public void intoActivity(Class<?> activityClass, Bundle bundle) {
        Intent into = new Intent(mContext, activityClass);
        if (bundle != null) {
            into.putExtra("bundle", bundle);
        }
        mContext.startActivity(into);
    }

    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(mContext, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        mContext.startActivity(intent);
    }


}
