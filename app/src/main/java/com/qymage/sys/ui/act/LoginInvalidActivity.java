package com.qymage.sys.ui.act;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qymage.sys.AppApplication;
import com.qymage.sys.R;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.util.AppManager;
import com.qymage.sys.common.util.MeventKey;
import com.qymage.sys.common.util.MyEvtnTools;
import com.qymage.sys.common.util.SPUtils;

import org.greenrobot.eventbus.EventBus;

import cn.leo.click.SingleClick;

/**
 * token 验证过期
 */
public class LoginInvalidActivity extends Activity implements View.OnClickListener {

    public static boolean isShow = true;

    public static void forward(String tip) {
        if (isShow) {
            Intent intent = new Intent(AppApplication.sInstance, LoginInvalidActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("tip", tip);
            AppApplication.sInstance.startActivity(intent);
            isShow = false;
        }
    }


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_login_invalid);
        initView();
    }


    private void initView() {
        TextView textView = (TextView) findViewById(R.id.content);
        String tip = getIntent().getStringExtra("tip");
        textView.setText(tip);
        findViewById(R.id.btn_confirm).setOnClickListener(this);

    }

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        SPUtils.remove(LoginInvalidActivity.this, Constants.token);
        SPUtils.remove(LoginInvalidActivity.this, Constants.openid);
        SPUtils.remove(LoginInvalidActivity.this, Constants.userid);
        EventBus.getDefault().post(new MyEvtnTools(MeventKey.OUTLOGIN));
        // 需要从新登录
        AppManager.getInstance().finishAllActivity();
        if (MainActivity.instance != null) {
            MainActivity.instance.finish();
        }
        openActivity(LoginActivity.class);
        finish();
    }

    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(LoginInvalidActivity.this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivityForResult(intent, 200);
    }
}

