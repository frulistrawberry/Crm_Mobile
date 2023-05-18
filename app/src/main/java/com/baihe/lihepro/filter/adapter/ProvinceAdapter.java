package com.baihe.lihepro.filter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.Holder> {
    private List<FilterRegionEntity> all;
    private List<FilterRegionEntity> select;
    private FilterCityManager filterCityManager;
    private LayoutInflater inflater;
    private int magin13;
    private int magin20;

    public ProvinceAdapter(Context context, FilterCityManager filterCityManager) {
        this.inflater = LayoutInflater.from(context);
        this.filterCityManager = filterCityManager;
        this.all = filterCityManager.getAll();
        this.select = filterCityManager.getSelect();
        this.magin13 = CommonUtils.dp2pxForInt(context, 13);
        this.magin20 = CommonUtils.dp2pxForInt(context, 20);
    }

    public void update() {
        all = filterCityManager.getAll();
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
        final FilterRegionEntity provinceEntity = all.get(position);
        holder.filter_city_item_text_tv.setText(provinceEntity.name);
        if (select.contains(provinceEntity)) {
            holder.filter_city_item_select_iv.setImageResource(R.drawable.check_icon);
            int selectIndex = select.indexOf(provinceEntity);
            FilterRegionEntity selectProvince = select.get(selectIndex);
            String text = "已选" + selectProvince.children.size() + "项";
            if (selectProvince.children.size() == provinceEntity.children.size()) {
                text = "已选全部";
            }
            holder.filter_city_item_des_tv.setText(text);
        } else {
            holder.filter_city_item_select_iv.setImageResource(R.drawable.unchecked_icon);
            holder.filter_city_item_des_tv.setText("");
        }
        holder.filter_city_item_select_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select.contains(provinceEntity)) {
                    select.remove(provinceEntity);
                } else {
                    FilterRegionEntity province = new FilterRegionEntity();
                    province.code = provinceEntity.code;
                    province.name = provinceEntity.name;
                    province.children = new ArrayList<>();
                    for (FilterRegionEntity entity : provinceEntity.children) {
                        FilterRegionEntity city = new FilterRegionEntity();
                        city.code = entity.code;
                        city.name = entity.name;
                        province.children.add(city);
                    }
                    select.add(province);
                }
                filterCityManager.update();
            }
        });
        holder.filter_city_item_other_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterCityManager != null) {
                    filterCityManager.setTabFilterRegionEntity(provinceEntity);
                }
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
        return all.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView filter_city_item_select_iv;
        TextView filter_city_item_text_tv;
        LinearLayout filter_city_item_right_ll;
        TextView filter_city_item_des_tv;
        LinearLayout filter_city_item_parent_ll;
        RelativeLayout filter_city_item_other_rl;

        public Holder(@NonNull View itemView) {
            super(itemView);
            filter_city_item_select_iv = itemView.findViewById(R.id.filter_city_item_select_iv);
            filter_city_item_text_tv = itemView.findViewById(R.id.filter_city_item_text_tv);
            filter_city_item_right_ll = itemView.findViewById(R.id.filter_city_item_right_ll);
            filter_city_item_parent_ll = itemView.findViewById(R.id.filter_city_item_parent_ll);
            filter_city_item_des_tv = itemView.findViewById(R.id.filter_city_item_des_tv);
            filter_city_item_other_rl = itemView.findViewById(R.id.filter_city_item_other_rl);
        }
    }
}
