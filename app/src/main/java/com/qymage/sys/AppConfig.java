package com.qymage.sys;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.SparseArray;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxf on 2017/8/4.
 */

public class AppConfig {

    //public static final String HOST = "http://livenewtest.yunbaozb.com";//50050
    public static final String HOST = "http://192.168.1.105:8080";
    //http://192.51.188.17
    //外部sd卡
    public static final String DCMI_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();

    public static final int num1000 = 1000;

    // 工作模块的id

    public interface status {

        final int value1=1;//
        final int value2=2;//
        final int value3=3;//
        final int value4=4;//
        final int value5=5;//
        final int value6=6;//
        final int value7=7;//
        final int value8=8;//
        final int value9=9;//
        final int value10=10;//
        final int value11=11;//
        final int value12=12;//
        final int value13=13;//
        final int value14=14;//
        final int value15=15;//

      /*  <enum label="立项" value="1"/>
        <enum label="日报" value="2"/>
        <enum label="投标保证金支" value="3"/>
        <enum label="投标保证金收" value="4"/>
        <enum label="履约保证金支" value="5"/>
        <enum label="履约保证金收" value="6"/>
        <enum label="请假" value="7"/>
        <enum label="合同" value="8"/>
        <enum label="付款" value="9"/>
        <enum label="收款" value="10"/>
        <enum label="开票" value="11"/>
        <enum label="收票" value="12"/>
        <enum label="借款" value="13"/>
        <enum label="月报" value="14"/>
        <enum label="通知" value="15"/>*/


    }






}
