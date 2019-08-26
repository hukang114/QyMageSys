package com.qymage.sys.common.config;



public interface Constants {

    //http://101.37.157.35 测试用
    //http://www.gzmchc.com // 正式用
    String base_url = "http://101.37.157.35/gxfw/"; //8080   8090
    String imageurl = "http://101.37.157.35/";

    public interface method {

        final String kuaidi = "https://m.kuaidi100.com/index_all.html?";
        // 商城消费回调
        final String notify_url = "http://www.gzmchc.com/notify/order_app_alipay_notify.php";

        final String zxkh = "http://27.221.14.250:16008/uic-front/front/index.jsp";

    }


    public interface status {
        final int success = 200;

        final int empty = 300;

        final String failnet = "网络链接失败";

        final String URL = "url";

        final String CONTENT = "content";

    }

    public interface baidu {

        final long severId = 143816;
    }

    public static final int TASK_ITEM_CODE = 0X115;
    public static final int TASK_ITEM_CODE_2 = 0X116;
    public static final int REQUEST_CODE_SKILL = 0X119;
    public static final int RESULT_CODE_SKILL = 0X120;

    public RequestMode mode = RequestMode.FRIST;

    public enum RequestMode {
        FRIST, LOAD_MORE, REFRESH, SEARCH, DEFAULT, CHILD, ISTIME, YMXJ;
    }

    public static final String no_data = "暂无数据显示";

    public static final String loding_data = "拼命加载中...";

    public static final String token = "token";
    public static final String openid = "openid";
    public static final String userid = "userid";

    public static final String login_mode = "login_mode";
    // 用户设置手势密码
    public static final String gesture_pwd = "gesture_pwd";
    // 是否记住登录密码
    public static final String REM_PSD = "rem_psd";
    //保存登录用户名
    public static final String USER_NAME = "username";


    // 二维码分享地址
    public static final String qrcode = imageurl + "qrcode/share.jpg";

    public static final String baiduapi_weather = "http://api.map.baidu.com/telematics/v3/weather?";

    public static final String mcode = "7D:53:98:FA:C5:F2:4D:F5:1D:2D:BA:21:18:36:03:B3:EB:28:EF:5B;com.ee.ryycsjd";

    // 平台接口================================


}
