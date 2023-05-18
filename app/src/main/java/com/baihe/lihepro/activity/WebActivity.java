package com.baihe.lihepro.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.ToastUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.Http;
import com.baihe.http.cookie.CookieStore;
import com.baihe.lihepro.BuildConfig;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.ShareDialog;
import com.baihe.lihepro.entity.H5ParamEntity;
import com.blankj.utilcode.util.GsonUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.umeng.socialize.UMShareAPI;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

import okhttp3.Cookie;


public class WebActivity extends BaseActivity {
    public static final String WEB_URL = "web_url";
    public static final String WEB_TITLE = "web_title";
    private BridgeWebView webview;
    private String mainPageUrl;
    private TextView title_text_tv;
    private Toolbar title_tb;
    private String title;

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WEB_URL, url);
        context.startActivity(intent);
    }

    public static void start(Context context,String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(WEB_URL, url);
        intent.putExtra(WEB_TITLE, title);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("");
        setContentView(R.layout.activity_webview);
        title_text_tv = findViewById(com.baihe.common.R.id.title_text_tv);
        title_tb = findViewById(com.baihe.common.R.id.title_tb);
        initData();
        initView();
        initUrl();
        statusLayout.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
            @Override
            public void onNetErrorClick() {
                webview.reload();
            }

            @Override
            public void onNetFailClick() {
                webview.reload();
            }

            @Override
            public void onExpandClick() {

            }
        });
        title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = webview.getUrl();
                if (UrlConstant.ANLIKU_H5.equals(url)){
                    finish();
                    return;
                }

                if (webview != null && webview.canGoBack()) {
                    webview.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initData() {
          mainPageUrl = getIntent().getStringExtra(WEB_URL);
          title = getIntent().getStringExtra(WEB_TITLE);
        if (TextUtils.isEmpty(mainPageUrl)) {
            return;
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        if (!TextUtils.isEmpty(title))
            title_text_tv.setText(title);
        webview = findViewById(R.id.webview);
        webview.requestFocus();
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//根据cache-control决定是否从网络上取数据。
        if (Build.VERSION.SDK_INT >= 21) {
            webview.getSettings().setMixedContentMode(0);
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webview.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);// 设置支持缩放
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBlockNetworkImage(false);//页面加载完成之前先阻止图片下载  待onPageFinished后再打开图片下载
        webview.getSettings().setDefaultTextEncodingName("UTF-8");
        webview.getSettings().setNeedInitialFocus(true);//当webview调用requestFocus时为webview设置节点
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        webview.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        webview.setScrollbarFadingEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.getSettings().setSaveFormData(false);
        webview.getSettings().setSavePassword(false);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//设置，可能的话使所有列的宽度不超过屏幕宽度
        webview.getSettings().setUseWideViewPort(true);//设置webview自适应屏幕大小
        webview.getSettings().setLoadWithOverviewMode(true);//设置webview自适应屏幕大小
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        String dir = this.getApplicationContext().getDir("database", MODE_PRIVATE).getPath();
        // 启用地理定位
        webview.getSettings().setGeolocationEnabled(true);
        // 设置定位的数据库路径
        webview.getSettings().setGeolocationDatabasePath(dir);
        // 最重要的方法，一定要设置，这就是出不来的主要原因
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webview.getSettings().setAppCachePath(appCachePath);

        // 支持缩放
        webview.setInitialScale(100);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setScrollbarFadingEnabled(true);
        // 设置H5的缓存打开,默认关闭
        webview.getSettings().setAppCacheEnabled(true);
        // 启用地理定位
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebViewClient = new BridgeWebViewClient(webview) {
            boolean isSuccess;

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isSuccess) {

                    statusLayout.normalStatus();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                statusLayout.loadingStatus();
                isSuccess = true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                statusLayout.netErrorStatus();
                isSuccess = false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view,url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受所有证书
            }
        };
        webview.setWebViewClient(mWebViewClient);
        webview.setWebChromeClient(mWebChromeClient);
        webview.setDownloadListener(new MyWebViewDownLoadListener());
        webview.setDefaultHandler(new DefaultHandler());
        webview.registerHandler("showShare", new BridgeHandler() {
            @Override
            public void handler(final String data, CallBackFunction function) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final H5ParamEntity entity = GsonUtils.getGson().fromJson(data,H5ParamEntity.class);
                        AndPermission.with(WebActivity.this)
                                .runtime()
                                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                                .onGranted(new Action<List<String>>() {
                                    @Override
                                    public void onAction(List<String> data) {
                                        new ShareDialog.Builder(WebActivity.this)
                                                .setTitle(entity.getTitle())
                                                .setDescription(entity.getDescription())
                                                .setThumb(entity.getThumb())
                                                .setShareUrl(entity.getShareUrl())
                                                .build().show();
                                    }
                                })
                                .onDenied(new Action<List<String>>() {
                                    @Override
                                    public void onAction(List<String> data) {
                                        ToastUtils.toast("请打开读取权限");
                                    }
                                })
                                .start();




                    }
                });
            }
        });

        webview.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);

    }

    private void initUrl() {
        webview.loadUrl(mainPageUrl);
        synCookies(this, mainPageUrl);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // 改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String url = webview.getUrl();
            if (UrlConstant.ANLIKU_H5.equals(url)){
                finish();
                return super.onKeyDown(keyCode, event);
            }
            if (webview != null && webview.canGoBack()) {
                // 执行上一步跳转逻辑
                webview.goBack();
                return true;
            } else {
//                destroyWebView();
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public boolean onJsTimeout() {
            return super.onJsTimeout();
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title)&&!title.contains("http:")&&!title.contains("https"))
                title_text_tv.setText(title);
        }
    };
    private WebViewClient mWebViewClient = null;

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        }
    }

    public static void synCookies(Context context, String url) {
        CookieStore cookieStore = Http.getInstance().getCookieStore();
        if (cookieStore == null) {
            return;
        }
        List<Cookie> cookies = Http.getInstance().getCookieStore().getAllCookies();
        if (cookies != null && cookies.size() > 0) {
            Cookie cookie = cookies.get(0);
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setCookie(url, cookie.toString());
            CookieSyncManager.getInstance().sync();
        }
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
