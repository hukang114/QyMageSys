package com.qymage.sys.ui.act;

import android.app.Dialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityOpenAfterWorkBinding;

import cn.leo.click.SingleClick;


/**
 * 上下班打卡
 */
public class OpenAfterWorkActivity extends BBActivity<ActivityOpenAfterWorkBinding> implements View.OnClickListener {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_after_work;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mBinding.dateWindow.setFormat24Hour("HH:mm:ss");
        }

        mBinding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.on_duty_btn:// 在岗

                    break;
                case R.id.business_travel_btn:// 出差

                    break;

                case R.id.leave_btn:// 请假

                    break;
            }
        });

        mBinding.clockImgBg.setOnClickListener(this);
        mBinding.locateBtn.setOnClickListener(this);
        mBinding.remarksBtn.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
    }


    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clock_img_bg:// 打卡

                break;

            case R.id.locate_btn:// 从新定位

                break;

            case R.id.remarks_btn:// 备注
                showNotesDialog();
                break;
        }
    }


    /**
     * 显示备注输入框
     */
    private void showNotesDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.dialog_open_notes_layout, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        EditText editText = dialog.findViewById(R.id.notes_edt);
        TextView submit_btn = dialog.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(v -> {
            mBinding.remarksBtn.setText(editText.getText().toString());
            dialog.dismiss();
        });

    }


}
