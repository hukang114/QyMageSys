package com.qymage.sys.common.util;


import android.util.Base64;
import android.util.Log;


import com.qymage.sys.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Decoder.BASE64Encoder;

/**
 * Created by HK on 2017/8/17.
 * 接口参数的签名验证
 */

public class AppSignUtil {


    /**
     * 所有的参数进行首字母排序
     *
     * @param params
     * @return
     */
    public static Map<String, String> genAppSign(Map<String, String> params) {

        HashMap<String, String> stringHashMap = new HashMap<>();

        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(params.entrySet());

        //排序方法
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                //return (o2.getValue() - o1.getValue());
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
        //排序后
        for (Map.Entry<String, String> m : infoIds) {
            stringHashMap.put(m.getKey(), m.getValue());
            Log.d("Tag", "genAppSign() returned: " + m.getKey() + ":" + m.getValue());

        }
        return stringHashMap;

    }

    /**
     * 取出所有参数排序后的Value 进行拼接加密赋值给 sign
     *
     * @param params
     * @return
     */
    public static String getgenAppSignValue(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();

        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(params.entrySet());

        //排序方法
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                //return (o2.getValue() - o1.getValue());
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
        //排序后
        for (Map.Entry<String, String> m : infoIds) {
            sb.append(m.getKey());
            sb.append("=");
            sb.append(m.getValue());
            sb.append("&");
            System.out.println(m.getKey() + ":" + m.getValue());
        }
        sb.deleteCharAt(sb.length() - 1);
//        sb.append("a5bb98402a7d3823290fb06c7aa8b97b");// 签名密码
        sb.append("&secret=e4cd404b0f9a9c026fd1e5c06e190e2c");


        // 第一遍加密
        String appSign = MD5.getMD5(sb.toString().getBytes());

        // 第二遍加密
//        String againSign = MD5.getMD5(appSign.getBytes());
        String againSign = appSign.toUpperCase();

        return againSign;

    }


    /**
     * 获取参数排序后的值
     *
     * @param username
     * @param password
     * @return
     */
    public static String getAfterencStr(String username, String password) {
        String str = getMosaicStr(username, password);
        if (BuildConfig.DEBUG) Log.d("AppSignUtil ", "str:" + str);
        byte[] b = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(b, Base64.NO_WRAP);
        StringBuilder sb = new StringBuilder(base64);
        sb.insert(base64.length() - 2, randomChar());
        String des = null;
        String sbc = sb.toString().replace(" ", "");
        if (BuildConfig.DEBUG) Log.d("AppSignUtil", "长度：" + sbc.length());
        if (sb.toString().length() % 2 == 0) {
            String a = sbc.substring(0, sbc.toString().length() - 1);
            des = new StringBuffer(a).reverse().toString() + sbc.substring(sbc.length() - 1, sbc.length());
        } else {
            String c = sbc.toString().substring(0, sbc.toString().length() - 2);
            des = new StringBuffer(c).reverse().toString() + sbc.toString().substring(sbc.length() - 2, sbc.length());
        }
        if (BuildConfig.DEBUG) Log.d("AppSignUtil", "des:" + des);
        return des;
    }


    /**
     * 先将加密的信息拼接
     *
     * @param username
     * @param password
     * @return
     */
    public static String getMosaicStr(String username, String password) {
//        myID:5603f5df326d2abe9fe7787554f4f9ba
//        拼接顺序：username,password,uuid,myid
//        然后base64加密
        String myid = "74bad43ba5bc1d9946e281f47dfce851";

        StringBuilder sb = new StringBuilder();
        sb.append(username);
        sb.append(",");
        sb.append(password);
        sb.append(",");
//        sb.append(RxDeviceTool.getAndroidId(XFBaseApplication.getAppContext()));
        sb.append(",");
        sb.append(myid);
        sb.append(",");
        sb.append(System.currentTimeMillis());// 时间戳
        return sb.toString();

    }

    //加密
    public static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    public static String randomChar() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(str.charAt(new Random().nextInt(26)));
        }
        return sb.toString();
    }


    public static String loginBase64Str(String str) {
        str = str + ",5603f5df326d2abe9fe7787554f4f9ba";
        Log.e("loginBase64Str", "change before: " + str);
        try {
            str = new String(Base64.encode(str.getBytes("utf-8"), Base64.NO_WRAP), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        str.replace(" ", "");
        str = str.substring(0, str.length() - 2) + "VMVWAT" + str.substring(str.length() - 2, str.length());
        boolean isou = str.length() % 2 == 0;
        Log.e("loginBase64Str", "before: " + str);
        if (isou) {
            String t = str.substring(0, str.length() - 1);
            String te = str.substring(str.length() - 1, str.length());
            StringBuffer stringBuffer = new StringBuffer(t);
            str = stringBuffer.reverse().toString() + te;
        } else {
            String tt = str.substring(0, str.length() - 2);
            StringBuffer stringBuffer = new StringBuffer();
            str = stringBuffer.reverse() + str.substring(str.length() - 2, str.length());
        }
        Log.e("loginBase64Str", "after: " + str);
        return str;
    }


    /**
     * 字符Asscii 排序
     *
     * @param str
     * @param isDesc 是否倒序, true 倒序, false 升序
     * @return
     */
    public String getAccsiiSort(String str, boolean isDesc) {
        if (str != null && !"".equals(str)) {
            char[] cArry = str.toCharArray();
            int arr[] = new int[cArry.length];
            int i = 0;
            for (char c : cArry) {
                arr[i] = c;
                i++;
            }
            StringBuilder sb = new StringBuilder();
            Arrays.sort(arr);
            if (isDesc) {
                for (int k = (arr.length - 1); k >= 0; k--) {
                    sb.append((char) arr[k]);
                }
            } else {
                for (int k = 0; k < arr.length; k++) {
                    sb.append((char) arr[k]);
                }
            }
            return sb.toString();
        }
        return str;
    }


}
