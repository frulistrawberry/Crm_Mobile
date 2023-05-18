package com.baihe.lihepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.MsgEntity;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.Holder>{

    private LayoutInflater inflater;
    private Context context;

    private List<MsgEntity> rows;

    private MsgListAdapter.OnItemListener listener;

    public MsgListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.rows = new ArrayList<>();
    }

    public void update(List<MsgEntity> rows) {
        this.rows.clear();
        if (rows != null) {
            this.rows.addAll(rows);
        }
        notifyDataSetChanged();
    }

    public void add(List<MsgEntity> rows) {
        if (rows != null) {
            this.rows.addAll(rows);
        }
        notifyDataSetChanged();
    }

    public void rest() {
        this.rows.clear();
        notifyDataSetChanged();
    }

    public List<MsgEntity> getData() {
        return rows;
    }

    @NonNull
    @Override
    public MsgListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MsgListAdapter.Holder(inflater.inflate(R.layout.item_msg, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MsgListAdapter.Holder holder, int position) {
        final MsgEntity msgEntity = rows.get(position);
        holder.title.setText(msgEntity.getText());
        boolean isUnread = "1".equals(msgEntity.getUnread());
        holder.status.setText(isUnread?"未读":"已读");
        holder.status.setTextColor(Color.parseColor(isUnread?"#EF3C3C":"#C5C5C5"));
        List<KeyValueEntity> entityList = new ArrayList<>();
        KeyValueEntity entity = new KeyValueEntity();
        entity.setKey("客户");
        entity.setVal(msgEntity.getName());

        KeyValueEntity entity1 = new KeyValueEntity();
        entity1.setKey("渠道");
        entity1.setVal(msgEntity.getChannel_name());

        KeyValueEntity entity2 = new KeyValueEntity();
        entity2.setKey("类型");
        entity2.setVal(msgEntity.getType_txt());

        KeyValueEntity entity3 = new KeyValueEntity();
        entity3.setKey("创建时间");
        entity3.setVal(msgEntity.getPush_time());
        entityList.add(entity);
        entityList.add(entity1);
        entityList.add(entity2);
        entityList.add(entity3);
        holder.msg_kvl.setData(entityList);
        if ("1".equals(msgEntity.getCustomer_type())){
            holder.button.setImageResource(R.drawable.icon_msg_detail);
        }else {
            holder.button.setImageResource(R.drawable.icon_msg_know);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        KeyValueLayout msg_kvl;
        TextView title;
        TextView status;
        ImageView button;


        public Holder(@NonNull View itemView) {
            super(itemView);
            msg_kvl = itemView.findViewById(R.id.msg_kvl);
            title = itemView.findViewById(R.id.tv_text);
            status = itemView.findViewById(R.id.tv_status);
            button = itemView.findViewById(R.id.button);
        }
    }

    public void setListener(MsgListAdapter.OnItemListener listener) {
        this.listener = listener;
    }

    public interface OnItemListener {
        void onClick(int position);
    }

}
