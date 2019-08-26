package com.qymage.sys.common.updateapp;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.qymage.sys.R;
import com.qymage.sys.common.util.ConverUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * 版本下载更新类 弹框在线更新样式
 *
 * @author XX
 */
public class UpdateManager {

    private static final String[] mPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private Context mContext;
    //返回的安装包url
    private String apkUrl;
    //从那个页面传来更新通知
    private String msg_cont;
    private String version_name;
    private TextView mContentTextView;
    private Button mUpdateOkButton;
    private NumberProgressBar mNumberProgressBar;
    private ImageView mIvClose;
    private TextView mTitleTextView;
    private LinearLayout mLlClose;
    private ImageView mTopIv;
    private TextView mIgnore;

    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/hzkj/";

    private static final String saveFileName = savePath + "huoma.apk";
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int GET_DOWN = 3;
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    private int count = 0;
    private static final String GUIDE_PREFs = "guide_prefs";// 缓存文件

    OnBtnClickL mClickL;
    String size = "";//MB
    DecimalFormat fnum = new DecimalFormat("#0.00");

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mNumberProgressBar.setProgress(progress);
                    mNumberProgressBar.setMax(100);
                    break;
                case DOWN_OVER:
                    colseDialog();
                    installApk();
                    break;
                case GET_DOWN:
                    String msgcon = "";
                    if (!TextUtils.isEmpty(size)) {
                        msgcon = "新版本大小：" + size + "\n\n";
                    }
                    if (!TextUtils.isEmpty(msg_cont)) {
                        msgcon += msg_cont;
                    }
                    mContentTextView.setText(msgcon);
                    break;
                default:
                    break;
            }
        }

    };

    public UpdateManager(Context context) {
        this.mContext = context;

    }


    //外部接口让主Activity调用

    /**
     * @param
     * @param msg    更新条件
     * @param apkUrl 更新包的下载 地址
     */
    public void checkUpdateInfo(String msg, String version_name, String apkUrl, OnBtnClickL btnClickL) {
        this.apkUrl = apkUrl;
        this.msg_cont = msg;
        this.version_name = version_name;
        this.mClickL = btnClickL;
        downLoadThread = new Thread(mdownApkRunnable);
        showNoticeDialog();
    }

    /**
     * 版本跟新提示框
     *
     * @param
     */
    private void showNoticeDialog() {
        downloadDialog = new Dialog(mContext, R.style.UpdateAppDialog);
        View view = View.inflate(mContext, R.layout.lib_update_app_dialog, null);
        downloadDialog.setContentView(view);
        downloadDialog.setCancelable(false);
        initView(view);
        getFileSize(apkUrl);// 读取安装包的大小及显示
        setDialogTheme(ContextCompat.getColor(mContext, R.color.red), R.mipmap.lib_update_app_top_bg);
        mLlClose.setVisibility(View.GONE);
        mTitleTextView.setText("是否升级到" + version_name + "版本？");
        mUpdateOkButton.setOnClickListener(v -> {
//            mNumberProgressBar.setVisibility(View.VISIBLE);
//            mUpdateOkButton.setVisibility(View.GONE);
            //安装包下载
            downloadApk();
        });
        downloadDialog.show();
    }

    private void initView(View view) {
        //提示内容
        mContentTextView = view.findViewById(R.id.tv_update_info);
        //标题
        mTitleTextView = view.findViewById(R.id.tv_title);
        //更新按钮
        mUpdateOkButton = view.findViewById(R.id.btn_ok);
        //进度条
        mNumberProgressBar = view.findViewById(R.id.npb);
        //关闭按钮
        mIvClose = view.findViewById(R.id.iv_close);
        //关闭按钮+线 的整个布局
        mLlClose = view.findViewById(R.id.ll_close);
        //顶部图片
        mTopIv = view.findViewById(R.id.iv_top);
        //忽略
        mIgnore = view.findViewById(R.id.tv_ignore);

        mIvClose.setOnClickListener(v -> {
            mClickL.onBtnLeftClick();
        });
    }

    /**
     * 设置
     *
     * @param color    主色
     * @param topResId 图片
     */
    private void setDialogTheme(int color, int topResId) {
        mTopIv.setImageResource(topResId);
        mUpdateOkButton.setBackgroundDrawable(DrawableUtil.getDrawable(ConverUtil.dp2px(mContext, 4), color));
        mNumberProgressBar.setProgressTextColor(color);
        mNumberProgressBar.setReachedBarColor(color);
        //随背景颜色变化
        mUpdateOkButton.setTextColor(ColorUtil.isTextColorDark(color) ? Color.BLACK : Color.WHITE);
    }


    public interface OnBtnClickL {
        void onBtnLeftClick();
    }

    /**
     * 读取安装包的大小
     *
     * @param urlString
     * @return
     */
    public void getFileSize(String urlString) {
        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                // 获取文件大小
                double length = conn.getContentLength();
                size = fnum.format(length / 1048576) + "MB";
                mHandler.sendEmptyMessage(GET_DOWN);
            } catch (MalformedURLException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];
                double filesize = conn.getContentLength();
                //format output
//                size = fnum.format(filesize / 1048576);
                Log.d("UpdateManager", size + "MB");
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        //下载完成通知安装   
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);//点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     *
     * @param
     */

    private void downloadApk() {
        PermissionUtils.init(mContext);
        boolean granted = PermissionUtils.isGranted(mPermission);
        if (granted) {
            mNumberProgressBar.setVisibility(View.VISIBLE);
            mUpdateOkButton.setVisibility(View.GONE);
            downLoadThread.start();
        } else {
            PermissionUtils permission = PermissionUtils.permission(mPermission);
            permission.callback(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    downLoadThread.start();
                }

                @Override
                public void onDenied() {
                    PermissionUtils.openAppSettings();
                    Toast.makeText(mContext, "请允许权限读写权限", Toast.LENGTH_SHORT).show();
                }
            });
            permission.request();
        }


    }

    public void colseDialog() {
        // 关闭更新提示框
        if (downloadDialog != null && downloadDialog.isShowing()) {
            downloadDialog.dismiss();
            downloadDialog = null;
        }
    }

    /**
     * 安装apk
     *
     * @param
     */
    private void installApk() {

        File apkfile = new File(saveFileName);
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.huoma.app.fileprovider", apkfile);//在AndroidManifest中的android:authorities值
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            mContext.startActivity(install);
        } else {
            if (!apkfile.exists()) {
                return;
            }
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }


    }
}  
