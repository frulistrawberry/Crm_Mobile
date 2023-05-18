package com.baihe.lihepro.filter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.base.BaseFragment;
import com.baihe.common.manager.TaskManager;
import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.filter.adapter.ChannelPagerAdapter;
import com.baihe.lihepro.filter.entity.FilterEntity;
import com.baihe.lihepro.filter.entity.FilterKVEntity;
import com.baihe.lihepro.manager.ChannelManager;
import com.baihe.lihepro.utils.Utils;
import com.baihe.lihepro.view.TextTransitionRadioButton;
import com.baihe.lihepro.view.TextTransitionRadioGroup;
import com.github.xubo.statusbarutils.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Author：xubo
 * Time：2020-08-17
 * Description：
 */
public class FilterChannelFragment extends BaseFragment implements Observer {
    public static final String TAG = "CHANNEL";

    private HorizontalScrollView filte_channel_tab_hsv;
    private ImageView filter_channel_title_back_iv;
    private TextView filter_channel_title_tv;
    private TextTransitionRadioGroup filte_channel_tab_ttrg;
    private ViewPager filter_channel_content_vp;
    private TextView filter_channel_ok_tv;
    private HorizontalScrollView filter_channel_navigation_hsv;
    private TextView filter_channel_navigation_tv;

    private FilterViewModel filterViewModel;
    private FilterEntity filterEntity;

    private Map<Integer, Integer> idForIndexs = new HashMap<>();
    private Map<Integer, RadioButton> indexForViews = new HashMap<>();
    private ChannelPagerAdapter channelPagerAdapter;
    private List<FilterKVEntity> tabs = new ArrayList<>();
    private ChannelManager channelManager;
    private FilterChannelTabManager filterChannelTabManager;
    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int index = idForIndexs.get(checkedId);
            filter_channel_content_vp.setCurrentItem(index);
        }
    };
    private ChannelManager.StatusListener statusListener = new ChannelManager.StatusListener() {
        @Override
        public void onUpate() {
            initNavigation();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_filter_channel;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FilterActivity filterActivity = (FilterActivity) getActivity();
        filterViewModel = filterActivity.getMultilevelFilterViewModel();
        filterEntity = filterViewModel.getFilterEntity();
        channelManager = ChannelManager.newInstance(filterViewModel.isMultiple());
        channelManager.init(filterViewModel.getFilterEntity().list, filterViewModel.getDefaultOrSaveValueForMultilevel());
        filterChannelTabManager = FilterChannelTabManager.newInstance();
        filterChannelTabManager.addObserver(this);
        filterActivity.setChannelManager(channelManager);
        filterActivity.setFilterChannelTabManager(filterChannelTabManager);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        filterChannelTabManager.deleteObserver(this);
        channelManager.removeListener(statusListener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout filter_channel_title_rl = view.findViewById(R.id.filter_channel_title_rl);
        int statusHeight = StatusBarUtils.getStatusBarHeight(getContext());
        ((LinearLayout.LayoutParams) filter_channel_title_rl.getLayoutParams()).topMargin = statusHeight;
        filter_channel_title_back_iv = view.findViewById(R.id.filter_channel_title_back_iv);
        filter_channel_title_tv = view.findViewById(R.id.filter_channel_title_tv);
        filte_channel_tab_hsv = view.findViewById(R.id.filte_channel_tab_hsv);
        filte_channel_tab_ttrg = view.findViewById(R.id.filte_channel_tab_ttrg);
        filter_channel_content_vp = view.findViewById(R.id.filter_channel_content_vp);
        filter_channel_ok_tv = view.findViewById(R.id.filter_channel_ok_tv);
        filter_channel_navigation_hsv = view.findViewById(R.id.filter_channel_navigation_hsv);
        filter_channel_navigation_tv = view.findViewById(R.id.filter_channel_navigation_tv);
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

    private void initData() {
        filter_channel_title_tv.setText(filterEntity.title);
        channelPagerAdapter = new ChannelPagerAdapter(getChildFragmentManager(), tabs);
        filter_channel_content_vp.setAdapter(channelPagerAdapter);
        filter_channel_content_vp.setOffscreenPageLimit(1);
        initTab();
        initNavigation();
    }

    private void initTab() {
        idForIndexs.clear();
        indexForViews.clear();
        int tabNum = tabs.size() + 1;
        int margin = CommonUtils.dp2pxForInt(getContext(), 16);
        for (int i = 0; i < tabNum; i++) {
            TextTransitionRadioButton textTransitionRadioButton = new TextTransitionRadioButton(getContext());
            textTransitionRadioButton.setTransition(true);
            textTransitionRadioButton.setSelectedTextBold(true);
            textTransitionRadioButton.setSelectedTextColor(Color.parseColor("#4A4C5C"));
            textTransitionRadioButton.setSelectedTextSize(CommonUtils.sp2px(getContext(), 16));
            textTransitionRadioButton.setTextGravity(TextTransitionRadioButton.TextGravity.CENTER);
            textTransitionRadioButton.setUnSelectedTextBold(false);
            textTransitionRadioButton.setUnSelectedTextColor(Color.parseColor("#4A4C5C"));
            textTransitionRadioButton.setUnSelectedTextSize(CommonUtils.sp2px(getContext(), 14));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textTransitionRadioButton.setButtonDrawable(null);
            } else {
                textTransitionRadioButton.setButtonDrawable(new BitmapDrawable());
            }
            textTransitionRadioButton.setText(Utils.formatNumText(i + 1) + "级渠道");
            if (filterChannelTabManager.getTabIndex() == i) {
                textTransitionRadioButton.setChecked(true);
                textTransitionRadioButton.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
            int id = textTransitionRadioButton.getId();
            if (id == View.NO_ID) {
                id = (int) (i + System.currentTimeMillis());
                textTransitionRadioButton.setId(id);
            }
            idForIndexs.put(id, i);
            indexForViews.put(i, textTransitionRadioButton);

            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.MATCH_PARENT);
            if (i == 0) {
                textTransitionRadioButton.setChecked(true);
                params.leftMargin = 0;
                params.rightMargin = margin;
            } else if (i == tabNum - 1) {
                params.leftMargin = margin;
                params.rightMargin = 0;
            } else {
                params.leftMargin = margin;
                params.rightMargin = margin;
            }
            filte_channel_tab_ttrg.addView(textTransitionRadioButton, params);
        }
    }

    private void initNavigation() {
        if (!filterViewModel.isMultiple()) {
            List<FilterKVEntity> selectChannels = channelManager.getSelectChannels();
            if (selectChannels != null && selectChannels.size() > 0) {
                FilterKVEntity selectChannel = selectChannels.get(0);
                filter_channel_navigation_tv.setText(gettNavigationName(selectChannel));
                filter_channel_navigation_tv.requestLayout();
                filter_channel_navigation_hsv.setVisibility(View.VISIBLE);
            } else {
                filter_channel_navigation_hsv.setVisibility(View.GONE);
            }
        }else{
            filter_channel_navigation_hsv.setVisibility(View.GONE);
        }
    }

    private String gettNavigationName(FilterKVEntity channel) {
        if (channel == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (channel.getParent() != null) {
            stringBuffer.append(gettNavigationName(channel.getParent()));
            stringBuffer.append(" > " + channel.getItem_key());
        } else {
            stringBuffer.append(channel.getItem_key());
        }
        String name = stringBuffer.toString();
        return name;
    }

    private void listener() {
        channelManager.addListener(statusListener);
        filter_channel_title_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        filte_channel_tab_ttrg.setOnCheckedChangeListener(checkedChangeListener);
        filter_channel_content_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                filte_channel_tab_ttrg.move(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                RadioButton radioButton = indexForViews.get(position);
                if (radioButton != null) {
                    radioButton.setChecked(true);
                    scrollTitle(radioButton);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        filter_channel_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> values = channelManager.getSelectChannelIds();
                filterViewModel.setDefaultOrSaveValueForMultilevel(values);
                getParentFragmentManager().popBackStack();
            }
        });
    }

    private void scrollTitle(View view) {
        final int left = view.getLeft();
        final int right = view.getRight();
        final int childWidth = right - left;
        final int viewWith = CommonUtils.getScreenWidth(getContext()) * 320 / 375;
        int scrollX = left - (viewWith - childWidth) / 2;
        filte_channel_tab_hsv.smoothScrollTo(scrollX, 0);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (filterChannelTabManager.getTabIndex() > tabs.size()) {
            return;
        }
        //重置tab组
        if (filterChannelTabManager.getTabIndex() == 0) {
            tabs.clear();
            tabs.add(filterChannelTabManager.getTabChannel());
        } else if (tabs.size() == filterChannelTabManager.getTabIndex()) {  //添加tab
            tabs.add(filterChannelTabManager.getTabChannel());
        } else {  //在某个tab重置
            int count = tabs.size() - filterChannelTabManager.getTabIndex();
            for (int i = 0; i < count; i++) {
                tabs.remove(tabs.size() - 1);
            }
            tabs.add(filterChannelTabManager.getTabChannel());
        }
        filte_channel_tab_ttrg.removeAllViews();
        filte_channel_tab_ttrg.setOnCheckedChangeListener(null);
        initTab();
        channelPagerAdapter.notifyDataSetChanged();
        filte_channel_tab_ttrg.setOnCheckedChangeListener(checkedChangeListener);
        TaskManager.newInstance().runOnUi(new Runnable() {
            @Override
            public void run() {
                filter_channel_content_vp.setCurrentItem(filterChannelTabManager.getTabIndex() + 1);
            }
        }, 10);
    }

    public static class FilterChannelTabManager extends Observable {
        private FilterKVEntity tabChannel;
        private int tabIndex;

        public static FilterChannelTabManager newInstance() {
            return new FilterChannelTabManager();
        }

        private FilterChannelTabManager() {
            tabChannel = null;
            tabIndex = 0;
        }

        public void setTab(FilterKVEntity tabChannel, int tabIndex) {
            this.tabChannel = tabChannel;
            this.tabIndex = tabIndex;
            update();
        }

        public void update() {
            setChanged();
            notifyObservers();
        }

        public FilterKVEntity getTabChannel() {
            return tabChannel;
        }

        public int getTabIndex() {
            return tabIndex;
        }
    }
}
