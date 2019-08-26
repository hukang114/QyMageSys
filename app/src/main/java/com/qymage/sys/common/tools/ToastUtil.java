package com.qymage.sys.common.tools;

import android.content.Context;
import android.widget.Toast;


/**
 * 类名：
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/5/614:19
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ToastUtil {

    private static Toast mToast;

    public ToastUtil() {

    }

    public static void showToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showToast(Context context, int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(context, context.getResources().getString(resId), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(context.getResources().getString(resId));
        }
        mToast.show();
    }


}
