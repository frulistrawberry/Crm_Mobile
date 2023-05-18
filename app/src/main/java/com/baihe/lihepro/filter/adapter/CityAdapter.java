package com.baihe.lihepro.filter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.filter.FilterCityManager;
import com.baihe.lihepro.filter.entity.FilterRegionEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-02-29
 * Description：
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.Holder> {
    private List<FilterRegionEntity> select;
    private FilterRegionEntity tabProvince;
    private FilterCityManager filterCityManager;
    private LayoutInflater inflater;
    private int magin13;
    private int magin20;

    public CityAdapter(Context context, FilterCityManager filterCityManager) {
        this.inflater = LayoutInflater.from(context);
        this.filterCityManager = filterCityManager;
        this.select = filterCityManager.getSelect();
        this.tabProvince = filterCityManager.getTabFilterRegionEntity();
        this.magin13 = CommonUtils.dp2pxForInt(context, 13);
        this.magin20 = CommonUtils.dp2pxForInt(context, 20);
    }

    public void update() {
        tabProvince = filterCityManager.getTabFilterRegionEntity();
        select = filterCityManager.getSelect();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_filter_city_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final FilterRegionEntity cityEntity = tabProvince.children.get(position);
        holder.filter_city_item_text_tv.setText(cityEntity.name);
        holder.filter_city_item_right_ll.setVisibility(View.GONE);
        if (select.contains(tabProvince)) {
            holder.filter_city_item_select_iv.setImageResource(R.drawable.check_icon);
            int selectIndex = select.indexOf(tabProvince);
            FilterRegionEntity selectProvince = select.get(selectIndex);
            if (selectProvince.children.contains(cityEntity)) {
                holder.filter_city_item_select_iv.setImageResource(R.drawable.check_icon);
            } else {
                holder.filter_city_item_select_iv.setImageResource(R.drawable.unchecked_icon);
            }
        } else {
            holder.filter_city_item_select_iv.setImageResource(R.drawable.unchecked_icon);
        }
        holder.filter_city_item_parent_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select.contains(tabProvince)) {
                    int selectIndex = select.indexOf(tabProvince);
                    FilterRegionEntity selectProvince = select.get(selectIndex);
                    if (selectProvince.children.contains(cityEntity)) {
                        selectProvince.children.remove(cityEntity);
                    } else {
                        FilterRegionEntity city = new FilterRegionEntity();
                        city.code = cityEntity.code;
                        city.name = cityEntity.name;
                        selectProvince.children.add(city);
                    }
                } else {
                    FilterRegionEntity province = new FilterRegionEntity();
                    province.code = tabProvince.code;
                    province.name = tabProvince.name;
                    province.children = new ArrayList<>();
                    FilterRegionEntity city = new FilterRegionEntity();
                    city.code = cityEntity.code;
                    city.name = cityEntity.name;
                    province.children.add(city);
                    select.add(province);
                }
                filterCityManager.update();
            }
        });
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.filter_city_item_parent_ll.getLayoutParams();
        if (position == 0) {
            params.topMargin = magin13;
            params.bottomMargin = 0;
        } else if (position == getItemCount() - 1) {
            params.topMargin = 0;
            params.bottomMargin = magin20;
        } else {
            params.topMargin = 0;
            params.bottomMargin = 0;
        }
    }

    @Override
    public int getItemCount() {
        return tabProvince != null ? tabProvince.children.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView filter_city_item_select_iv;
        TextView filter_city_item_text_tv;
        LinearLayout filter_city_item_right_ll;
        LinearLayout filter_city_item_parent_ll;

        public Holder(@NonNull View itemView) {
            super(itemView);
            filter_city_item_select_iv = itemView.findViewById(R.id.filter_city_item_select_iv);
            filter_city_item_text_tv = itemView.findViewById(R.id.filter_city_item_text_tv);
            filter_city_item_right_ll = itemView.findViewById(R.id.filter_city_item_right_ll);
            filter_city_item_parent_ll = itemView.findViewById(R.id.filter_city_item_parent_ll);
        }
    }
}
