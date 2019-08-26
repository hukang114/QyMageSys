package com.qymage.sys.common.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Decoder.BASE64Encoder;


/**
 * 作者：Administrator on 2017/1/16 18:44
 * 邮箱：45645@163.com
 * Version 3.0.0
 * 类说明：
 */
public class Tools {

    public static Dialog dialog;
    private static SharedPreferences sp;
    private Context instance;
    private String message;


    /**
     * 存入sharedpre String类型
     *
     * @param context
     * @param xml_name  //存入的文件名
     * @param key_name
     * @param key_value
     */
    public static void set_Share_pre(Context context, String xml_name,
                                     String key_name, String key_value) {
        sp = context.getSharedPreferences(xml_name, Context.MODE_PRIVATE);// Context.MODE_PRIVATE只允许自己的程序访问
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key_name, key_value);
        editor.commit();
    }

    /**
     * 取值String 类型
     *
     * @param context
     * @param xml_name
     * @param key_name
     * @return
     */
    public static String get_Share_pre(Context context, String xml_name,
                                       String key_name) {
        sp = context.getSharedPreferences(xml_name, Context.MODE_PRIVATE);// Context.MODE_PRIVATE只允许自己的程序访问

        String getvalue = sp.getString(key_name, "");// 获取不到前面 的值 就获取后面的“”的值

        return getvalue;
    }

    /**
     * 保存实体类对象
     *
     * @param context
     * @param key_name
     * @param oAuth_1
     */
    public static boolean saveClass(Context context, String key_name, Object oAuth_1) {
        sp = context.getSharedPreferences("base64", Context.MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(oAuth_1);
            // 将字节流编码成 的字符窜
//			String oAuth_Base64 = new String(org.apache.commons.codec.binary.Base64.encodeBase64(baos.toByteArray()));
            String oAuth_Base64 = new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));

            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key_name, oAuth_Base64);
            editor.commit();
            return true;
        } catch (IOException e) {
            return false;
        }
        /*Log.e("ok", "存储*//**//*成功");*/
    }


    /**
     * 存入sharedpre float类型
     *
     * @param context
     * @param xml_name  //存入的文件名
     * @param key_name
     * @param key_value
     */
    public static void set_Share_pre2(Context context, String xml_name,
                                      String key_name, float key_value) {
        sp = context.getSharedPreferences(xml_name, Context.MODE_PRIVATE);// Context.MODE_PRIVATE只允许自己的程序访问
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key_name, key_value);
        editor.commit();
    }

    /**
     * 取值String 类型
     *
     * @param context
     * @param xml_name
     * @param key_name
     * @return
     */
    public static float get_Share_pre2(Context context, String xml_name,
                                       String key_name) {
        sp = context.getSharedPreferences(xml_name, Context.MODE_PRIVATE);// Context.MODE_PRIVATE只允许自己的程序访问

        float getvalue = sp.getFloat(key_name, 0);// 获取不到前面 的值 就获取后面的“”的值

        return getvalue;
    }

    /**
     * 存入sharedpre int类型
     *
     * @param context
     * @param xml_name
     * @param key_name
     * @param key_value
     */
    public static void set_Share_pre1(Context context, String xml_name,
                                      String key_name, int key_value) {
        sp = context.getSharedPreferences(xml_name, Context.MODE_PRIVATE);// Context.MODE_PRIVATE只允许自己的程序访问
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key_name, key_value);
        editor.commit();
    }

    /**
     * 取值 int类型
     *
     * @param context
     * @param xml_name
     * @param key_name
     * @return
     */
    public static int get_Share_pre1(Context context, String xml_name,
                                     String key_name) {
        sp = context.getSharedPreferences(xml_name, Context.MODE_PRIVATE);// Context.MODE_PRIVATE只允许自己的程序访问
        int getvalue = sp.getInt(key_name, 0);// 获取不到前面 的值 就获取后面的“”的值
        return getvalue;
    }

    /**
     * @param context  上下文对象
     * @param bitmap   url转成的bitmap对象
     * @param xml_name 缓存的文件名字
     */
    public static void saveBitmapToSharedPreferences(Context context, Bitmap bitmap, String xml_name) {
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
        sp = context.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("image", imageString);
        editor.commit();
    }

    /**
     * @param context  文本对象
     * @param xml_name 存入时的文件名
     * @return 返回一个图像对象Bitmap
     */
    public static Bitmap getBitmapFromSharedPreferences(Context context, String xml_name) {
        sp = context.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        //第一步:取出字符串形式的Bitmap
        String imageString = sp.getString("image", "");
        //第二步:利用Base64将字符串转换为ByteArrayInputStream
        byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        //第三步:利用ByteArrayInputStream生成Bitmap
        Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
        return bitmap;
    }


    /**
     * 清除Share值
     *
     * @param context
     * @param xml_name
     * @param key_name
     */
    public static void del_Share(Context context, String xml_name,
                                 String key_name) {
        sp = context.getSharedPreferences(xml_name, Context.MODE_PRIVATE);// Context.MODE_PRIVATE只允许自己的程序访问
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key_name);
        editor.commit();

    }

    /**
     * 清除share缓存信息
     *
     * @param context
     * @param xml_name
     */
    public static void clearShare(Context context, String xml_name) {
        sp = context.getSharedPreferences(xml_name, Context.MODE_PRIVATE);// Context.MODE_PRIVATE只允许自己的程序访问
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }




    // NETWORK
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connect = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connect == null) {
            return false;
        } else// get all network info
        {
            NetworkInfo[] info = connect.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 启动应用市场的App评分详情页面
    public static void launchAppDetail(Activity activity, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 获取已安装应用商店的包名列表
     *
     * @param context
     * @return
     */
    public static ArrayList<String> queryInstalledMarketPkgs(Activity context) {
        ArrayList<String> pkgs = new ArrayList<String>();
        if (context == null)
            return pkgs;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        if (infos == null || infos.size() == 0)
            return pkgs;
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            String pkgName = "";
            try {
                ActivityInfo activityInfo = infos.get(i).activityInfo;
                pkgName = activityInfo.packageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(pkgName))
                pkgs.add(pkgName);

        }
        return pkgs;
    }



    /**
     * 过滤出已经安装的包名集合
     *
     * @param context
     * @param pkgs
     *            待过滤包名集合
     * @return 已安装的包名集合
     */
    public static ArrayList<String> filterInstalledPkgs(Context context, ArrayList<String> pkgs) {
        ArrayList<String> empty = new ArrayList<String>();
        if (context == null || pkgs == null || pkgs.size() == 0)
            return empty;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPkgs = pm.getInstalledPackages(0);
        int li = installedPkgs.size();
        int lj = pkgs.size();
        for (int j = 0; j < lj; j++) {
            for (int i = 0; i < li; i++) {
                String installPkg = "";
                String checkPkg = pkgs.get(j);
                try {
                    installPkg = installedPkgs.get(i).applicationInfo.packageName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(installPkg))
                    continue;
                if (installPkg.equals(checkPkg)) {
                    empty.add(installPkg);
                    break;
                }

            }
        }
        return empty;
    }


    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }


    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    /**
     * @将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @author QQ986945193
     * @Date 2015-01-26
     * @param path 图片路径
     * @return
     */
    public static String imageToBase64(String path) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {

            InputStream in = new FileInputStream(path);

            data = new byte[in.available()];

            in.read(data);

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);

    }

    /**
     * 根据图片的url路径获得Bitmap对象
     * @param url
     * @return
     */
    public static Bitmap decodeUriAsBitmapFromNet(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;
        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 规则2：至少包含大小写字母及数字中的两种
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }


    /**
     * 规则3：必须同时包含大小写字母及数字
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isContainAll(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLowerCase = false;//定义一个boolean值，用来表示是否包含字母
        boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLowerCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true;
            } else if (Character.isUpperCase(str.charAt(i))) {
                isUpperCase = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLowerCase && isUpperCase && str.matches(regex);
        return isRight;
    }

    /**
     *   判断EditText输入的数字、中文还是字母方法
     */
    public static void whatIsInput(Context context, EditText edInput) {
        String txt = edInput.getText().toString();

        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(txt);
        if (m.matches()) {
            Toast.makeText(context, "输入的是数字", Toast.LENGTH_SHORT).show();
        }
        p = Pattern.compile("[a-zA-Z]");
        m = p.matcher(txt);
        if (m.matches()) {
            Toast.makeText(context, "输入的是字母", Toast.LENGTH_SHORT).show();
        }
        p = Pattern.compile("[\u4e00-\u9fa5]");
        m = p.matcher(txt);
        if (m.matches()) {
            Toast.makeText(context, "输入的是汉字", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 检查手机上是否安装了指定的软件
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 2000;
    private static long lastClickTime;
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    /**
     * String.comparTo方法
     *
     * @param localVersion
     * @param onlineVersion
     * @return
     */
    public static boolean isNewVersionStr(String localVersion, String onlineVersion)
    {
        if (localVersion == null || onlineVersion == null)
        {
            return false;
        }
        return onlineVersion.compareTo(localVersion) > 0 ? true : false;

    }


}
