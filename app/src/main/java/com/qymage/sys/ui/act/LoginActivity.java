package com.qymage.sys.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.google.gson.Gson;
import com.qymage.sys.R;
import com.qymage.sys.common.allpay.wxpay.MD5Util;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.http.HttpConsts;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.http.LogUtils;
import com.qymage.sys.common.util.MD5;
import com.qymage.sys.common.util.SPUtils;
import com.qymage.sys.databinding.ActivityLoginBinding;
import com.qymage.sys.ui.entity.LoginEntity;

import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 用户登录
 */
public class LoginActivity extends BBActivity<ActivityLoginBinding> implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.btnLogin.setOnClickListener(this);
        mBinding.ivCleanPhone.setOnClickListener(this);
        mBinding.cleanPassword.setOnClickListener(this);
        initEvent();
        setSetPsdChek();
    }

    /**
     * 设置是否选择记住密码
     */
    private void setSetPsdChek() {
        String username = SPUtils.get(this, Constants.USER_NAME, "").toString();
        String psd = SPUtils.get(this, Constants.REM_PSD, "").toString();
        mBinding.etMobile.setText(username);
        if (psd != null && !psd.equals("")) {
            mBinding.remPsd.setChecked(true);
            mBinding.etCode.setText(psd);
        }


    }


    @Override
    protected void initData() {
        super.initData();
    }


    private void initEvent() {
        mBinding.etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mBinding.ivCleanPhone.getVisibility() == View.GONE) {
                    mBinding.ivCleanPhone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mBinding.ivCleanPhone.setVisibility(View.GONE);
                }
            }
        });
        mBinding.etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && mBinding.cleanPassword.getVisibility() == View.GONE) {
                    mBinding.cleanPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    mBinding.cleanPassword.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    showToast("请输入数字或字母");
                    s.delete(temp.length() - 1, temp.length());
                    mBinding.etCode.setSelection(s.length());
                }
            }
        });

    }


    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (isEmpty()) {
                    LoginMoth();
                }
                break;
            case R.id.ivCleanPhone:
                mBinding.etMobile.setText("");
                break;
            case R.id.clean_password:
                mBinding.etCode.setText("");
                break;
        }
    }

    private void LoginMoth() {
        showLoading();
        HttpUtil.UserLogin(HttpConsts.LOGIN, getPar()).execute(new JsonCallback<Result<LoginEntity>>() {
            @Override
            public void onSuccess(Result<LoginEntity> result, Call call, Response response) {
                closeLoading();
                SPUtils.put(LoginActivity.this, Constants.USER_NAME, mBinding.etMobile.getText().toString());
                if (mBinding.remPsd.isChecked()) {
                    SPUtils.put(LoginActivity.this, Constants.REM_PSD, mBinding.etCode.getText().toString());
                } else {
                    SPUtils.remove(LoginActivity.this, Constants.REM_PSD);
                }
                if (result.data != null) {
                    SPUtils.put(LoginActivity.this, Constants.token, result.data.token);
                    SPUtils.put(LoginActivity.this, Constants.userid, result.data.info.userAccount);
                    SPUtils.put(LoginActivity.this, Constants.userinfo, new Gson().toJson(result.data.info));
                }
                showToast("登录成功");
                openActivity(MainActivity.class);
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


    private HashMap<String, String> getPar() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userCode", mBinding.etMobile.getText().toString().trim());
        map.put("password", mBinding.etCode.getText().toString());
        return map;
    }

    private boolean isEmpty() {
        if (TextUtils.isEmpty(mBinding.etMobile.getText().toString())) {
            showToast("请输入登录账号");
            return false;
        } else if (TextUtils.isEmpty(mBinding.etCode.getText().toString())) {
            showToast("请输入登录密码");
            return false;
        } else {
            return true;
        }
    }
}
