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

    //public static final String HOST = "http://livenewtest.yunbaozb.com";
    public static final String HOST = "http://120.79.18.66:8085";
    //http://192.51.188.17
    //外部sd卡
    public static final String DCMI_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();


}
