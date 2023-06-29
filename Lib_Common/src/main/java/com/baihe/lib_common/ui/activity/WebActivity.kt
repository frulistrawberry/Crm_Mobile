package com.baihe.lib_common.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.CommonActivityWebBinding
import com.baihe.lib_framework.base.BaseViewBindActivity
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

class WebActivity: BaseViewBindActivity<CommonActivityWebBinding>() {
    val title: String by lazy {
        intent.getStringExtra("title")
    }
    val url:String by lazy {
        intent.getStringExtra("url")
    }

    /**
     * 辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等
     */
    private val mWebChromeClient = object : WebChromeClient() {
        /**
         * 网页Title更改
         */
        override fun onReceivedTitle(webView: WebView?, title: String?) {
            super.onReceivedTitle(webView, title)
            if (webView != null && !webView.canGoBack() && this@WebActivity.title.isNotEmpty()) {
                updateToolbar {
                    this.title = this@WebActivity.title
                }
            } else {
                var titleResult = ""
                if (!title.isNullOrEmpty() && !title.startsWith("http", true)) {
                    titleResult = title
                }
                updateToolbar {
                    this.title = titleResult
                }
            }
        }

        /**
         * 文件选择回调
         */
        override fun onShowFileChooser(
            webView: WebView?,
            back: ValueCallback<Array<Uri>>?,
            chooser: FileChooserParams?
        ): Boolean {
            return super.onShowFileChooser(webView, back, chooser)
        }

        /**
         * 获得网页的加载进度并显示
         */
        override fun onProgressChanged(webView: WebView?, process: Int) {
            super.onProgressChanged(webView, process)
            if (process == 100) {
                this@WebActivity.showContentView()
            }
        }
    }

    /**
     * 处理各种通知 & 请求事件
     */
    private val mWebViewClient = object : WebViewClient() {
        /**
         * 网页加载完毕
         */
        override fun onPageFinished(webView: WebView?, url: String?) {
            super.onPageFinished(webView, url)
        }

        /**
         * 跳转拦截处理
         * 打开网页时，不调用系统浏览器进行打开，而是在本WebView中直接显示
         */
        override fun shouldOverrideUrlLoading(webview: WebView?, url: String?): Boolean {
            return super.shouldOverrideUrlLoading(webview, url)
        }

        /**
         * 设置错误页面
         */
        override fun onReceivedError(webview: WebView?, p1: Int, p2: String?, p3: String?) {
            super.onReceivedError(webview, p1, p2, p3)
        }

        override fun onReceivedSslError(
            webView: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            super.onReceivedSslError(webView, handler, error)
            //忽略ssl错误
            handler?.proceed()
            this@WebActivity.showErrorView()
        }
    }

    companion object{
        @JvmStatic
        fun start(context: Context,title:String,url:String){
            context.startActivity(Intent(context,WebActivity::class.java).also {
                it.putExtra("title",title)
                it.putExtra("url",url)
            })
        }
    }

    override fun onReload() {
        super.onReload()
        mBinding.webView.reload()
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            setTitle(this@WebActivity.title)
            navIcon(R.mipmap.navigation_icon){
                if (mBinding.webView.canGoBack()){
                    mBinding.webView.goBack()
                }else{
                    finish()
                }

            }
        }
        mBinding.webView.webChromeClient = mWebChromeClient
        mBinding.webView.webViewClient = mWebViewClient
        showLoadingView()
        mBinding.webView.loadUrl(url)

    }
}