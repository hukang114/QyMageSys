package com.qymage.sys.ui.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BaseActivity;
import com.qymage.sys.common.tools.ToastUtil;
import com.qymage.sys.ui.act.ApplicationRepaymentActivity;
import com.qymage.sys.ui.act.LoanApplicationActivity;
import com.qymage.sys.ui.act.OpenAfterWorkActivity;
import com.qymage.sys.ui.act.ProjectApprovaApplylActivity;
import com.qymage.sys.ui.act.ProjectApprovaLoglActivity;
import com.qymage.sys.ui.entity.WorkListEnt;

import java.util.List;

/**
 * Created by admin on 2019/8/24.
 */

public class WorkListAdapter extends BaseQuickAdapter<WorkListEnt, BaseViewHolder> {


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

                break;

            case "shoukuan"://收款申请

                break;

            case "fukuan"://付款申请

                break;
            case "kaipiao"://开票申请

                break;
            case "shoupiao"://收票申请

                break;
            case "toubiaoz"://投标保证金支

                break;
            case "toubiaos"://投标保证金收

                break;
            case "lvyuez"://履约保证金支

                break;
            case "lvyues"://履约保证金收

                break;
            case "jiekuan"://借款申请
                openActivity(LoanApplicationActivity.class);

                break;
            case "huankuan"://还款申请
                openActivity(ApplicationRepaymentActivity.class);
                break;
        }
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
