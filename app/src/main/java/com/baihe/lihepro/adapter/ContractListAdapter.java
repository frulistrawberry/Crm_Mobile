package com.baihe.lihepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.ContractSearchActivity;
import com.baihe.lihepro.entity.ContractItemEntity;
import com.baihe.lihepro.view.FlowLayout;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class ContractListAdapter extends RecyclerView.Adapter<ContractListAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ContractItemEntity> list;

    private OnItemClickListener listener;

    private boolean isSearch;
    private String keyword;
    private int searchType = ContractSearchActivity.SEARCH_CONTRACT_TYPE;

    public ContractListAdapter(Context context, boolean isSearch) {
        this.context = context;
        this.isSearch = isSearch;
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

    public void setData(List<ContractItemEntity> entities) {
        this.list.clear();
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public void addData(List<ContractItemEntity> entities) {
        this.list.addAll(entities);
        notifyDataSetChanged();
    }

    public List<ContractItemEntity> getData() {
        return list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.activity_contract_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final ContractItemEntity contractItemEntity = list.get(position);

        holder.requirement_list_item_label_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return contractItemEntity.getCategory_color() == null?0:contractItemEntity.getCategory_color().size();
            }

            @Override
            public String getLabelText(int position) {
                return contractItemEntity.getCategory_color().get(position).getCategory_name();
            }

            @Override
            public Drawable getLabelBackgroundDrawable(int position) {
                GradientDrawable labelDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.round_label_drawable);
                String color = contractItemEntity.getCategory_color().get(position).getCategory_color();
                if (!TextUtils.isEmpty(color) && color.startsWith("#")) {
                    try {
                        labelDrawable.setColor(Color.parseColor(color));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return labelDrawable;
            }
        });

        if (isSearch) {
            StringBuffer buffer = new StringBuffer();
            switch (searchType) {
                case ContractSearchActivity.SEARCH_PHONE_TYPE:
                    buffer.append("手机号 | ");
                    break;
                case ContractSearchActivity.SEARCH_GROUPID_TYPE:
                    buffer.append("客户编号 | ");
                    break;
                case ContractSearchActivity.SEARCH_CONTRACT_TYPE:
                    buffer.append("合同号 | ");
                    break;
                case ContractSearchActivity.SEARCH_ORDER_TYPE:
                    buffer.append("订单号 | ");
                    break;
            }




            if (!TextUtils.isEmpty(contractItemEntity.getTitle())) {
                if (!TextUtils.isEmpty(keyword)) {
                    String newText = "<font color='#00B6EB'>" + keyword + "</font>";
                    String newTitle = contractItemEntity.getTitle().replace(keyword, newText);
                    buffer.append(newTitle);
                } else {
                    buffer.append(contractItemEntity.getTitle());
                }
            }
            holder.contract_list_item_name_tv.setText(Html.fromHtml(buffer.toString()));
        } else {
            holder.contract_list_item_name_tv.setText(contractItemEntity.getTitle());
        }
        holder.contract_list_item_data_kvl.setData(contractItemEntity.getOther());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(contractItemEntity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView contract_list_item_name_tv;
        private KeyValueLayout contract_list_item_data_kvl;
        private FlowLayout requirement_list_item_label_fl;

        public Holder(@NonNull View itemView) {
            super(itemView);
            contract_list_item_name_tv = itemView.findViewById(R.id.contract_list_item_name_tv);
            contract_list_item_data_kvl = itemView.findViewById(R.id.contract_list_item_data_kvl);
            requirement_list_item_label_fl = itemView.findViewById(R.id.requirement_list_item_label_fl);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ContractItemEntity contractItemEntity);
    }
}
