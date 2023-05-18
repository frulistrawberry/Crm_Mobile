package com.baihe.lihepro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.GlideApp;
import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.photo.LocalPhotoEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-08-08
 * Description：
 */
public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.Holder> {
    private List<LocalPhotoEntity> photoInfos;
    private Map<Integer, Integer> selectPhotoInfos;
    private LayoutInflater inflater;
    private Context context;
    private int size;
    private OnItemClickListener itemClickListener;
    private int maxSize;

    public PhotoListAdapter(Context context, int maxSize) {
        this.context = context;
        this.maxSize = maxSize;
        this.inflater = LayoutInflater.from(context);
        this.photoInfos = new ArrayList<>();
        this.selectPhotoInfos = new HashMap<>();
        int screenWidth = CommonUtils.getScreenWidth(context);
        size = (screenWidth - 5 * CommonUtils.dp2pxForInt(context, 4)) / 4;
    }

    public void addList(List<LocalPhotoEntity> photoInfos) {
        this.photoInfos.addAll(photoInfos);
        notifyDataSetChanged();
    }

    public void setList(List<LocalPhotoEntity> photoInfos) {
        this.photoInfos.clear();
        this.photoInfos.addAll(photoInfos);
        notifyDataSetChanged();
    }

    public ArrayList<LocalPhotoEntity> getList() {
        return (ArrayList<LocalPhotoEntity>) photoInfos;
    }

    public int getListSize() {
        return photoInfos.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_photo_list_item, null);
        PhotoListAdapter.Holder holder = new PhotoListAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        LocalPhotoEntity photoInfo = photoInfos.get(position);
        holder.photo_list_item_photo_iv.getLayoutParams().width = size;
        holder.photo_list_item_photo_iv.getLayoutParams().height = size;
        holder.photo_list_item_mask_view.getLayoutParams().width = size;
        holder.photo_list_item_mask_view.getLayoutParams().height = size;
        String imagePath = photoInfo.getPhotoThumbnailPath();
        if (TextUtils.isEmpty(imagePath)) {
            imagePath = photoInfo.getPhotoPath();
        }
        GlideApp.with(context).load(imagePath).into(holder.photo_list_item_photo_iv);
        holder.photo_list_item_select_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = selectPhotoInfos.size();
                if (selectPhotoInfos.containsKey(position)) {
                    int selectValue = selectPhotoInfos.get(position);
                    selectPhotoInfos.remove(position);
                    for (Map.Entry<Integer, Integer> entry : selectPhotoInfos.entrySet()) {
                        int index = entry.getKey();
                        int value = entry.getValue();
                        if (value > selectValue) {
                            selectPhotoInfos.put(index, value - 1);
                        }
                    }
                    notifyDataSetChanged();
                } else if (size < maxSize) {
                    selectPhotoInfos.put(position, size + 1);
                    notifyDataSetChanged();
                }
                if (itemClickListener != null) {
                    itemClickListener.select(haveSelect());
                }
            }
        });
        if (selectPhotoInfos.containsKey(position)) {
            holder.photo_list_item_select_tv.setBackgroundResource(R.drawable.photo_list_item_select_s);
            holder.photo_list_item_select_tv.setText(selectPhotoInfos.get(position) + "");
        } else {
            holder.photo_list_item_select_tv.setBackgroundResource(R.drawable.photo_list_item_select_n);
            holder.photo_list_item_select_tv.setText("");
        }
        if (selectPhotoInfos.size() < maxSize) {
            holder.photo_list_item_mask_view.setVisibility(View.GONE);
        } else {
            if (selectPhotoInfos.containsKey(position)) {
                holder.photo_list_item_mask_view.setVisibility(View.GONE);
            } else {
                holder.photo_list_item_mask_view.setVisibility(View.VISIBLE);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoInfos.size();
    }

    public boolean haveSelect() {
        return selectPhotoInfos.size() > 0 ? true : false;
    }

    public Map<Integer, Integer> getSelectPhotoInfos() {
        return selectPhotoInfos;
    }

    public void setSelectPhotoInfos(Map<Integer, Integer> selectPhotoInfos) {
        this.selectPhotoInfos = selectPhotoInfos;
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public ImageView photo_list_item_photo_iv;
        public TextView photo_list_item_select_tv;
        public View photo_list_item_mask_view;

        public Holder(View itemView) {
            super(itemView);
            // TODO Auto-generated constructor stub
            photo_list_item_photo_iv = itemView.findViewById(R.id.photo_list_item_photo_iv);
            photo_list_item_select_tv = itemView.findViewById(R.id.photo_list_item_select_tv);
            photo_list_item_mask_view = itemView.findViewById(R.id.photo_list_item_mask_view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void select(boolean haveSelect);
    }

}
