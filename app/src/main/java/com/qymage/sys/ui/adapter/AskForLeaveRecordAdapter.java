package com.qymage.sys.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.common.util.PicasooUtil;
import com.qymage.sys.ui.entity.AskForLeaveEntity;
import com.qymage.sys.ui.entity.JournalEntity;

import java.util.List;

/**
 * Created by admin on 2019/8/24.
 * 请假记录
 */

public class AskForLeaveRecordAdapter extends BaseQuickAdapter<AskForLeaveEntity, BaseViewHolder> {


    public AskForLeaveRecordAdapter(int layoutResId, @Nullable List<AskForLeaveEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AskForLeaveEntity item) {
        PicasooUtil.setImageResource(item.headimg, helper.getView(R.id.afl_head_img), 360);
        if(item.result_type == 0){
            ((TextView)helper.getView(R.id.shenpi_jieguo_value)).setTextColor(Color.YELLOW);
        }else if(item.result_type == 1){
            ((TextView)helper.getView(R.id.shenpi_jieguo_value)).setTextColor(Color.RED);
        }else if(item.result_type == 2){
            ((TextView)helper.getView(R.id.shenpi_jieguo_value)).setTextColor(Color.GREEN);
        }
        helper.setText(R.id.afl_user_name, item.username+"提交的请假申请")
                .setText(R.id.qingjia_leixing_value, item.afl_type)
                .setText(R.id.qingjia_kaishi_value, item.start_time)
                .setText(R.id.qingjia_jieshu_value, item.end_time)
                .setText(R.id.shenpi_jieguo_value, item.result);

    }


}
