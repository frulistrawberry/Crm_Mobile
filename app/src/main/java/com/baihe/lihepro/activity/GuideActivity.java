package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.manager.TaskManager;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.SPConstant;
import com.baihe.lihepro.fragment.GuideFragment;
import com.baihe.lihepro.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-17
 * Description：
 */
public class GuideActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

    private ImageView guide_skip_iv;
    private ViewPager guide_vp;
    private CuidePagerAdapter cuidePagerAdapter;
    private int selectIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        init();
        initData();
        listener();
    }

    private void init() {
        guide_skip_iv = findViewById(R.id.guide_skip_iv);
        guide_vp = findViewById(R.id.guide_vp);
    }

    private void initData() {
        cuidePagerAdapter = new CuidePagerAdapter(getSupportFragmentManager());
        guide_vp.setAdapter(cuidePagerAdapter);
        guide_vp.setOffscreenPageLimit(1);

        TaskManager.newInstance().runOnUi(new Runnable() {
            @Override
            public void run() {
                GuideFragment guideFragment = cuidePagerAdapter.findFragment(selectIndex);
                if (guideFragment != null) {
                    guideFragment.startAnimation();
                }
            }
        }, 500);
    }

    private void listener() {
        guide_skip_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.getPhoneSP(context).edit().putBoolean(SPConstant.KEY_GUIDE, false).apply();
                LoginActivity.start(context);
                finish();
            }
        });
        guide_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int index = guide_vp.getCurrentItem();
                if (state == ViewPager.SCROLL_STATE_IDLE && selectIndex != index) {
                    selectIndex = index;
                    List<GuideFragment> otherGuideFragments = cuidePagerAdapter.findFragmentForOthers(index);
                    for (GuideFragment guideFragment : otherGuideFragments) {
                        guideFragment.rest();
                    }
                    GuideFragment guideFragment = cuidePagerAdapter.findFragment(index);
                    if (guideFragment != null) {
                        guideFragment.startAnimation();
                    }
                }
            }
        });
    }

    public class CuidePagerAdapter extends FragmentPagerAdapter {

        public CuidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new GuideFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(GuideFragment.INTENT_GUIDE_INDEX, position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        public GuideFragment findFragment(int position) {
            String tag = "android:switcher:" + R.id.guide_vp + ":" + getItemId(position);
            Fragment tagFragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (tagFragment != null && tagFragment instanceof GuideFragment) {
                GuideFragment guideFragment = (GuideFragment) tagFragment;
                return guideFragment;
            }
            return null;
        }

        public List<GuideFragment> findFragmentForOthers(int position) {
            List<GuideFragment> guideFragments = new ArrayList<>();
            for (int i = 0; i < getCount(); i++) {
                if (i != position) {
                    String tag = "android:switcher:" + R.id.guide_vp + ":" + getItemId(i);
                    Fragment tagFragment = getSupportFragmentManager().findFragmentByTag(tag);
                    if (tagFragment != null && tagFragment instanceof GuideFragment) {
                        GuideFragment guideFragment = (GuideFragment) tagFragment;
                        guideFragments.add(guideFragment);
                    }
                }
            }
            return guideFragments;
        }
    }
}
