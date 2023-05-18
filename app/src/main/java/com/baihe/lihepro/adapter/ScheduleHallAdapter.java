package com.baihe.lihepro.adapter;

import android.annotation.SuppressLint;
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
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.entity.schedule.HallItem;

import java.util.List;

public class ScheduleHallAdapter extends RecyclerView.Adapter<ScheduleHallAdapter.Holder>{
    private LayoutInflater inflater;
    private Context context;
    private List<HallItem> data;
    private int[] textColors = {Color.WHITE,Color.parseColor("#E8920F"),Color.parseColor("#E97264")};
    private int[] backgrounds = {R.drawable.schedule_staus_available_bg,R.drawable.schedule_staus_reserved_bg,R.drawable.schedule_staus_booked_bg};
    private String[] contents = {"空闲","已预留","已预订"};
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View v,HallItem item,int position);
    }

    public ScheduleHallAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<HallItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new Holder(inflater.inflate(R.layout.item_hall, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        HallItem item = data.get(position);
        if (position == 0){
            holder.v_divider.setVisibility(View.GONE);
        }else {
            holder.v_divider.setVisibility(View.VISIBLE);
        }
        holder.tv_hall_name.setText(item.getName());
        if (item.getLunchStatus()!=ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            holder.ll_lunch_status.setVisibility(View.VISIBLE);
            holder.tv_lunch_status.setTextColor(textColors[item.getLunchStatus()]);
            holder.tv_lunch_status.setBackgroundResource(backgrounds[item.getLunchStatus()]);
            holder.tv_lunch_status.setText(contents[item.getLunchStatus()]);
        }else {
            holder.ll_lunch_status.setVisibility(View.GONE);
        }

        if (item.getDinnerStatus()!= ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            holder.ll_dinner_status.setVisibility(View.VISIBLE);
            holder.tv_dinner_status.setTextColor(textColors[item.getDinnerStatus()]);
            holder.tv_dinner_status.setBackgroundResource(backgrounds[item.getDinnerStatus()]);
            holder.tv_dinner_status.setText(contents[item.getDinnerStatus()]);
        }else {
            holder.ll_dinner_status.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view,item,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null?0:data.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        public TextView tv_hall_name;
        public TextView tv_lunch_status;
        public TextView tv_dinner_status;
        public LinearLayout ll_lunch_status;
        public LinearLayout ll_dinner_status;
        public View v_divider;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_hall_name = itemView.findViewById(R.id.tv_hall_name);
            tv_lunch_status = itemView.findViewById(R.id.tv_lunch_status);
            tv_dinner_status = itemView.findViewById(R.id.tv_dinner_status);
            ll_lunch_status = itemView.findViewById(R.id.ll_lunch_status);
            ll_dinner_status = itemView.findViewById(R.id.ll_dinner_status);
            v_divider = itemView.findViewById(R.id.v_divider);
        }
    }
}
