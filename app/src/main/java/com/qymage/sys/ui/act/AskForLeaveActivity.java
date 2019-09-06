package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityChangePasswordBinding;

public class AskForLeaveActivity extends BBActivity<ActivityChangePasswordBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ask_for_leave;
    }

    /**
     * ID说明
     * 请选择请假类型      xuanze_qingjialeixing (TextView)
     * 选择开始时间       xuanze_time_start (TextView)
     * 选择结束时间       xuanze_time_end (TextView)
     * 输入时间            shuru_shichang (EditText)
     * 请假事由             qingjia_shiyou (EditText)
     * 请假图片RecylclerView    qingjia_tupian_recy (RecylclerView)
     * 请假图片             qingjia_tupian (ImageView)
     * 添加审批人RecyclerView    qingjia_spr_recyclerview (RecyclerView)
     * 添加审批人图片      qinajia_spr_img (ImageView)
     * 添加抄送人RecyclerView    qingjia_csr_recyclerview (RecyclerView)
     * 添加抄送人图片      qingjia_csr_img (ImageView)
     * 提交按钮      qingjia_save_btn (Button)
     */

    @Override
    protected void initView() {

    }
}
