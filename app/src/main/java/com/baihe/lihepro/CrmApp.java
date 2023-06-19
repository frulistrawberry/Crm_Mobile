package com.baihe.lihepro;

import android.content.Context;

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
    }


}
