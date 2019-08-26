package com.qymage.sys.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.qymage.sys.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 图片异步加载工具类
 *
 * @return
 */
public class PicasooUtil {
    /**
     * 第三方实现图片异步加载
     *
     * @param context
     * @param url
     * @param placeholderDrawable
     * @param target
     */
    private static PicasooUtil instance = null;

    private static final String W80_H80 = "80*80";
    private static final String W200_100 = "200*100";
    private static final String W400_H200 = "400*200";

    private PicasooUtil() {
    }

    public static synchronized PicasooUtil getInstance() {
        if (instance == null) {
            instance = new PicasooUtil();
        }
        return instance;
    }

    public static void setImageResource(String url, int placeholderDrawable, ImageView target, int cornerRadiusPixels) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .bitmapConfig(Config.RGB_565)
                .showImageOnLoading(placeholderDrawable) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(placeholderDrawable) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(placeholderDrawable) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)) // 设置成圆角图片
                .build(); // 构建完成
        ImageLoader
                .getInstance()
                .displayImage(url, target, options);

    }

    public static void setImageResource(String url, ImageView target, int cornerRadiusPixels) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .bitmapConfig(Config.RGB_565)
                .showImageOnLoading(R.mipmap.icon_default_image)
                .showImageForEmptyUri(R.mipmap.icon_default_image)
                .showImageOnFail(R.mipmap.icon_default_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)) // 设置成圆角图片
                //.imageScaleType(ImageScaleType.EXACTLY)
                .build();
        ImageLoader.getInstance().displayImage(url, target, options);
    }

    /**
     * 从内存卡中异步加载本地图片
     *
     * @param uri
     * @param imageView
     */
    public static void displayFromSDCard(String uri, ImageView imageView, int cornerRadiusPixels) {
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .bitmapConfig(Config.RGB_565)
                .showImageOnLoading(R.mipmap.icon_default_image)
                .showImageForEmptyUri(R.mipmap.icon_default_image)
                .showImageOnFail(R.mipmap.icon_default_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)) // 设置成圆角图片
                //.imageScaleType(ImageScaleType.EXACTLY)
                .build();
        ImageLoader.getInstance().displayImage("file://" + uri, imageView, options);

    }


    public static void setImageResource(String url, ImageView target) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .bitmapConfig(Config.RGB_565)
                .showImageOnLoading(R.mipmap.icon_default_image)
                .showImageForEmptyUri(R.mipmap.icon_default_image)
                .showImageOnFail(R.mipmap.icon_default_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(0)) // 设置成圆角图片
                //.imageScaleType(ImageScaleType.EXACTLY)
                .build();
        ImageLoader.getInstance().displayImage(url, target, options);
    }


    public static void setNoImageResource(String url, ImageView target, int cornerRadiusPixels) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .bitmapConfig(Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)) // 设置成圆角图片
                //.imageScaleType(ImageScaleType.EXACTLY)
                .build();
        ImageLoader.getInstance().displayImage(url, target, options);
    }


    /**
     * 设置网络图片
     *
     * @param context     上下文
     * @param url         图片路径
     * @param placeholder 占位图
     * @param imageView   imageView图片控件
     */
    public static void setImageUrl(Context context, String url, int placeholder, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .dontAnimate()//防止设置placeholder导致第一次不显示网络图片,只显示默认图片的问题
                .error(placeholder)
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 设置本地图片
     *
     * @param path      图片路径
     * @param imageView imageView
     */
    public static void setImageResource(String path, int placeholder, ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(placeholder)
                .showImageForEmptyUri(placeholder)
                .showImageOnLoading(placeholder).build();
        ImageLoader.getInstance().displayImage("file:/" + path, imageView, options);
    }

    // 将网络图片url转换成 Bitmap
    public static Bitmap bitmap;

    public static Bitmap returnBitMap(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return bitmap;
    }
}
