package com.baihe.lihepro.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.baihe.common.base.BaseFragment;
import com.baihe.lihepro.R;

/**
 * Author：xubo
 * Time：2020-08-17
 * Description：
 */
public class GuideFragment extends BaseFragment {
    public static final String INTENT_GUIDE_INDEX = "INTENT_GUIDE_INDEX";

    private LottieAnimationView guide_lav;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        guide_lav = view.findViewById(R.id.guide_lav);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int guideIndex = getArguments().getInt(INTENT_GUIDE_INDEX);
        String jsonName = "boot" + (guideIndex + 1) + ".json";
        guide_lav.setImageAssetsFolder("assets");
        guide_lav.setAnimation(jsonName);
    }

    public void startAnimation(){
        guide_lav.setProgress(0);
        guide_lav.playAnimation();
    };

    public void rest(){
        guide_lav.cancelAnimation();
        guide_lav.setProgress(0);
    };
}
