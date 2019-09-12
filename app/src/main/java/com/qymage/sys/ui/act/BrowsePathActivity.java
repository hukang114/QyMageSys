package com.qymage.sys.ui.act;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.util.VerifyUtils;
import com.qymage.sys.databinding.ActivityBrowsePathBinding;
import com.tencent.mm.opensdk.utils.Log;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class BrowsePathActivity extends BBActivity<ActivityBrowsePathBinding> {


    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;

    private String url = "";
    private String type = "";
    private String title;
    private Intent mIntent;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_browse_path;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        url = mIntent.getStringExtra("path");
        try {
            type = mIntent.getStringExtra("content");
            title = mIntent.getStringExtra("title");
        } catch (Exception e) {
        }

        if (VerifyUtils.isEmpty(url)) {
            mBinding.emptyLayout.showEmpty(R.mipmap.icon_default_image, "无法跳转页面");
            return;
        }
        initWebView();
    }

    private void initWebView() {
        mBinding.wvWebView.setWebViewClient(webViewClient);
        mBinding.wvWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int newProgress) {
                if (newProgress == 100) {
                    mBinding.progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    mBinding.progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mBinding.progressBar.setProgress(newProgress);//设置进度值
                }
                super.onProgressChanged(webView, newProgress);
            }


            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                Log.i("test", "openFileChooser 1");
                BrowsePathActivity.this.uploadFile = uploadFile;
                openFileChooseProcess();
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
                Log.i("test", "openFileChooser 2");
                BrowsePathActivity.this.uploadFile = uploadFile;
                openFileChooseProcess();
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.i("test", "openFileChooser 3");
                BrowsePathActivity.this.uploadFile = uploadFile;
                openFileChooseProcess();
            }

            // For Android  >= 5.0
            public boolean onShowFileChooser(com.tencent.smtt.sdk.WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                Log.i("test", "openFileChooser 4:" + filePathCallback.toString());
                BrowsePathActivity.this.uploadFiles = filePathCallback;
                openFileChooseProcess();
                return true;
            }

        });


        if (type != null && !type.equals("")) {
            String data = "<html><head><style>img{width:100% !important;}</style></head><body style='margin:0;padding:0'>" + type + "</body></html>";
            mBinding.wvWebView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
            if (title != null) {
                mBinding.metitle.setcTxt(title);
            }
        } else {
            if (title != null) {
                mBinding.metitle.setcTxt(title);
            }
            mBinding.wvWebView.loadUrl(url);
        }
    }

    @Override
    protected void initData() {
        super.initData();

    }

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView wv, String url) {
            if (url == null) return false;
            try {
                if (url.startsWith("weixin://") // 微信
                        || url.startsWith("alipays://") // 支付宝
                        || url.startsWith("mailto://") // 邮件
                        || url.startsWith("tel://")// 电话
                        || url.startsWith("dianping://")// 大众点评
                        || url.startsWith("pinduoduo://")// 拼多多
                        || url.startsWith("taobao://")// 淘宝
                        || url.startsWith("tmall://")// 天猫
                        || url.startsWith("vipshop://")// 唯品会
                ) {// 其他自定义的scheme
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            } catch (Exception e) { // 防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                return true;// 没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
            }
            // 处理http和https开头的url
            wv.loadUrl(url);
            return true;
        }
    };


    private void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "test"), 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (null != uploadFile) {
                        Uri result = data == null || resultCode != RESULT_OK ? null
                                : data.getData();
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    if (null != uploadFiles) {
                        Uri result = data == null || resultCode != RESULT_OK ? null
                                : data.getData();
                        uploadFiles.onReceiveValue(new Uri[]{result});
                        uploadFiles = null;
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != uploadFile) {
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }
            if (null != uploadFiles) {
                uploadFiles.onReceiveValue(null);
                uploadFiles = null;
            }

        }
    }

    /**
     * 确保注销配置能够被释放
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (this.mBinding.wvWebView != null) {
            mBinding.wvWebView.destroy();
        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mBinding.wvWebView.canGoBack()) {
            mBinding.wvWebView.goBack();
            return;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mBinding.wvWebView.canGoBack()) {
            mBinding.wvWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
