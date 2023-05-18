package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.base.BaseLayoutParams;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.view.FontTextView;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.RequirementListPagerAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.RequirementTabEntity;
import com.baihe.lihepro.filter.FilterCallback;
import com.baihe.lihepro.filter.FilterUtils;
import com.baihe.lihepro.filter.entity.FilterEntity;
import com.baihe.lihepro.fragment.RequirementListFragment;
import com.baihe.lihepro.manager.HttpRequestManager;
import com.baihe.lihepro.manager.HttpTask;
import com.baihe.lihepro.view.TextTransitionRadioButton;
import com.baihe.lihepro.view.TextTransitionRadioGroup;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-07-24
 * Description：
 */
public class RequirementListActivity extends BaseActivity {

    private boolean isTeam;

    public static void start(Context context) {
        Intent intent = new Intent(context, RequirementListActivity.class);
        context.startActivity(intent);
    }

    private Toolbar requirement_list_title_tb;
    private ImageView requirement_list_title_search_iv;
    private HorizontalScrollView requirement_list_title_hs;
    private TextTransitionRadioGroup requirement_list_title_ttrg;
    private LinearLayout requirement_list_title_filter_ll;
    private ViewPager requirement_list_vp;
    private LinearLayout btnTab1;
    private LinearLayout btnTab2;
    private FontTextView tab1;
    private FontTextView tab2;
    private View line1;
    private View line2;

    private Map<Integer, Integer> idForIndexMap = new HashMap<>();
    private Map<Integer, TextTransitionRadioButton> indexForViewMap = new HashMap<>();

    private Map<String, Object> filterParmsMap = new HashMap<>();
    private RequirementListPagerAdapter adapter;
    private RequirementTabEntity tabEntity;
    private boolean showDis = false;

    private ArrayList<FilterEntity> filterEntities;
    private FilterCallback filterCallback = new FilterCallback() {
        @Override
        public void call(Map<String, Object> requestMap) {
            Logger.d(requestMap);
            filterParmsMap.clear();
            filterParmsMap.putAll(requestMap);
            refresh();
        }
    };

    public Map<String, Object> getFilterParmsMap() {
        return filterParmsMap;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_requirement_list_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_requirement_list, null), params);
        init();
        init2Tab();
        loadData();
        listener();
    }

    private void loadData() {
        HttpTask taskFilter = HttpTask.create(HttpRequest.create(UrlConstant.FILTER_CONFIGT_URL).putParam(new JsonParam("params")
                .putParamValue("tab", "customer")));
        HttpTask taskTabs = HttpTask.create(HttpRequest.create(UrlConstant.REQUIREMENT_PHASE_URL));
        HttpRequestManager.newInstance().get(taskFilter, new HttpRequestManager.TaskCallBack<ArrayList<FilterEntity>>() {

            @Override
            public ArrayList<FilterEntity> doInBackground(String response) {
                return (ArrayList<FilterEntity>) JsonUtils.parseList(response, FilterEntity.class);
            }

            @Override
            public void success(ArrayList<FilterEntity> filterEntities) {
                RequirementListActivity.this.filterEntities = filterEntities;
            }
        }).get(taskTabs, new HttpRequestManager.TaskCallBack<RequirementTabEntity>() {
            @Override
            public RequirementTabEntity doInBackground(String response) {
                return JsonUtils.parse(response, RequirementTabEntity.class);
            }

            @Override
            public void success(RequirementTabEntity tabEntity) {
                RequirementListActivity.this.tabEntity = tabEntity;
            }
        }).execute(new HttpRequestManager.CallBack() {
            @Override
            public void before() {
                statusLayout.loadingStatus();
            }

            @Override
            public void success() {
                statusLayout.normalStatus();
                requirement_list_title_filter_ll.setVisibility(View.VISIBLE);
                initTab();
            }

            @Override
            public void netError() {
                statusLayout.netErrorStatus();
            }

            @Override
            public void netFail() {
                statusLayout.netFailStatus();
            }

            @Override
            public void after() {

            }
        });
    }
    private void init2Tab() {
        btnTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTeam = false;
                tab1.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
                tab2.setFontStyle(FontTextView.FontStyle.NORMAL);
                tab1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                tab2.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                tab1.invalidate();
                tab2.invalidate();

                refresh();
            }
        });
        btnTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTeam = true;
                tab2.setFontStyle(FontTextView.FontStyle.HALF_BOLD);
                tab1.setFontStyle(FontTextView.FontStyle.NORMAL);
                tab1.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                tab2.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                line2.setVisibility(View.VISIBLE);
                line1.setVisibility(View.INVISIBLE);
                tab1.invalidate();
                tab2.invalidate();
                refresh();
            }
        });
    }

    private void init() {
        requirement_list_title_tb = findViewById(R.id.requirement_list_title_tb);
        requirement_list_title_search_iv = findViewById(R.id.requirement_list_title_search_iv);
        requirement_list_title_hs = findViewById(R.id.requirement_list_title_hs);
        requirement_list_title_ttrg = findViewById(R.id.requirement_list_title_ttrg);
        requirement_list_title_filter_ll = findViewById(R.id.requirement_list_title_filter_ll);
        requirement_list_vp = findViewById(R.id.requirement_list_vp);
        btnTab1 = findViewById(R.id.btn_tab1);
        btnTab2 = findViewById(R.id.btn_tab2);
        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
    }

    private void initTab() {
        //初始化radiobutton
        int count = tabEntity.getList().size();
        int margin = CommonUtils.dp2pxForInt(context, 16);
        for (int i = 0; i < count; i++) {
            KeyValueEntity keyValueEntity = tabEntity.getList().get(i);

            TextTransitionRadioButton textTransitionRadioButton = new TextTransitionRadioButton(context);
            textTransitionRadioButton.setTransition(true);
            textTransitionRadioButton.setSelectedTextBold(true);
            textTransitionRadioButton.setSelectedTextColor(Color.parseColor("#4A4C5C"));
            textTransitionRadioButton.setSelectedTextSize(CommonUtils.sp2px(context, 16));
            textTransitionRadioButton.setTextGravity(TextTransitionRadioButton.TextGravity.CENTER);
            textTransitionRadioButton.setUnSelectedTextBold(false);
            textTransitionRadioButton.setUnSelectedTextColor(Color.parseColor("#4A4C5C"));
            textTransitionRadioButton.setUnSelectedTextSize(CommonUtils.sp2px(context, 14));

            int id = (int) (i + System.currentTimeMillis());
            textTransitionRadioButton.setId(id);
            textTransitionRadioButton.setText(keyValueEntity.getKey());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textTransitionRadioButton.setButtonDrawable(null);
            } else {
                textTransitionRadioButton.setButtonDrawable(new BitmapDrawable());
            }
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.MATCH_PARENT);
            if (i == 0) {
                textTransitionRadioButton.setChecked(true);
                params.leftMargin = 0;
                params.rightMargin = margin;
            } else if (i == count - 1) {
                params.leftMargin = margin;
                params.rightMargin = 0;
            } else {
                params.leftMargin = margin;
                params.rightMargin = margin;
            }
            idForIndexMap.put(id, i);
            indexForViewMap.put(i, textTransitionRadioButton);

            requirement_list_title_ttrg.addView(textTransitionRadioButton, params);
        }

        adapter = new RequirementListPagerAdapter(getSupportFragmentManager(), tabEntity);
        requirement_list_vp.setAdapter(adapter);
        requirement_list_vp.setOffscreenPageLimit(tabEntity.getList().size() - 1);
    }

    private void listener() {
        requirement_list_title_ttrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = idForIndexMap.get(checkedId);
                if (tabEntity != null && index >= 0 && index < tabEntity.getList().size()) {
                    requirement_list_vp.setCurrentItem(index);
                }
            }
        });
        requirement_list_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                requirement_list_title_ttrg.move(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                TextTransitionRadioButton radioButton = indexForViewMap.get(position);
                if (radioButton != null) {
                    radioButton.setChecked(true);
                    scrollTitle(radioButton);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        requirement_list_title_filter_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterUtils.filter(context, filterEntities, filterCallback);
            }
        });
        requirement_list_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        statusLayout.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
            @Override
            public void onNetErrorClick() {
                loadData();
            }

            @Override
            public void onNetFailClick() {
                loadData();
            }

            @Override
            public void onExpandClick() {

            }
        });
        requirement_list_title_search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequirementSearchActivity.start(context,2);
            }
        });
    }

    private void scrollTitle(View view) {
        final int left = view.getLeft();
        final int right = view.getRight();
        final int screenWith = requirement_list_title_filter_ll.getLeft() - CommonUtils.dp2pxForInt(context, 5);
        final int childWidth = right - left;
        int scrollX = left + CommonUtils.dp2pxForInt(context, 15) - (screenWith - childWidth) / 2;
        requirement_list_title_hs.smoothScrollTo(scrollX, 0);
    }

    public void refresh() {
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            RequirementListFragment requirementListFragment = getFragment(i);
            if (requirementListFragment != null) {
                requirementListFragment.refreshData(isTeam);
            }
        }
    }

    private RequirementListFragment getFragment(int index) {
        String tagName = "android:switcher:" + requirement_list_vp.getId() + ":" + adapter.getItemId(index);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tagName);
        if (fragment != null && fragment instanceof RequirementListFragment) {
            return (RequirementListFragment) fragment;
        }
        return null;
    }
}
