package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.DateUtil;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.ui.entity.ProjectApprovaLoglDetEnt;

import java.util.List;

/**
 * 类名：
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1620:18
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ProcessListAdapter extends BaseQuickAdapter<ProjectApprovaLoglDetEnt.ActivityVoListBean, BaseViewHolder> {

    List<ProjectApprovaLoglDetEnt.ActivityVoListBean> listdata;

    public ProcessListAdapter(int layoutResId, @Nullable List<ProjectApprovaLoglDetEnt.ActivityVoListBean> data) {
        super(layoutResId, data);
        this.listdata = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProjectApprovaLoglDetEnt.ActivityVoListBean item) {
        TextView name_tv = helper.getView(R.id.tvDot);
        TextView centet_div = helper.getView(R.id.centet_div);
        if (helper.getPosition() == listdata.size() - 1) {
            centet_div.setVisibility(View.GONE);
        } else {
            centet_div.setVisibility(View.VISIBLE);
        }
        if (item.userName != null) {
            if (item.userName.length() >= 3) {
                String strh = item.userName.substring(item.userName.length() - 2, item.userName.length());   //截取
                name_tv.setText(strh);
            } else {
                name_tv.setText(item.userName);
            }
        }

        if (item.comment != null && !item.comment.equals("")) {
            helper.setText(R.id.comment_tv, "审批意见：" + item.comment);
        }
        if (item.actType != null && item.actType.equals("startEvent")) {
            helper.setText(R.id.user_name, item.userName + "发起审批");
            helper.setText(R.id.time_tv, DateUtil.formatMillsTo(item.createdDate));
        } else {
            if (item.id != null) {
                String status = VerifyUtils.isEmpty(item.status) ? "" : item.status.equals("1") ? "同意" : item.status.equals("2") ? "拒绝" :
                        item.status.equals("3") ? "撤销" : "";
                helper.setText(R.id.user_name, item.name + "(" + status + ")");
                helper.setText(R.id.time_tv, DateUtil.formatMillsTo(item.createdDate));
            } else {
                helper.setText(R.id.user_name, "正在等待" + item.userName + "审批");
            }

        }

    }


}
