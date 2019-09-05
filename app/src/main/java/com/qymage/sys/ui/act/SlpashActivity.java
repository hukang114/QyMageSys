package com.qymage.sys.ui.act;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.util.SPUtils;
import com.qymage.sys.databinding.ActivitySlpashBinding;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.leo.click.SingleClick;


public class SlpashActivity extends BBActivity<ActivitySlpashBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_slpash;
    }

    private int recLen = 5;//跳过倒计时提示5秒
    Timer timer = new Timer();

    @Override
    protected void initView() {
        super.initView();
        // 隐藏app状态栏
        ImmersionBar.with(this).fitsSystemWindows(true).transparentBar().init();
        applyPermission();
        setRestPush();
        starAct();
        mBinding.tvJumpOver.setOnClickListener(new View.OnClickListener() {
            @SingleClick(2000)
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                }
                if (isLogin(LoginActivity.class)) {
                    startActivity(new Intent(SlpashActivity.this, MainActivity.class));
                }
                finish();
            }
        });
    }


    private void starAct() {
        timer.schedule(task, 1000, 1000);//等待时间一秒，停顿时间一秒

    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() { // UI thread
                @Override
                public void run() {
                    recLen--;
                    mBinding.tvJumpOver.setText("跳过 " + recLen);
                    if (recLen < 0) {
                        timer.cancel();
                        mBinding.tvJumpOver.setVisibility(View.GONE);//倒计时到0隐藏字体
                        if (isLogin(LoginActivity.class)) {
                            startActivity(new Intent(SlpashActivity.this, MainActivity.class));
//                            openActivity(LoginActivity.class);
                        }
                        finish();
                    }
                }
            });
        }
    };


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
    }

    private void setRestPush() {
        String userid = (String) SPUtils.get(this, Constants.userid, "");
        if (userid != null && !userid.equals("")) {
            JPushInterface.setAlias(SlpashActivity.this, userid, (code, alias, tags) -> {
                String logs;
                switch (code) {
                    case 0:
                        logs = "Set tag and alias success";
                        break;

                    case 6002:
                        logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                        break;

                    default:
                        logs = "Failed with errorCode = " + code;
                        break;
                }
                Log.i("JPush", logs);
            });
        } else {
            SPUtils.remove(SlpashActivity.this, Constants.userid);
        }
    }


    // 注册权限
    private void applyPermission() {
        XXPermissions.with(this)
                //.constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES)
                //不指定权限则自动获取清单中的危险权限
                .permission(Permission.Group.STORAGE, Permission.Group.LOCATION)
                .permission(Permission.CAMERA)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (!isAll) {
                            showToast("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            showToast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(mActivity);
                        } else {
                            showToast("获取权限失败！");
                            XXPermissions.gotoPermissionSettings(mActivity);
                        }
                    }
                });

    }


}
