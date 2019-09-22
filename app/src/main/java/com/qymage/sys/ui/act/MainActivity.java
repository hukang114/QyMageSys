package com.qymage.sys.ui.act;

import android.os.Process;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BaseActivity;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.common.util.HackyViewPager;
import com.qymage.sys.common.util.SPUtils;
import com.qymage.sys.ui.adapter.HomeViewPagerAdapter;
import com.qymage.sys.ui.entity.LoginEntity;
import com.qymage.sys.ui.fragment.JournalFragment;
import com.qymage.sys.ui.fragment.MyFragment;
import com.qymage.sys.ui.fragment.NewsFragment;
import com.qymage.sys.ui.fragment.WorkFragment;

public class MainActivity extends BaseActivity {


    public static MainActivity instance = null;

    public static HackyViewPager viewpager;
    public static BottomNavigationView navigation;
    public static RadioButton tab1, tab2, tab3, tab4;
    public RadioGroup radioGroup;

    public static LoginEntity.InfoBean infoBean;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (!SPUtils.get(this, Constants.userinfo, "").toString().equals("")) {
            infoBean = gson.fromJson(SPUtils.get(this, Constants.userinfo, "").toString(), LoginEntity.InfoBean.class);
        }
        instance = this;
        checkVersion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;

    }

    private void initView() {
        viewpager = findViewById(R.id.viewpager);
        radioGroup = findViewById(R.id.radiogroup);
        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        tab3 = findViewById(R.id.tab3);
        tab4 = findViewById(R.id.tab4);
        viewpager.setScanScroll(false);
        setupViewPager(viewpager);
        viewpager.setCurrentItem(0);
        tab1.setChecked(true);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.tab1:
                    viewpager.setCurrentItem(0);
                    break;
                case R.id.tab2:
                    viewpager.setCurrentItem(1);
                    break;
                case R.id.tab3:
                    viewpager.setCurrentItem(2);
                    break;
                case R.id.tab4:
                    viewpager.setCurrentItem(3);
                    break;
            }

        });

    }

    private void setupViewPager(HackyViewPager viewpager) {
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(NewsFragment.newInstance());
        adapter.addFragment(WorkFragment.newInstance());
        adapter.addFragment(JournalFragment.newInstance());
        adapter.addFragment(MyFragment.newInstance());
        viewpager.setAdapter(adapter);
    }

    long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        // 退出当前的程序
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) { // 如果两次按键间隔>2000毫秒，则不退出
                showToast("再按一次退出程序");
                firstTime = secondTime;
                return false;
            } else {
                Process.killProcess(Process.myPid());
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 检测版本更新
     */
    private void checkVersion() {

    }


    /**
     * 更具用户按钮权限返回对应的流程定义ID
     *
     * @param btnType
     * @return
     */
    public static String processDefId(String btnType) {
        String processDefId = "";
        if (infoBean != null && infoBean.processAppBtnVo != null && infoBean.processAppBtnVo.size() > 0) {

            for (int i = 0; i < infoBean.processAppBtnVo.size(); i++) {
                if (infoBean.processAppBtnVo.get(i).btnType.equals(btnType)) {
                    processDefId = infoBean.processAppBtnVo.get(i).processDefId;
                    break;
                }
            }
        }
        return processDefId;
    }


}
