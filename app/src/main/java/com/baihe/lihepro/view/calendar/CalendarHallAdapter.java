package com.baihe.lihepro.view.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.schedule.HallItem;
import com.baihe.lihepro.entity.schedule.calendarnew.HomeHallItem;
import com.baihe.lihepro.entity.schedule.calendarnew.HomeReserveInfo;

import java.util.List;

public class CalendarHallAdapter extends RecyclerView.Adapter<CalendarHallAdapter.Holder> {

    private Context mContext;
    private List<HomeHallItem> mData;
    private String hallNameColor;
    private String saleNameColor;


    public CalendarHallAdapter(Context mContext, List<HomeHallItem> mData, String hallNameColor, String saleNameColor) {
        this.mContext = mContext;
        this.mData = mData;
        this.hallNameColor = hallNameColor;
        this.saleNameColor = saleNameColor;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_hall,parent,false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (position == 0)
            holder.divider.setVisibility(View.GONE);
        else
            holder.divider.setVisibility(View.VISIBLE);
        if (hallNameColor!=null)
            holder.tv_hall_name.setTextColor(Color.parseColor(hallNameColor));
        if (saleNameColor!=null){
            holder.sale_dinner.setTextColor(Color.parseColor(saleNameColor));
            holder.sale_lunch.setTextColor(Color.parseColor(saleNameColor));
        }
        HomeHallItem item = mData.get(position);
        if (item != null) {
            holder.tv_hall_name.setText(item.getHallName());
            HomeReserveInfo lunch = item.getLunch();
            HomeReserveInfo dinner = item.getDinner();
            if (lunch!=null){
                holder.ll_lunch.setVisibility(View.VISIBLE);
                holder.sale_lunch.setText(lunch.getSaleName());
                if (lunch.getIsMulti() == 0){
                    holder.v_lunch_flag.setVisibility(View.GONE);
                    holder.flag_lunch.setBackgroundResource(R.drawable.bg_calendar_flag_lunch_single);
                }else {
                    holder.v_lunch_flag.setVisibility(View.VISIBLE);
                    holder.flag_lunch.setBackgroundResource(R.drawable.bg_calendar_flag_lunch);
                }
            }else {
                holder.ll_lunch.setVisibility(View.GONE);
            }

            if (dinner!=null){
                holder.ll_dinner.setVisibility(View.VISIBLE);
                holder.sale_dinner.setText(dinner.getSaleName());
                if (dinner.getIsMulti() == 0){
                    holder.v_dinner_flag.setVisibility(View.GONE);
                    holder.flag_dinner.setBackgroundResource(R.drawable.bg_calendar_flag_dinner_single);
                }else {
                    holder.v_dinner_flag.setVisibility(View.VISIBLE);
                    holder.flag_dinner.setBackgroundResource(R.drawable.bg_calendar_flag_dinner);
                }
            }else {
                holder.ll_dinner.setVisibility(View.GONE);
            }
        }



    }

    @Override
    public int getItemCount() {
        return mData!=null?mData.size():0;
    }

    public class Holder extends RecyclerView.ViewHolder{
        public View divider;
        public TextView tv_hall_name;
        public TextView flag_lunch;
        public TextView sale_lunch;
        public View v_lunch_flag;
        public View v_dinner_flag;
        public TextView flag_dinner;
        public TextView sale_dinner;
        public LinearLayout ll_lunch;
        public LinearLayout ll_dinner;

        public Holder(@NonNull View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.v_divider);
            tv_hall_name = itemView.findViewById(R.id.tv_hall_name);
            flag_lunch = itemView.findViewById(R.id.flag_lunch);
            sale_lunch = itemView.findViewById(R.id.sale_lunch);
            v_lunch_flag = itemView.findViewById(R.id.v_lunch_flag);
            v_dinner_flag = itemView.findViewById(R.id.v_dinner_flag);
            flag_dinner = itemView.findViewById(R.id.flag_dinner);
            sale_dinner = itemView.findViewById(R.id.sale_dinner);
            ll_lunch = itemView.findViewById(R.id.ll_lunch);
            ll_dinner = itemView.findViewById(R.id.ll_dinner);
        }
    }
}
