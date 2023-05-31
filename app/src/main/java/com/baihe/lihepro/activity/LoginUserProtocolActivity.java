package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.baihe.common.base.BaseActivity;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;


/**
 * Created by yanjl. on 16/11/28.
 * <p>
 * 用戶协议页面
 */
public class LoginUserProtocolActivity extends BaseActivity {
    public static final String URL = "url";
    public static final String IS_CHECKED = "is_checked";
    private Toolbar toolbar;
    private LinearLayout ll_return;
    private TextView tv_close;
    private FrameLayout root_view;
    private WebView webview;
    private String mainPageUrl;
    private TextView tv_title;
    private CheckBox ck_protocol;
    private boolean isProtocolChecked;

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginUserProtocolActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_protocol_webview);
        statusLayout.loadingStatus();
        initData();
        initView();
        initToolbar();
        initUrl();
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
        mainPageUrl = getIntent().getStringExtra(URL);
        if (TextUtils.isEmpty(mainPageUrl)) {
            mainPageUrl = UrlConstant.AGREEMENT_LIHE;
        }
        isProtocolChecked = getIntent().getBooleanExtra(IS_CHECKED, true);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_navigation);
        ll_return = toolbar.findViewById(R.id.ll_return);
        tv_close = toolbar.findViewById(R.id.tv_close);
        TextView title = toolbar.findViewById(R.id.tv_title);
        title.setText("用户协议和隐私政策");
        title.setTextColor(Color.BLACK);
        toolbar.getMenu().clear();
        // 左上角返回按钮
        ll_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webview != null && webview.canGoBack()) {
                    // 执行上一步跳转逻辑
                    webview.goBack();
                    tv_close.setVisibility(View.VISIBLE);
                } else {
                    tv_close.setVisibility(View.INVISIBLE);
//                    destroyWebView();
                    finish();
                }
            }
        });

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_title = toolbar.findViewById(R.id.tv_title);

    }


    private void initView() {
        root_view = findViewById(R.id.root_view);
        webview = findViewById(R.id.webview);
        ck_protocol = findViewById(R.id.ck_protocol);


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
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();

        // 支持缩放
        webview.setInitialScale(100);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setScrollbarFadingEnabled(true);
        // 设置H5的缓存打开,默认关闭
        // 启用地理定位
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webview.setWebViewClient(mWebViewClient);
        webview.setWebChromeClient(mWebChromeClient);
        webview.setDownloadListener(new MyWebViewDownLoadListener());

        if (isProtocolChecked) {
            ck_protocol.setChecked(true);
        } else {
            ck_protocol.setChecked(false);
        }

        ck_protocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isProtocolChecked = isChecked;
                Intent intent = new Intent();
                intent.putExtra(IS_CHECKED, isProtocolChecked);
                setResult(RESULT_OK, intent);
            }
        });

    }

    private void initUrl() {
        webview.loadUrl(mainPageUrl);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // 改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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
            if (newProgress == 100) {
                statusLayout.normalStatus();
            }
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };
    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (webview != null && webview.canGoBack()) {
                tv_close.setVisibility(View.VISIBLE);
            } else {
                tv_close.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // 接受所有证书
        }
    };

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        }
    }

}
