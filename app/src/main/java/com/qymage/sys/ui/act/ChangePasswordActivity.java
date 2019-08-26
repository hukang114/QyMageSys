package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityChangePasswordBinding;

import java.util.HashMap;

/**
 * 修改登录密码
 */
public class ChangePasswordActivity extends BBActivity<ActivityChangePasswordBinding> implements View.OnClickListener {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.sendBt.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();

    }


    private boolean isCheck() {
        if (TextUtils.isEmpty(mBinding.oldPwdEt.getText().toString())) {
            showToast("请输入旧密码");
            return false;
        } else if (TextUtils.isEmpty(mBinding.pwdEt.getText().toString())) {
            showToast("请输入新密码");
            return false;
        } else if (mBinding.pwdEt.getText().length() < 6) {
            showToast("密码长度不能小于6位数");
            return false;
        } else if (TextUtils.isEmpty(mBinding.pwd2Et.getText().toString())) {
            showToast("请再次输入新密码");
            return false;
        } else if (!mBinding.pwdEt.getText().toString().equals(mBinding.pwd2Et.getText().toString())) {
            showToast("两次设置的密码不一致");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_bt:
                if (isCheck()) {
                    submitData();
                }
                break;
        }
    }

    private void submitData() {

        showToast("修改成功");
        finish();


    }

    private HashMap<String, String> getPer() {
        HashMap<String, String> map = new HashMap<>();
        map.put("newpsd", mBinding.pwdEt.getText().toString());
        map.put("oldpsd", mBinding.oldPwdEt.getText().toString());
        return map;

    }


}


