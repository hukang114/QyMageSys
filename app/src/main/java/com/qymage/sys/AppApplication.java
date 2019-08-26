package com.qymage.sys;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;


import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.qymage.sys.common.allpay.wxpay.Constants;
import com.qymage.sys.common.allpay.wxpay.WXPayUtil;
import com.qymage.sys.common.http.HttpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.logging.Level;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by HK on 2018/4/24.
 */

public class AppApplication extends Application {

    public static AppApplication sInstance;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.white, android.R.color.black);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        // 极光推送初始化
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
        initWeiXin();
        //初始化Http
        HttpUtil.init();
        //初始化ShareSdk
        MobSDK.init(this);
        intiBaiDu();
        initImageLoader();
    }

    private void intiBaiDu() {
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    private void initWeiXin() {
        //==========================================================================================
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(getApplicationContext(), Constants.APP_ID, false);
        // 将该app注册到微信
        msgApi.registerApp(Constants.APP_ID);
        WXPayUtil.init(getApplicationContext(), Constants.APP_ID);

    }

    /**
     * 初始化 ImageLoader
     */
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                // max width, max height，即保存的每个缓存文件的最大长宽
                .memoryCacheExtraOptions(480, 800)
                // 线程池内加载的数量
                .threadPoolSize(4)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                // 你可以通过自己的内存缓存实现
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                // 将保存的时候的URI名称用MD5 加密
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheSize(50 * 1024 * 1024)
                // 缓存的文件数量
                .discCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                // 自定义缓存路径
                //.discCache(new UnlimitedDiscCache(getApplicationContext().getCacheDir()))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                // connectTimeout (5 s), readTimeout (30 s)超时时间
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 30 * 1000, 60 * 1000))
                // Remove for release app
                .writeDebugLogs()
                // 开始构建
                .build();
        // 全局初始化此配置
        ImageLoader.getInstance().init(config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
}
