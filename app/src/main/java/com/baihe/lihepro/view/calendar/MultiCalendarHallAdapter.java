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
import com.baihe.lihepro.entity.schedule.calendarnew.HomeHallItem;
import com.baihe.lihepro.entity.schedule.calendarnew.HomeReserveInfo;

import java.util.List;

public class MultiCalendarHallAdapter extends RecyclerView.Adapter<MultiCalendarHallAdapter.Holder> {

    private Context mContext;
    private List<HomeHallItem> mData;
    private String currentDate;
    private String hallNameColor;
    private String saleNameColor;

    public MultiCalendarHallAdapter(Context mContext, List<HomeHallItem> mData, String currentDate, String hallNameColor, String saleNameColor) {
        this.mContext = mContext;
        this.mData = mData;
        this.currentDate = currentDate;
        this.hallNameColor = hallNameColor;
        this.saleNameColor = saleNameColor;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_hall_multi,parent,false);
        Holder holder = new Holder(itemView);
//        holder.setIsRecyclable(false);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        HomeHallItem item = mData.get(position);
        if (item != null) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.line_all.setVisibility(View.INVISIBLE);
            holder.line_mid_lunch.setVisibility(View.INVISIBLE);
            holder.line_mid_dinner.setVisibility(View.INVISIBLE);
            holder.line_start.setVisibility(View.INVISIBLE);
            holder.line_end.setVisibility(View.INVISIBLE);
            holder.ll_all.setVisibility(View.GONE);
            holder.ll_lunch.setVisibility(View.GONE);
            holder.ll_dinner.setVisibility(View.GONE);

            if (hallNameColor!=null)
                holder.tv_hall_name.setTextColor(Color.parseColor(hallNameColor));
            if (saleNameColor!=null){
                holder.sale_dinner.setTextColor(Color.parseColor(saleNameColor));
                holder.sale_lunch.setTextColor(Color.parseColor(saleNameColor));
                holder.sale_all.setTextColor(Color.parseColor(saleNameColor));
            }

            holder.tv_hall_name.setText(item.getHallName());
            HomeReserveInfo lunch = item.getLunch();
            HomeReserveInfo dinner = item.getDinner();
            if (lunch!=null){
                holder.sale_lunch.setText(lunch.getSaleName());
                holder.sale_all.setText(item.getLunch().getSaleName());
                if (lunch.getIsMulti() == 0){
                    holder.v_lunch_flag.setVisibility(View.GONE);
                    holder.flag_lunch.setBackgroundResource(R.drawable.bg_calendar_flag_lunch_single);
                    holder.ll_lunch.setVisibility(View.VISIBLE);
                }else {
                    holder.v_lunch_flag.setVisibility(View.VISIBLE);
                    holder.flag_lunch.setBackgroundResource(R.drawable.bg_calendar_flag_lunch);
                    if (dinner!=null){
                        if (dinner.getIsMulti() == 1 ){
                            if (dinner.getReserve_num().equals(lunch.getReserve_num())) {
                                //午宴晚宴是同一个订单
                                holder.ll_all.setVisibility(View.VISIBLE);
                                if (currentDate.equals(lunch.getStart_date()) && currentDate.equals(lunch.getEnd_date())){
                                    //定了一天
                                    holder.ll_line.setVisibility(View.VISIBLE);
                                    holder.line_mid_lunch.setVisibility(View.VISIBLE);
                                }else {
                                    holder.line_all.setVisibility(View.VISIBLE);
                                }

                            }else {
                                holder.ll_lunch.setVisibility(View.VISIBLE);
                                holder.ll_line.setVisibility(View.VISIBLE);
                                holder.line_start.setVisibility(View.VISIBLE);
                            }
                        }else {
                            holder.ll_lunch.setVisibility(View.VISIBLE);
                            holder.ll_line.setVisibility(View.VISIBLE);
                            holder.line_start.setVisibility(View.VISIBLE);
                        }

                    }else {
                        holder.ll_lunch.setVisibility(View.VISIBLE);
                        holder.ll_line.setVisibility(View.VISIBLE);
                        holder.line_start.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (dinner!=null){
                holder.sale_dinner.setText(dinner.getSaleName());
                holder.sale_all.setText(item.getDinner().getSaleName());
                if (dinner.getIsMulti() == 0){
                    holder.v_dinner_flag.setVisibility(View.GONE);
                    holder.ll_dinner.setVisibility(View.VISIBLE);
                    holder.flag_dinner.setBackgroundResource(R.drawable.bg_calendar_flag_dinner_single);
                }else {
                    holder.v_dinner_flag.setVisibility(View.VISIBLE);
                    holder.flag_dinner.setBackgroundResource(R.drawable.bg_calendar_flag_dinner);
                    if (lunch!=null){
                        if (lunch.getIsMulti() == 1 ){
                            if (dinner.getReserve_num().equals(lunch.getReserve_num())) {
                                //午宴晚宴是同一个订单
                                holder.ll_all.setVisibility(View.VISIBLE);
                                if (currentDate.equals(lunch.getStart_date()) && currentDate.equals(lunch.getEnd_date())){
                                    //定了一天
                                    holder.ll_line.setVisibility(View.VISIBLE);
                                    holder.line_mid_dinner.setVisibility(View.VISIBLE);
                                }else {
                                    holder.line_all.setVisibility(View.VISIBLE);
                                }

                            }else {
                                holder.ll_dinner.setVisibility(View.VISIBLE);
                                holder.ll_line.setVisibility(View.VISIBLE);
                                holder.line_end.setVisibility(View.VISIBLE);
                            }
                        }else {
                            holder.ll_dinner.setVisibility(View.VISIBLE);
                            holder.ll_line.setVisibility(View.VISIBLE);
                            holder.line_end.setVisibility(View.VISIBLE);
                        }

                    }else {
                        holder.ll_dinner.setVisibility(View.VISIBLE);
                        holder.ll_line.setVisibility(View.VISIBLE);
                        holder.line_end.setVisibility(View.VISIBLE);
                    }
                }
            }
        }else {
            holder.itemView.setVisibility(View.INVISIBLE);
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
        public TextView sale_all;
        public LinearLayout ll_lunch;
        public LinearLayout ll_dinner;
        public LinearLayout ll_all;
        private LinearLayout ll_line;
        private View line_start;
        private View line_end;
        private View line_mid_lunch;
        private View line_mid_dinner;
        private View line_all;

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
            ll_line = itemView.findViewById(R.id.ll_line);
            line_start = itemView.findViewById(R.id.line_start);
            line_end = itemView.findViewById(R.id.line_end);
            line_mid_lunch = itemView.findViewById(R.id.line_mid_lunch);
            line_mid_dinner = itemView.findViewById(R.id.line_mid_dinner);
            line_all = itemView.findViewById(R.id.line_all);
            ll_all = itemView.findViewById(R.id.ll_all);
            sale_all = itemView.findViewById(R.id.sale_all);
        }
    }
}
