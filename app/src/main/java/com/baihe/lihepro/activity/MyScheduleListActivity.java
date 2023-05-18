package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.base.BaseLayoutParams;
import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.MyScheduleListPagerAdapter;
import com.baihe.lihepro.fragment.ScheduleListFragment;
import com.baihe.lihepro.view.indicator.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class MyScheduleListActivity extends BaseActivity {

    public static void start(Context context){
        context.startActivity(new Intent(context, MyScheduleListActivity.class));
    }

    private Toolbar my_schedule_list_title_tb;
    private ViewPager my_schedule_list_vp;
    private MyScheduleListPagerAdapter adapter;
    private MagicIndicator tabIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_my_schedule_list_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_my_schedule_list, null), params);
        init();
        initTab();
        listener();
    }

    private void initTab() {
        String[] tabs = {"预订单","预留单"};
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(ScheduleListFragment.newFragment(2));
        fragmentList.add(ScheduleListFragment.newFragment(1));
        CommonNavigator navigator = new CommonNavigator(this);
        navigator.setAdjustMode(true);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                SimplePagerTitleView titleView = new ScaleTransitionPagerTitleView(context);
                titleView.setText(tabs[index]);
                titleView.setNormalColor(Color.parseColor("#4A4C5C"));
                titleView.setSelectedColor(Color.parseColor("#4A4C5C"));
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        my_schedule_list_vp.setCurrentItem(index);
                    }
                });
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setYOffset(UIUtil.dip2px(context, 3));
                indicator.setColors(Color.parseColor("#00B6EB"));
                return indicator;
            }
        });
        tabIndicator.setNavigator(navigator);
        ViewPagerHelper.bind(tabIndicator,my_schedule_list_vp);
        adapter = new MyScheduleListPagerAdapter(getSupportFragmentManager(), fragmentList);
        my_schedule_list_vp.setAdapter(adapter);
    }

    private void listener() {
        my_schedule_list_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        my_schedule_list_title_tb = findViewById(R.id.my_schedule_list_title_tb);
        my_schedule_list_vp = findViewById(R.id.my_schedule_list_vp);
        tabIndicator = findViewById(R.id.tab_indicator);
    }
}
