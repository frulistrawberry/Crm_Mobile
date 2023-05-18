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
import android.widget.HorizontalScrollView;
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

import com.baihe.common.manager.TaskManager;
import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.filter.adapter.SecondPagerAdapter;
import com.baihe.lihepro.filter.entity.FilterEntity;
import com.baihe.lihepro.filter.entity.FilterKVEntity;
import com.baihe.lihepro.view.SliderRadioGroup;
import com.github.xubo.statusbarutils.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-02-21
 * Description：二级页
 */
public class FilterSecondFragment extends Fragment {
    public static final String TAG = "SECOND";

    private ImageView filter_second_title_back_iv;
    private TextView filter_second_title_tv;
    private TextView filter_second_title_reset_tv;
    private TextView filter_second_ok_tv;
    private HorizontalScrollView filter_second_tab_sv;
    private SliderRadioGroup filter_second_tab_srl;
    private ViewPager filter_second_content_vp;
    private LinearLayout filter_second_parent_ll;

    private FilterViewModel filterViewModel;
    private FilterEntity filterEntity;
    private SecondPagerAdapter secondPagerAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FilterActivity filterActivity = (FilterActivity) getActivity();
        filterViewModel = filterActivity.getMultilevelFilterViewModel();
        filterEntity = filterViewModel.getFilterEntity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout filter_second_title_rl = view.findViewById(R.id.filter_second_title_rl);
        int statusHeight = StatusBarUtils.getStatusBarHeight(getContext());
        ((LinearLayout.LayoutParams) filter_second_title_rl.getLayoutParams()).topMargin = statusHeight;
        filter_second_title_back_iv = view.findViewById(R.id.filter_second_title_back_iv);
        filter_second_title_tv = view.findViewById(R.id.filter_second_title_tv);
        filter_second_title_reset_tv = view.findViewById(R.id.filter_second_title_reset_tv);
        filter_second_ok_tv = view.findViewById(R.id.filter_second_ok_tv);
        filter_second_tab_sv = view.findViewById(R.id.filter_second_tab_sv);
        filter_second_tab_srl = view.findViewById(R.id.filter_second_tab_srl);
        filter_second_tab_srl.setSliderGravity(SliderRadioGroup.SliderGravity.BOTTOM);
        filter_second_content_vp = view.findViewById(R.id.filter_second_content_vp);
        filter_second_parent_ll = view.findViewById(R.id.filter_second_parent_ll);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        listener();
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
        filter_second_title_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        filter_second_title_reset_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelectLabelStatus();
            }
        });
        filter_second_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> values = getValue();
                filterViewModel.setDefaultOrSaveValueForMultilevel(values);
                getParentFragmentManager().popBackStack();
            }
        });
        filter_second_parent_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initData() {
        filter_second_title_tv.setText(filterEntity.title);
        secondPagerAdapter = new SecondPagerAdapter(getChildFragmentManager(), filterEntity.list, filterViewModel.isMultiple(), filterViewModel.getDefaultOrSaveValueForMultilevel(), filter_second_content_vp.getId());
        filter_second_content_vp.setAdapter(secondPagerAdapter);
        filter_second_content_vp.setOffscreenPageLimit(filterEntity.list.size() - 1);
        addTab();
    }

    private void addTab() {
        final Map<Integer, Integer> idForIndexs = new HashMap<>();
        final Map<Integer, RadioButton> indexForViews = new HashMap<>();
        List<FilterKVEntity> filterKVEntities = filterEntity.list;
        int size = filterKVEntities.size();
        for (int i = 0; i < size; i++) {
            FilterKVEntity filterKVEntity = filterKVEntities.get(i);
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(filterKVEntity.item_key);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                radioButton.setButtonDrawable(null);
            } else {
                radioButton.setButtonDrawable(new BitmapDrawable());
            }
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTextColor(Color.parseColor("#4A4C5C"));
            radioButton.setTextSize(16);
            if (i == 0) {
                radioButton.setChecked(true);
                radioButton.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
            int id = (int) (i + System.currentTimeMillis());
            radioButton.setId(id);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.MATCH_PARENT);
            if (i == 0) {
                params.rightMargin = CommonUtils.dp2pxForInt(getContext(), 26);
            } else if (i == size - 1) {
                params.leftMargin = CommonUtils.dp2pxForInt(getContext(), 26);
            } else {
                params.leftMargin = CommonUtils.dp2pxForInt(getContext(), 26);
                params.rightMargin = CommonUtils.dp2pxForInt(getContext(), 26);
            }
            filter_second_tab_srl.addView(radioButton, params);
            idForIndexs.put(id, i);
            indexForViews.put(i, radioButton);
        }
        filter_second_tab_srl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = idForIndexs.get(checkedId);
                filter_second_content_vp.setCurrentItem(index);
            }
        });
        filter_second_content_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                filter_second_tab_srl.move(position, positionOffset);
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
                scrollTitle(radioButton);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public List<String> getValue() {
        List<String> value = new ArrayList<>();
        int size = secondPagerAdapter.getCount();
        for (int i = 0; i < size; i++) {
            String brideTagName = "android:switcher:" + filter_second_content_vp.getId() + ":" + secondPagerAdapter.getItemId(i);
            Fragment tagFragment = getChildFragmentManager().findFragmentByTag(brideTagName);
            if (tagFragment instanceof FilterSecondChildFragment) {
                FilterSecondChildFragment secondChildFragment = (FilterSecondChildFragment) tagFragment;
                value.addAll(secondChildFragment.getValue());
            }
        }
        return value;
    }

    public List<String> clearSelectLabelStatus() {
        List<String> value = new ArrayList<>();
        int size = secondPagerAdapter.getCount();
        for (int i = 0; i < size; i++) {
            String brideTagName = "android:switcher:" + filter_second_content_vp.getId() + ":" + secondPagerAdapter.getItemId(i);
            Fragment tagFragment = getChildFragmentManager().findFragmentByTag(brideTagName);
            if (tagFragment != null && tagFragment instanceof FilterSecondChildFragment) {
                FilterSecondChildFragment secondChildFragment = (FilterSecondChildFragment) tagFragment;
                secondChildFragment.clearSelectLabelStatus();
            }
        }
        return value;
    }

    private void scrollTitle(View view) {
        if(view==null){
            return;
        }
        final int left = view.getLeft();
        final int right = view.getRight();
        final int screenWith = CommonUtils.getScreenWidth(getContext());

        if (left + right > screenWith) {  //当选择栏目大于屏幕的中间处 居中显示
            TaskManager.newInstance().runOnUi(new Runnable() {
                @Override
                public void run() {
                    filter_second_tab_sv.smoothScrollTo((left + right - screenWith) / 2, 0);
                }
            }, 50);
        } else {//当选择栏目左边距小于屏幕一般距离，直接左滑到头
            TaskManager.newInstance().runOnUi(new Runnable() {
                @Override
                public void run() {
                    filter_second_tab_sv.smoothScrollTo(0, 0);
                }
            }, 50);
        }
    }
}
