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
import com.baihe.lihepro.filter.adapter.CityAdapter;

import java.util.Observable;
import java.util.Observer;

/**
 * Author：xubo
 * Time：2020-02-29
 * Description：
 */
public class FilterCityCityFragment extends Fragment implements Observer {
    private RecyclerView filter_city_city_rv;
    private FilterCityManager filterCityManager;
    private CityAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FilterActivity crmFilterActivity = (FilterActivity) getActivity();
        filterCityManager = crmFilterActivity.getFilterCityManager();
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
        return inflater.inflate(R.layout.fragment_filter_city_city, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filter_city_city_rv = view.findViewById(R.id.filter_city_city_rv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        adapter = new CityAdapter(getContext(), filterCityManager);
        filter_city_city_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        filter_city_city_rv.setAdapter(adapter);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (adapter != null) {
            adapter.update();
        }
    }
}
