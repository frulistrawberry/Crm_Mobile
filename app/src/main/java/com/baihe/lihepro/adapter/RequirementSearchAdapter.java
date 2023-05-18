package com.baihe.lihepro.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.RequirementSearchActivity;
import com.baihe.lihepro.entity.RequirementEntity;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class RequirementSearchAdapter extends RecyclerView.Adapter<RequirementSearchAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<RequirementEntity> list;

    private String keyword;
    private int searchType = RequirementSearchActivity.SEARCH_PHONE_TYPE;

    private OnItemClickListener listener;

    public RequirementSearchAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = new ArrayList<>();
    }

    public void updateSearch(String keyword, int searchType) {
        this.keyword = keyword;
        this.searchType = searchType;
        this.list.clear();
        notifyDataSetChanged();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setData(List<RequirementEntity> entities) {
        this.list.clear();
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public void addData(List<RequirementEntity> entities) {
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public List<RequirementEntity> getData() {
        return list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.activity_requirement_search_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final RequirementEntity requirementEntity = list.get(position);
        StringBuffer buffer = new StringBuffer();
        buffer.append(requirementEntity.getCustomer_name());
        String content;
        switch (searchType) {
            case RequirementSearchActivity.SEARCH_PHONE_TYPE:
                content = requirementEntity.getEncode_phone();
                break;
            case RequirementSearchActivity.SEARCH_GROUPID_TYPE:
                content = requirementEntity.getCustomer_id();
                break;
            case RequirementSearchActivity.SEARCH_WECHAT_TYPE:
                content = requirementEntity.getWechat();
                break;
            default:
                content = requirementEntity.getEncode_phone();
                break;
        }
        if(!TextUtils.isEmpty(content)){
            if (!TextUtils.isEmpty(keyword)) {
                String newText = "<font color='#00B6EB'>" + keyword + "</font>";
                content = content.replace(keyword, newText);
            }
            buffer.append(" | " + content);
        }
        holder.requirement_search_item_name_tv.setText(Html.fromHtml(buffer.toString()));
        holder.requirement_search_item_data_kvl.setData(requirementEntity.getShow_array());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(requirementEntity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView requirement_search_item_name_tv;
        private KeyValueLayout requirement_search_item_data_kvl;

        public Holder(@NonNull View itemView) {
            super(itemView);
            requirement_search_item_name_tv = itemView.findViewById(R.id.requirement_search_item_name_tv);
            requirement_search_item_data_kvl = itemView.findViewById(R.id.requirement_search_item_data_kvl);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(RequirementEntity requirementEntity);
    }
}
