package com.baihe.lib_common

import android.app.Application
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.http.EasyConfig
import com.baihe.http.ssl.HttpSslFactory
import com.baihe.imageloader.ImageLoaderConfig
import com.baihe.imageloader.ImageLoaderUtils
import com.baihe.lib_common.http.RequestHandler
import com.baihe.lib_common.http.cookie.CookieJarImpl
import com.baihe.lib_common.http.log.server.ApiServer
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.ui.widget.refresh.RefreshHeaderLayout
import com.baihe.lib_common.ui.widget.state.EmptyViewDelegate
import com.baihe.lib_common.ui.widget.state.ErrorViewDelegate
import com.baihe.lib_common.ui.widget.state.LoadingViewDelegate
import com.baihe.lib_common.ui.widget.state.ToolbarViewDelegate
import com.baihe.lib_framework.helper.AppHelper
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_framework.manager.AppManager
import com.baihe.lib_framework.storage.StorageManager
import com.baihe.lib_framework.toast.TipsToast
import com.baihe.lib_framework.utils.DeviceInfoUtils
import com.dylanc.loadingstateview.LoadingStateView
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.android.tpush.XGIOperateCallback
import com.tencent.android.tpush.XGPushConfig
import com.tencent.android.tpush.XGPushManager
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


object CommonConfig {

    @JvmStatic
    fun init(context: Application,isDebug:Boolean){
        AppHelper.init(context,isDebug)
        AppManager.init(context)
        StorageManager.init(context,isDebug=isDebug)
        DeviceInfoUtils.init(context)
        TipsToast.init(context)
        LogUtil.init(isDebug)
        if (isDebug){
            ARouter.openLog() // 打印日志
            ARouter.openDebug()
        }
        ARouter.init(context)

        initHttp(isDebug)
        initImageLoader()
        initCommonUi()
        initPush()


    }

    private fun initPush() {
        XGPushConfig.enableDebug(AppHelper.getApplication(), BuildConfig.DEBUG)
        XGPushConfig.setOppoPushAppId(AppHelper.getApplication(), "ea9afa6fc4a0466cbc96a8a1ed2640d6")
        XGPushConfig.setOppoPushAppKey(AppHelper.getApplication(), "b3c4ff4ac4734b419620764ed584fd18")

        XGPushConfig.setMiPushAppId(AppHelper.getApplication(), "2882303761518222692")
        XGPushConfig.setMiPushAppKey(AppHelper.getApplication(), "5641822277692")

        XGPushConfig.enableOtherPush(AppHelper.getApplication(), true)
        XGPushManager.registerPush(AppHelper.getApplication(), object : XGIOperateCallback {
            override fun onSuccess(data: Any, flag: Int) {
                //token在设备卸载重装的时候有可能会变
                LogUtil.d("LihePush", "注册成功，设备token为：$data")
                //更新http拦截器里的device_token
                EasyConfig.getInstance()
                    .addParam("device_token", XGPushConfig.getToken(AppHelper.getApplication()))
            }

            override fun onFail(data: Any, errCode: Int, msg: String) {
                LogUtil.d("LihePush", "注册失败，错误码：$errCode,错误信息：$msg")
            }
        })
    }

    private fun initCommonUi() {
        LoadingStateView.setViewDelegatePool{
            register(
                ToolbarViewDelegate(),
                LoadingViewDelegate(),
                EmptyViewDelegate(),
                ErrorViewDelegate()
            )
        }
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            RefreshHeaderLayout(
                context
            )
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> //加载完成时滚动列表显示新的内容
            layout.setEnableScrollContentWhenLoaded(true)
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }

    private fun initHttp(isDebug: Boolean){
        val apiServer = ApiServer()
        val sslConfig = HttpSslFactory.generateSslConfig()

        val okHttpClient = OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .cookieJar(CookieJarImpl.getInstance())
            .sslSocketFactory(sslConfig.sslSocketFactory, sslConfig.trustManager)
            .hostnameVerifier(HttpSslFactory.generateUnSafeHostnameVerifier())
            .build()

        EasyConfig.with(okHttpClient).apply {
            server = apiServer
            handler = RequestHandler()
            retryCount = 1
            retryTime = 2000
            isLogEnabled = isDebug
            logTag = "EasyHttp"
            val params = HashMap<String, String>().apply {
                put("apver", AppManager.getAppVersionName(AppHelper.getApplication()))
                put("appId", "20")
                put("platform", "2201")
                put("device_type", "1")
                put("device", "${DeviceInfoUtils.phoneBrand} ${DeviceInfoUtils.phoneModel}")
                put("userId",UserServiceProvider.getUserId()?:"")
                put("companyId",UserServiceProvider.getCompanyId()?:"")
            }
            this.params.putAll(params)
        }.into()
    }

    private fun initImageLoader(){
        val config = ImageLoaderConfig.Builder().apply {
            placePicRes(R.drawable.common_image_load_default)
            errorPicRes(R.drawable.common_image_load_default)
            setMaxDiskCache(1024 * 1024 * 50)
            setMaxMemoryCache(1024 * 1024 * 10)
        }.build()
        ImageLoaderUtils.init(config)
    }

}