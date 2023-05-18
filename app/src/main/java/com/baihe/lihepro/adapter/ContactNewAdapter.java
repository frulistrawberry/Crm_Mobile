package com.baihe.lihepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.ToastUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.view.KeyValueEditLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-13
 * Description：
 */
public class ContactNewAdapter extends RecyclerView.Adapter<ContactNewAdapter.Holder> {
    private static final int ITEM_TYPE_NORMAL = 1;
    private static final int ITEM_TYPE_ADD = 2;

    private static final int CONTACT_MAX_NUM = 5;

    private List<List<KeyValueEntity>> dataList;
    private LayoutInflater inflater;
    private OnItemListner listner;

    public ContactNewAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.dataList = new ArrayList<>();
    }

    public void updateList(List<List<KeyValueEntity>> dataList) {
        this.dataList.clear();
        if (dataList != null) {
            this.dataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void addData(List<KeyValueEntity> data) {
        if (data != null) {
            this.dataList.add(data);
        }
        notifyDataSetChanged();
    }

    public List<List<KeyValueEntity>> getDataList() {
        return dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return ITEM_TYPE_ADD;
        } else {
            return ITEM_TYPE_NORMAL;
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_ADD) {
            return new Holder(inflater.inflate(R.layout.activity_contact_edit_item_add, parent, false), viewType);
        }
        return new Holder(inflater.inflate(R.layout.activity_contact_edit_item, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType == ITEM_TYPE_ADD) {
            if (getItemCount() > CONTACT_MAX_NUM) {
                holder.contact_edit_item_add_ll.setBackgroundResource(R.drawable.contact_add_drawable);
                holder.contact_edit_item_add_tv.setTextColor(Color.parseColor("#4A4C5C"));
            } else {
                holder.contact_edit_item_add_ll.setBackgroundResource(R.drawable.contact_add_drawable);
                holder.contact_edit_item_add_tv.setTextColor(Color.parseColor("#4A4C5C"));
            }
            holder.contact_edit_item_add_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getItemCount() <= CONTACT_MAX_NUM) {
                        if (listner != null) {
                            listner.add();
                        }
                    } else {
                        ToastUtils.toast("联系人最多维护" + CONTACT_MAX_NUM + "组");
                    }
                }
            });
        } else {
            final List<KeyValueEntity> data = dataList.get(position);
            holder.contact_edit_item_content_kvel.setData(data);
            KeyValueEntity keyValueEntity = holder.contact_edit_item_content_kvel.findEntityByParamKey("phone");
            if (keyValueEntity != null && keyValueEntity.getEvent() != null && KeyValueEditLayout.ItemAction.READONLY.valueOf().equals(keyValueEntity.getEvent().getAction())) {
                holder.contact_edit_item_delete_iv.setVisibility(View.GONE);
            } else {
                holder.contact_edit_item_delete_iv.setVisibility(View.VISIBLE);
            }
            holder.contact_edit_item_delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listner != null) {
                        listner.delete(data);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private KeyValueEditLayout contact_edit_item_content_kvel;
        private ImageView contact_edit_item_delete_iv;

        private LinearLayout contact_edit_item_add_ll;
        private TextView contact_edit_item_add_tv;

        public Holder(@NonNull View itemView, int viewTpe) {
            super(itemView);
            if (viewTpe == ITEM_TYPE_ADD) {
                contact_edit_item_add_ll = itemView.findViewById(R.id.contact_edit_item_add_ll);
                contact_edit_item_add_tv = itemView.findViewById(R.id.contact_edit_item_add_tv);
            } else {
                contact_edit_item_content_kvel = itemView.findViewById(R.id.contact_edit_item_content_kvel);
                contact_edit_item_delete_iv = itemView.findViewById(R.id.contact_edit_item_delete_iv);
            }
        }
    }

    public void setOnItemListner(OnItemListner listner) {
        this.listner = listner;
    }

    public interface OnItemListner {
        void add();

        void delete(List<KeyValueEntity> data);
    }
}
