package com.baihe.lihepro;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.baihe.common.base.BaseApplication;
import com.baihe.common.view.StatusLayout;
import com.baihe.lihepro.view.RefreshHeaderlayout;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

/**
 * Author：xubo
 * Time：2020-07-20
 * Description：
 */
public class CrmApp extends BaseApplication {
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new RefreshHeaderlayout(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //加载完成时滚动列表显示新的内容
                layout.setEnableScrollContentWhenLoaded(true);
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
//        StatusLayout.setDefaultExpandLayoutId(R.layout.layout_net_no_data);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }


}
