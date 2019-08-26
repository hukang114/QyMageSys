package com.qymage.sys.common.tools;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qymage.sys.R;


/**
 * Created by sunfusheng on 16/4/6.
 */
public class ImageManager {

    public static Context mContext;
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    public ImageManager(Context context) {
        this.mContext = context;
    }

    // 将资源ID转为Uri
    public static Uri resourceIdToUri(int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + mContext.getPackageName() + FOREWARD_SLASH + resourceId);
    }
    // 加载网络图片
    public static void loadUrlImage(String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)
                .placeholder(R.mipmap.icon_default_image)
                .error(R.mipmap.icon_default_image)
//                .crossFade()
                .centerCrop()
                .into(imageView);
            //
//        UILUtils.displayImage(mContext.getApplicationContext(), url, imageView);

    }
    // 加载网络图片
    public static void loadUrlImage_no(String url, ImageView imageView) {
        Glide.with(mContext.getApplicationContext())
                .load(url)
                .error(R.mipmap.icon_default_image)
//                .crossFade()
                .into(imageView);

//        UILUtils.displayImage(mContext, url, imageView);

    }


    // 加载drawable图片
    public static void loadResImage(int resId, ImageView imageView) {
        Glide.with(mContext)
                .load(resourceIdToUri(resId))
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .crossFade()
                .into(imageView);
    }

    // 加载drawable图片
    public static void loadResImage1(int resId, ImageView imageView) {
        Glide.with(mContext.getApplicationContext())
                .load(resourceIdToUri(resId))
                .into(imageView);
    }

    // 加载本地图片
    public void loadLocalImage(Context mContext,String path, ImageView imageView) {
        Glide.with(mContext)
                .load("file://" + path)
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .crossFade()
                .into(imageView);
    }

    // 加载网络圆型图片
    public static void loadCircleImage(Context mContext,String url, ImageView imageView) {
        Glide.with(mContext)
                .load(url)
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                .crossFade()
                .into(imageView);
    }

    // 加载drawable圆型图片
    public void loadCircleResImage(int resId, ImageView imageView) {
//        Glide.with(mContext)
//                .load(resourceIdToUri(resId))
//                .placeholder(R.color.font_black_6)
//                .error(R.color.font_black_6)
//                .crossFade()
//                .transform(new GlideCircleTransform(mContext))
//                .into(imageView);
    }

    // 加载本地圆型图片
    public void loadCircleLocalImage(String path, ImageView imageView) {
//        Glide.with(mContext)
//                .load("file://" + path)
//                .placeholder(R.color.font_black_6)
//                .error(R.color.font_black_6)
//                .crossFade()
//                .transform(new GlideCircleTransform(mContext))
//                .into(imageView);
    }

}
