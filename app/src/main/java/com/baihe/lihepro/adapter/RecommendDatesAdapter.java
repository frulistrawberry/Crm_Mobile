package com.baihe.lihepro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.schedule.BookActive;
import com.baihe.lihepro.entity.schedule.HallItem;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecommendDatesAdapter extends RecyclerView.Adapter<RecommendDatesAdapter.Holder> {

    private LayoutInflater inflater;
    private Context context;
    private List<String> data;
    private OnItemClickListener listener;

    public OnItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RecommendDatesAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public interface OnItemClickListener{
        void onItemClick(View v, String item, int position);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new RecommendDatesAdapter.Holder(inflater.inflate(R.layout.item_rec_dates, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String item = data.get(position);
        try {
            Date date = new SimpleDateFormat("yyyy年MM月dd日").parse(item);
            String str = new SimpleDateFormat("yyyy-MM-dd").format(date);
            holder.tv_year.setText(str.split("-")[0]+"年");
            holder.tv_date.setText(String.format("%s月%s日",str.split("-")[1],str.split("-")[2]));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(holder.itemView,str,position);
                    }
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }




    }

    @Override
    public int getItemCount() {
         return data == null?0:data.size();
    }

    public void setData(List<String> dynamics) {
        data = dynamics;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        public TextView tv_year;
        public TextView tv_date;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_year = itemView.findViewById(R.id.tv_year);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
