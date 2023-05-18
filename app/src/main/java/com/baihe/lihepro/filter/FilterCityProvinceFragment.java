package com.baihe.lihepro.filter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.filter.adapter.ProvinceAdapter;

import java.util.Observable;
import java.util.Observer;

/**
 * Author：xubo
 * Time：2020-02-29
 * Description：
 */
public class FilterCityProvinceFragment extends Fragment implements Observer {
    private RecyclerView filter_city_province_rv;
    private FilterCityManager filterCityManager;
    private ProvinceAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FilterActivity filterActivity = (FilterActivity) getActivity();
        filterCityManager = filterActivity.getFilterCityManager();
        if (filterCityManager != null) {
            filterCityManager.addObserver(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (filterCityManager != null) {
            filterCityManager.deleteObserver(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_city_province, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filter_city_province_rv = view.findViewById(R.id.filter_city_province_rv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        adapter = new ProvinceAdapter(getContext(), filterCityManager);
        filter_city_province_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        filter_city_province_rv.setAdapter(adapter);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (adapter != null) {
            adapter.update();
        }
    }
}
