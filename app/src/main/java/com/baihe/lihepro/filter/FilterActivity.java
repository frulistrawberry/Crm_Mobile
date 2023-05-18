package com.baihe.lihepro.filter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.filter.entity.FilterEntity;
import com.baihe.lihepro.manager.ChannelManager;
import com.github.xubo.statusbarutils.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-02-21
 * Description：
 */
public class FilterActivity extends AppCompatActivity {
    public static final String FILTER_ENTITIES = "FILTER_ENTITIES";
    public static final String FILTER_VALUE = "FILTER_VALUE";
    public static final String BIND_TAG = "BIND_TAG";

    private DrawerLayout filter_dl;
    private FrameLayout filter_content_fl;
    private FrameLayout filter_container_fl;
    private TextView filter_root_title_reset_tv;
    private LinearLayout filter_root_content_ll;
    private TextView filter_root_ok_tv;

    private FragmentManager fragmentManager;
    private List<FilterViewModel> filterViewModels = new ArrayList<>();
    private FilterViewModel cityFilterViewModel;
    private FilterViewModel city2FilterViewModel;
    private FilterViewModel multilevelFilterViewModel;
    private ArrayList<FilterEntity> filterEntities;
    //一一绑定的标记tag
    private String bindTag;
    private Handler handler = new Handler();
    private FilterCityManager filterCityManager;
    private FilterChannelFragment.FilterChannelTabManager filterChannelTabManager;
    private ChannelManager channelManager;

    public static final void start(Context context, ArrayList<FilterEntity> filterEntities, HashMap<String, Object> filterValueMap, String bindTag) {
        Intent intent = new Intent(context, FilterActivity.class);
        if (filterValueMap != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(FILTER_VALUE, filterValueMap);
            intent.putExtras(bundle);
        }
        if (filterEntities == null) {
            filterEntities = new ArrayList<>();
        }
        intent.putParcelableArrayListExtra(FILTER_ENTITIES, filterEntities);
        intent.putExtra(BIND_TAG, bindTag);
        context.startActivity(intent);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        StatusBarUtils.setStatusBarTransparenLight(this);
        filterEntities = getIntent().getParcelableArrayListExtra(FILTER_ENTITIES);
        bindTag = getIntent().getStringExtra(BIND_TAG);
        init();
        initData();
        listener();
    }

    private void init() {
        filter_dl = findViewById(R.id.filter_dl);
        filter_content_fl = findViewById(R.id.filter_content_fl);
        filter_container_fl = findViewById(R.id.filter_container_fl);
        RelativeLayout filter_root_title_rl = findViewById(R.id.filter_root_title_rl);
        int statusHeight = StatusBarUtils.getStatusBarHeight(this);
        ((LinearLayout.LayoutParams) filter_root_title_rl.getLayoutParams()).topMargin = statusHeight;
        filter_root_title_reset_tv = findViewById(R.id.filter_root_title_reset_tv);
        filter_root_content_ll = findViewById(R.id.filter_root_content_ll);
        filter_root_ok_tv = findViewById(R.id.filter_root_ok_tv);
        filter_container_fl.getLayoutParams().width = CommonUtils.getScreenWidth(this) * 320 / 375;
    }

    private void initData() {
        //取出上次保存的筛选选中值
        Map<String, Object> filterValueMap = (Map<String, Object>) getIntent().getSerializableExtra(FILTER_VALUE);
        //动态生成UI并保存Model对象
        int size = filterEntities.size();
        for (int i = 0; i < size; i++) {
            FilterEntity filterEntity = filterEntities.get(i);
            FilterViewModel filterViewModel = new FilterViewModel(this, filterEntity, filterValueMap != null ? filterValueMap.get(filterEntity.filter_key) : null);
            filter_root_content_ll.addView(filterViewModel.getView());

            if (i != size - 1) {
                View lineView = new View(this);
                lineView.setBackgroundColor(Color.parseColor("#EFEFEF"));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.dp2pxForInt(this, 0.5f));
                filter_root_content_ll.addView(lineView, params);
            }

            if (FilterViewModel.TYPE_CITY.equals(filterEntity.type)) {
                cityFilterViewModel = filterViewModel;
            }
            if (FilterViewModel.TYPE_CITY2.equals(filterEntity.type)) {
                city2FilterViewModel = filterViewModel;
            }
            if (FilterViewModel.TYPE_MULTILEVEL.equals(filterEntity.type)) {
                multilevelFilterViewModel = filterViewModel;
            }
            filterViewModels.add(filterViewModel);
        }

        fragmentManager = getSupportFragmentManager();
        //弹出侧拉栏
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                filter_dl.openDrawer(filter_content_fl);
            }
        }, 50);
    }

    public void listener() {
        filter_dl.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                finish();
            }
        });
        filter_root_title_reset_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (FilterViewModel filterViewModel : filterViewModels) {
                    filterViewModel.rest();
                }
            }
        });
        filter_root_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<String, Object> filterValueMap = new HashMap<>();
                for (FilterViewModel filterViewModel : filterViewModels) {
                    Map<String, Object> map = filterViewModel.getValue();
                    if (map != null) {
                        filterValueMap.putAll(map);
                    }
                }
                filter_dl.closeDrawer(filter_content_fl);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FilterManager.newInstance().notifyObservers(filterValueMap, bindTag);
                    }
                }, 100);
            }
        });
        if (cityFilterViewModel != null) {
            cityFilterViewModel.setFilterPageListener(new FilterViewModel.AddFilterPageListener() {
                @Override
                public void addFilterPage(String type) {
                    addFilterFragment(FilterCityFragment.TAG);
                }
            });
        }
        if (city2FilterViewModel != null) {
            city2FilterViewModel.setFilterPageListener(new FilterViewModel.AddFilterPageListener() {
                @Override
                public void addFilterPage(String type) {
                    addFilterFragment(FilterCity2Fragment.TAG);
                }
            });
        }
        if (multilevelFilterViewModel != null) {
            multilevelFilterViewModel.setFilterPageListener(new FilterViewModel.AddFilterPageListener() {
                @Override
                public void addFilterPage(String type) {
                    addFilterFragment(FilterChannelFragment.TAG);
                }
            });
        }
    }

    private void addFilterFragment(String fragmentTag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (FilterChannelFragment.TAG.equals(fragmentTag)) {  //渠道
            FilterChannelFragment fragment = new FilterChannelFragment();
            fragmentTransaction.add(R.id.filter_container_fl, fragment, FilterChannelFragment.TAG);
        } else if (FilterCityFragment.TAG.equals(fragmentTag)) {  //城市选择页-老样式
            FilterCityFragment fragment = new FilterCityFragment();
            fragmentTransaction.add(R.id.filter_container_fl, fragment, FilterCityFragment.TAG);
        } else if (FilterCity2Fragment.TAG.equals(fragmentTag)) {  //城市选择页-新样式
            FilterCity2Fragment fragment = new FilterCity2Fragment();
            fragmentTransaction.add(R.id.filter_container_fl, fragment, FilterCity2Fragment.TAG);
        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public FilterViewModel getCityFilterViewModel() {
        return cityFilterViewModel;
    }

    public FilterViewModel getCity2FilterViewModel() {
        return city2FilterViewModel;
    }

    public FilterViewModel getMultilevelFilterViewModel() {
        return multilevelFilterViewModel;
    }

    public FilterChannelFragment.FilterChannelTabManager getFilterChannelTabManager() {
        return filterChannelTabManager;
    }

    public void setFilterChannelTabManager(FilterChannelFragment.FilterChannelTabManager filterChannelTabManager) {
        this.filterChannelTabManager = filterChannelTabManager;
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public void setChannelManager(ChannelManager channelManager) {
        this.channelManager = channelManager;
    }

    public FilterCityManager getFilterCityManager() {
        return filterCityManager;
    }

    public void setFilterCityManager(FilterCityManager filterCityManager) {
        this.filterCityManager = filterCityManager;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (filter_dl.isDrawerOpen(filter_content_fl)) {
            filter_dl.closeDrawer(filter_content_fl);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
