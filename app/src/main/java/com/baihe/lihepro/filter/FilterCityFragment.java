package com.baihe.lihepro.filter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.filter.adapter.CityPagerAdapter;
import com.baihe.lihepro.filter.entity.FilterEntity;
import com.baihe.lihepro.filter.entity.FilterRegionEntity;
import com.baihe.lihepro.view.SliderRadioGroup;
import com.github.xubo.statusbarutils.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Author：xubo
 * Time：2020-02-21
 * Description：城市选页
 */
public class FilterCityFragment extends Fragment implements Observer {
    public static final String TAG = FilterViewModel.TYPE_CITY;

    private ImageView filter_city_title_back_iv;
    private TextView filter_city_title_reset_tv;
    private TextView filter_city_ok_tv;
    private TextView filter_city_title_tv;
    private SliderRadioGroup filte_city_tab_srl;
    private ViewPager filter_city_content_vp;
    private LinearLayout filter_city_parent_ll;

    private FilterViewModel filterViewModel;
    private FilterEntity filterEntity;
    private FilterCityManager filterCityManager;

    private Map<Integer, Integer> idForIndexs = new HashMap<>();
    private Map<Integer, RadioButton> indexForViews = new HashMap<>();
    private List<String> tabs = new ArrayList<>();
    private CityPagerAdapter cityPagerAdapter;
    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int index = idForIndexs.get(checkedId);
            filter_city_content_vp.setCurrentItem(index);
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FilterActivity filterActivity = (FilterActivity) getActivity();
        filterViewModel = filterActivity.getCityFilterViewModel();
        filterEntity = filterViewModel.getFilterEntity();
        filterCityManager = new FilterCityManager(filterViewModel.getAllRegion(), filterViewModel.getDefaultOrSaveValueForCity());
        filterActivity.setFilterCityManager(filterCityManager);
        filterCityManager.addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        filterCityManager.deleteObserver(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_city, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout filter_city_title_rl = view.findViewById(R.id.filter_city_title_rl);
        int statusHeight = StatusBarUtils.getStatusBarHeight(getContext());
        ((LinearLayout.LayoutParams) filter_city_title_rl.getLayoutParams()).topMargin = statusHeight;
        filter_city_title_back_iv = view.findViewById(R.id.filter_city_title_back_iv);
        filter_city_title_reset_tv = view.findViewById(R.id.filter_city_title_reset_tv);
        filter_city_ok_tv = view.findViewById(R.id.filter_city_ok_tv);
        filter_city_title_tv = view.findViewById(R.id.filter_city_title_tv);
        filte_city_tab_srl = view.findViewById(R.id.filte_city_tab_srl);
        filter_city_content_vp = view.findViewById(R.id.filter_city_content_vp);
        filter_city_parent_ll = view.findViewById(R.id.filter_city_parent_ll);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        listener();
    }

    private void initData() {
        filte_city_tab_srl.setSliderGravity(SliderRadioGroup.SliderGravity.BOTTOM);
        filter_city_title_tv.setText(filterEntity.title);
        initTab();
        cityPagerAdapter = new CityPagerAdapter(getChildFragmentManager(), tabs);
        filter_city_content_vp.setAdapter(cityPagerAdapter);
        filter_city_content_vp.setOffscreenPageLimit(1);
    }

    private void initTab() {
        tabs.clear();
        idForIndexs.clear();
        indexForViews.clear();
        for (int i = 0; i < 2; i++) {
            RadioButton radioButton = new RadioButton(getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                radioButton.setButtonDrawable(null);
            } else {
                radioButton.setButtonDrawable(new BitmapDrawable());
            }
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTextColor(Color.parseColor("#4A4C5C"));
            radioButton.setTextSize(16);
            int id = (int) (i + System.currentTimeMillis());
            radioButton.setId(id);
            idForIndexs.put(id, i);
            indexForViews.put(i, radioButton);
        }
        RadioButton radioButton = indexForViews.get(0);
        radioButton.setText("请选择");
        radioButton.setChecked(true);
        radioButton.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tabs.add("请选择");
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = 0;
        params.rightMargin = CommonUtils.dp2pxForInt(getContext(), 26);
        filte_city_tab_srl.addView(radioButton, params);
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        TranslateAnimation animation = null;
        if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN && enter) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            animation.setInterpolator(new DecelerateInterpolator());
        } else if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE && !enter) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            animation.setInterpolator(new DecelerateInterpolator());
        }
        if (animation == null) {
            animation = new TranslateAnimation(0, 0, 0, 0);
        } else {
            animation.setDuration(300);
        }
        return animation;
    }

    private void listener() {
        filte_city_tab_srl.setOnCheckedChangeListener(checkedChangeListener);
        filter_city_content_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                filte_city_tab_srl.move(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                for (Map.Entry<Integer, RadioButton> entry : indexForViews.entrySet()) {
                    entry.getValue().setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
                RadioButton radioButton = indexForViews.get(position);
                if (radioButton != null) {
                    radioButton.setChecked(true);
                    radioButton.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        filter_city_title_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        filter_city_title_reset_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCityManager.rest();
            }
        });
        filter_city_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<FilterRegionEntity> select = filterCityManager.getSelect();
                filterViewModel.setDefaultOrSaveValueForCity(select);
                getParentFragmentManager().popBackStack();
            }
        });
        filter_city_parent_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof Boolean && (boolean) arg) {
            FilterRegionEntity filterRegionEntity = filterCityManager.getTabFilterRegionEntity();
            int childCount = filte_city_tab_srl.getChildCount();
            if (filterRegionEntity != null) {
                RadioButton radioButton = indexForViews.get(1);
                radioButton.setText(filterRegionEntity.name);
                if (childCount == 1) {
                    tabs.add(filterRegionEntity.name);
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.MATCH_PARENT);
                    params.leftMargin = CommonUtils.dp2pxForInt(getContext(), 26);
                    params.rightMargin = 0;
                    filte_city_tab_srl.addView(radioButton, params);
                } else {
                    tabs.set(1, filterRegionEntity.name);
                }
                cityPagerAdapter.notifyDataSetChanged();
                filter_city_content_vp.setCurrentItem(1);
            } else {
                if (childCount == 2) {  //重置
                    filte_city_tab_srl.removeAllViews();
                    filte_city_tab_srl.setOnCheckedChangeListener(null);
                    initTab();
                    cityPagerAdapter = new CityPagerAdapter(getChildFragmentManager(), tabs);
                    filter_city_content_vp.setAdapter(cityPagerAdapter);
                    filter_city_content_vp.setOffscreenPageLimit(1);
                    filte_city_tab_srl.setOnCheckedChangeListener(checkedChangeListener);
                }
            }
        }
    }
}
