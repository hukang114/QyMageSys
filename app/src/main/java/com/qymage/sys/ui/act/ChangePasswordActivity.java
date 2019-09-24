package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.util.AppManager;
import com.qymage.sys.common.util.MeventKey;
import com.qymage.sys.common.util.MyEvtnTools;
import com.qymage.sys.common.util.SPUtils;
import com.qymage.sys.databinding.ActivityChangePasswordBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 修改登录密码 修改密码
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
        showLoading();
        HttpUtil.updatePwd(HttpConsts.UPDATEPWD, getPer()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                // 需要从新登录
                showToast("密码修改成功,需要重新登录");
                SPUtils.remove(ChangePasswordActivity.this, Constants.token);
                SPUtils.remove(ChangePasswordActivity.this, Constants.openid);
                SPUtils.remove(ChangePasswordActivity.this, Constants.userid);
                EventBus.getDefault().post(new MyEvtnTools(MeventKey.OUTLOGIN));
                // 需要从新登录
                AppManager.getInstance().finishAllActivity();
                if (MainActivity.instance != null) {
                    MainActivity.instance.finish();
                }
                openActivity(LoginActivity.class);
                finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                showToast(e.getMessage());
            }
        });

    }

    private HashMap<String, Object> getPer() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("newPwd", mBinding.pwdEt.getText().toString());
        map.put("oldPwd", mBinding.oldPwdEt.getText().toString());
        map.put("userCode", getUserId());
        return map;

    }


}


