package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.base.BaseActivity;
import com.baihe.lihepro.GlideApp;
import com.baihe.lihepro.R;
import com.baihe.lihepro.view.PhotoBrowserView;
import com.github.xubo.statusbarutils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-05
 * Description：
 */
public class PhotoBrowserActivity extends BaseActivity {
    public static final String INTENT_PHOTO_URLS = "INTENT_PHOTO_URLS";
    public static final String INTENT_PHOTO_SELECT = "INTENT_PHOTO_SELECT";

    public static void start(Context context, ArrayList<String> urls, int selectPosition) {
        Intent intent = new Intent(context, PhotoBrowserActivity.class);
        intent.putStringArrayListExtra(INTENT_PHOTO_URLS, urls);
        intent.putExtra(INTENT_PHOTO_SELECT, selectPosition);
        context.startActivity(intent);
    }

    private List<String> urls;
    private int selectPosition;

    private ViewPager photo_browser_back_vp;
    private ImageView photo_browser_back_iv;
    private RelativeLayout photo_browser_title_rl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_browser);
        urls = getIntent().getStringArrayListExtra(INTENT_PHOTO_URLS);
        selectPosition = getIntent().getIntExtra(INTENT_PHOTO_SELECT, 0);
        init();
        initData();
        listener();
        if (StatusBarUtils.setStatusBarTransparen(this)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) photo_browser_title_rl.getLayoutParams();
            params.topMargin = StatusBarUtils.getStatusBarHeight(context);
        }
    }

    private void init() {
        photo_browser_back_vp = findViewById(R.id.photo_browser_back_vp);
        photo_browser_back_iv = findViewById(R.id.photo_browser_back_iv);
        photo_browser_title_rl = findViewById(R.id.photo_browser_title_rl);
    }

    private void initData() {
        PhotoBrowserAdapter photoBrowserAdapter = new PhotoBrowserAdapter(context, urls);
        photo_browser_back_vp.setAdapter(photoBrowserAdapter);
        photo_browser_back_vp.setCurrentItem(selectPosition);
    }

    private void listener() {
        photo_browser_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class PhotoBrowserAdapter extends PagerAdapter {
        private List<String> urls;
        private Context context;

        public PhotoBrowserAdapter(Context context, List<String> urls) {
            this.context = context;
            this.urls = urls;
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoBrowserView photo_browser_item_pbv = new PhotoBrowserView(context);
            container.addView(photo_browser_item_pbv);
            GlideApp.with(context).load(urls.get(position)).placeholder(R.drawable.image_load_default).into(photo_browser_item_pbv);
            photo_browser_item_pbv.setParams(position, urls.size());
            return photo_browser_item_pbv;
        }
    }
}
