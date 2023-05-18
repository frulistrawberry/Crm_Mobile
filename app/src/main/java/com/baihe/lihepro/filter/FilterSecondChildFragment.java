package com.baihe.lihepro.filter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baihe.lihepro.R;
import com.baihe.lihepro.filter.entity.FilterKVEntity;
import com.baihe.lihepro.view.FlowFixedLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-02-21
 * Description：二级页
 */
public class FilterSecondChildFragment extends Fragment {
    public static final String KEY_MULTIL = "key_multil";
    public static final String KEY_ENTITIS = "key_entitis";
    public static final String KEY_DEFAULT_VALUES = "key_default_values";
    public static final String KEY_VIEWPAGE_ID = "key_viewpage_id";
    public static final String KEY_CHILD_COUNT = "key_child_count";

    private FlowFixedLayout filter_second_child_label_ffl;
    private ArrayList<FilterKVEntity> filterKVEntities;
    private boolean isMuitil;
    private List<String> defaultValues;
    private int viewpageId;
    private int childCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_second_child, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filter_second_child_label_ffl = view.findViewById(R.id.filter_second_child_label_ffl);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filterKVEntities = (ArrayList<FilterKVEntity>) getArguments().getSerializable(KEY_ENTITIS);
        if (filterKVEntities == null) {
            filterKVEntities = new ArrayList<>();
        }
        defaultValues = (List<String>) getArguments().getSerializable(KEY_DEFAULT_VALUES);
        isMuitil = getArguments().getBoolean(KEY_MULTIL);
        viewpageId = getArguments().getInt(KEY_VIEWPAGE_ID);
        childCount = getArguments().getInt(KEY_CHILD_COUNT);
        initData();
        listener();
    }

    private void initData() {
        if (isMuitil) {
            filter_second_child_label_ffl.setLabelSelect(FlowFixedLayout.LabelSelect.MULTI, R.drawable.light_blue_round_drawable, Color.WHITE);
            filter_second_child_label_ffl.setMultiSelectMaxNum(Integer.MAX_VALUE);
        } else {
            filter_second_child_label_ffl.setLabelSelect(FlowFixedLayout.LabelSelect.SINGLE, R.drawable.light_blue_round_drawable, Color.WHITE);
            filter_second_child_label_ffl.setEnableSingleCancel(true);
        }
        filter_second_child_label_ffl.setLabelAdapter(new FlowFixedLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return filterKVEntities.size();
            }

            @Override
            public String getLabelText(int position) {
                return filterKVEntities.get(position).item_key;
            }

            @Override
            public boolean isSelect(int position) {
                return defaultValues != null && defaultValues.contains(filterKVEntities.get(position).item_val);
            }
        });
    }

    private void listener() {
        filter_second_child_label_ffl.setOnLabelClickListener(new FlowFixedLayout.OnLabelClickListener() {
            @Override
            public void onLabelClick(String text, int index) {
                if (!isMuitil && filter_second_child_label_ffl.getSelectLabelsIndex().size() > 0) {  //清空其他Fragment的选中
                    clearOhterSelectLabel();
                }
            }
        });
    }

    public List<String> getValue() {
        List<String> value = new ArrayList<>();
        for (int index : filter_second_child_label_ffl.getSelectLabelsIndex()) {
            value.add(filterKVEntities.get(index).item_val);
        }
        return value;
    }

    public void clearSelectLabelStatus() {
        filter_second_child_label_ffl.clearSelectLabelStatus();
    }

    public void clearOhterSelectLabel() {
        for (int i = 0; i < childCount; i++) {
            String brideTagName = "android:switcher:" + viewpageId + ":" + i;
            Fragment tagFragment = getFragmentManager().findFragmentByTag(brideTagName);
            if (tagFragment != null && tagFragment != this && tagFragment instanceof FilterSecondChildFragment) {
                FilterSecondChildFragment secondChildFragment = (FilterSecondChildFragment) tagFragment;
                secondChildFragment.clearSelectLabelStatus();
            }
        }
    }
}
