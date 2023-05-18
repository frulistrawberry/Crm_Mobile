package com.baihe.lihepro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.schedule.BookActive;

import java.util.List;

public class ScheduleBookActiveAdapter extends RecyclerView.Adapter<ScheduleBookActiveAdapter.Holder> {

    private LayoutInflater inflater;
    private Context context;
    private List<BookActive> data;

    public ScheduleBookActiveAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ScheduleBookActiveAdapter.Holder(inflater.inflate(R.layout.item_book_active, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        BookActive item = data.get(position);
        holder.tv_content.setText(item.getContent());
        if (!TextUtils.isEmpty(item.getDateTime()))
            holder.tv_date.setText(item.getDateTime());
        else {
            holder.tv_date.setText("");
        }
    }

    @Override
    public int getItemCount() {
         return data == null?0:data.size();
    }

    public void setData(List<BookActive> dynamics) {
        data = dynamics;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        public TextView tv_content;
        public TextView tv_date;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
