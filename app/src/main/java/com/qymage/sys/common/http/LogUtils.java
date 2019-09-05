package com.qymage.sys.common.http;

import android.util.Log;

/**
 * LogUtils 输出工具类
 * Used 实现自由的控制日志的打印--《第一行代码》 郭霖
 * 只有当LEVEL常量的值大于或等于对应日志级别值的时候，才会打印日志。
 * 开发阶段，将LEVEL赋值为VERBOSE，上线阶段将LEVEL赋值为NOTHING
 */

public class LogUtils {

    private static final String TAG = "HKLog";
    private static boolean allowD = true;
    private static boolean allowE = true;
    private static boolean allowI = true;
    private static boolean allowV = true;
    private static boolean allowW = true;

    private LogUtils() {
    }

    /**
     * 开启Log
     *
     * @author ZhongDaFeng
     */
    public static void openLog(boolean flag) {
        allowD = flag;
        allowE = flag;
        allowI = flag;
        allowV = flag;
        allowW = flag;
    }

    public static void d(String content) {
        if (!allowD)
            return;
        Log.d(TAG, content);
    }

    public static void e(String content) {
        if (!allowE)
            return;
        Log.e(TAG, content);
    }

    public static void i(String content) {
        if (!allowI)
            return;
        Log.i(TAG, content);
    }

    public static void v(String content) {
        if (!allowV)
            return;
        Log.v(TAG, content);
    }

    public static void w(String content) {
        if (!allowW)
            return;
        Log.w(TAG, content);
    }
}
