package com.qymage.sys.ui.act;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

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
import com.qymage.sys.databinding.ActivitySetUpBinding;
import com.qymage.sys.ui.fragment.MyFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 设置
 */

public class SetUpActivity extends BBActivity<ActivitySetUpBinding> implements View.OnClickListener {


    private Bundle bundle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_up;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.aboutRelayout.setOnClickListener(this);
        mBinding.infoSecurityRelayout.setOnClickListener(this);
        mBinding.messageNotifiRelayout.setOnClickListener(this);
        mBinding.userInfoRelayout.setOnClickListener(this);
        mBinding.outloginTv.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_relayout:// 个人信息
                if (MyFragment.infoBean != null) {
                    bundle = new Bundle();
                    bundle.putSerializable("data", MyFragment.infoBean);
                    openActivity(PersonalActivity.class, bundle);
                }
                break;
            case R.id.info_security_relayout:// 修改密码
                openActivity(ChangePasswordActivity.class);
                break;
            case R.id.message_notifi_relayout:// 修改推送通知

                break;
            case R.id.about_relayout:// 关于我们
                openActivity(AboutActivity.class);
                break;

            case R.id.outlogin_tv:// 退出登录
                showOutLoginDialog();
                break;
        }

    }

    private void showOutLoginDialog() {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.dialog_outlogin_layout, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.findViewById(R.id.tv_outlogin).setOnClickListener(view1 -> {
            dialog.dismiss();
            sendOutLogin();
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> dialog.dismiss());
    }

    /**
     * 、
     * 提交退出登录
     */
    private void sendOutLogin() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("userCode", getUserId());
        showLoading();
        HttpUtil.logout(HttpConsts.LOGOUT, map).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                showToast("退出登录成功");
                SPUtils.remove(SetUpActivity.this, Constants.token);
                SPUtils.remove(SetUpActivity.this, Constants.openid);
                SPUtils.remove(SetUpActivity.this, Constants.userid);
                EventBus.getDefault().post(new MyEvtnTools(MeventKey.OUTLOGIN));
                // 需要从新登录
                AppManager.getInstance().finishAllActivityNoLogin();
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
}
