package com.qymage.sys.common.http;

import com.lzy.okgo.request.PostRequest;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;

import java.util.Map;

/**
 * Created by admin on 2019/8/23.
 */

public class HttpUtil {

    private static final String SALT = "76576076c1f5f657b634e966c8836a06";
    private static final String DEVICE = "android";
    private static final String VIDEO_SALT = "#2hgfk85cm23mk58vncsark";


    /**
     * 初始化
     */
    public static void init() {
        HttpClient.getInstance().init();
    }

    /**
     * 取消网络请求
     */
    public static void cancel(String tag) {
        HttpClient.getInstance().cancel(tag);
    }


    public static PostRequest getAdv(String name, Map<String, String> params) {
        return HttpClient.getInstance().postData(name, params);
    }


    /**
     * 用户登录
     *
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest UserLogin(String urlname, Map<String, String> params) {
        return HttpClient.getInstance().postData(urlname, params);
    }


    /**
     * 用户修改密码
     *
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest updatePwd(String urlname, Map<String, String> params) {
        return HttpClient.getInstance().postData(urlname, params);
    }

    /**
     * 用户退出登录
     *
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest logout(String urlname, Map<String, String> params) {
        return HttpClient.getInstance().postData(urlname, params);
    }


}


