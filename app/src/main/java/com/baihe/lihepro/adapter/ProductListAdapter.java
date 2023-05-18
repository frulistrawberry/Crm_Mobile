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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.GlideApp;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.FollowDetailActivity;
import com.baihe.lihepro.activity.PhotoBrowserActivity;
import com.baihe.lihepro.entity.ProductEntity;
import com.baihe.lihepro.glide.transformation.RoundedCornersTransformation;
import com.baihe.lihepro.manager.ProductSelectManager;
import com.baihe.lihepro.view.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-04
 * Description：
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.Holder> {
    private LayoutInflater inflater;
    private Context context;

    private List<ProductEntity> rows;
    private ProductSelectManager productSelectManager;

    private OnItemListener listener;

    public ProductListAdapter(Context context, ProductSelectManager productSelectManager) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.productSelectManager = productSelectManager;
        if (productSelectManager != null) {
            productSelectManager.subscribe(new ProductSelectManager.ProductSelectCallback() {
                @Override
                public void updateNotify() {
                    notifyDataSetChanged();
                }
            });
        }
        this.rows = new ArrayList<>();
    }

    public void update(List<ProductEntity> rows) {
        this.rows.clear();
        if (rows != null) {
            this.rows.addAll(rows);
        }
        notifyDataSetChanged();
    }

    public void add(List<ProductEntity> rows) {
        if (rows != null) {
            this.rows.addAll(rows);
        }
        notifyDataSetChanged();
    }

    public void rest() {
        this.rows.clear();
        notifyDataSetChanged();
    }

    public List<ProductEntity> getData() {
        return rows;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.fragment_product_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        final ProductEntity productEntity = rows.get(position);
        holder.product_list_item_name_tv.setText(productEntity.getName());
        holder.product_list_item_label_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return TextUtils.isEmpty(productEntity.getCategory()) ? 0 : 1;
            }

            @Override
            public String getLabelText(int position) {
                return productEntity.getCategory();
            }

            @Override
            public Drawable getLabelBackgroundDrawable(int position) {
                GradientDrawable labelDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.round_label_drawable);
                String color = productEntity.getCategory_color();
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
        holder.product_list_item_price_tv.setText(productEntity.getPrice());
        if (productSelectManager != null && productSelectManager.getSelectProduct().contains(productEntity)) {
            holder.product_list_item_check_iv.setImageResource(R.drawable.check_icon);
        } else {
            holder.product_list_item_check_iv.setImageResource(R.drawable.unchecked_icon);
        }
        holder.product_list_item_check_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productSelectManager == null) {
                    return;
                }
                if (productSelectManager.getSelectProduct().contains(productEntity)) {
                    productSelectManager.getSelectProduct().remove(productEntity);
                } else {
                    productSelectManager.getSelectProduct().add(productEntity);
                }
                productSelectManager.updateObservers();
                notifyDataSetChanged();
            }
        });
        Object tag = holder.product_list_item_pic_rv.getTag();
        if (tag != null || tag instanceof FollowDetailActivity.AttachmentAdapter) {
            AttachmentAdapter attachmentAdapter = (AttachmentAdapter) tag;
            attachmentAdapter.update(productEntity.getAccessory());
        } else {
            final AttachmentAdapter attachmentAdapter = new AttachmentAdapter(context, productEntity.getAccessory());
            holder.product_list_item_pic_rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            holder.product_list_item_pic_rv.setAdapter(attachmentAdapter);
            holder.product_list_item_pic_rv.setTag(attachmentAdapter);
            final int offset = CommonUtils.dp2pxForInt(context, 8);
            holder.product_list_item_pic_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    int position = parent.getChildAdapterPosition(view);
                    if (position == attachmentAdapter.getItemCount() - 1) {
                        outRect.set(0, 0, 0, 0);
                    } else {
                        outRect.set(0, 0, offset, 0);
                    }
                }
            });
            attachmentAdapter.setOnItemClickListener(new AttachmentAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(List<String> pics, int postion) {
                    PhotoBrowserActivity.start(context, (ArrayList<String>) pics, postion);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView product_list_item_check_iv;
        private TextView product_list_item_name_tv;
        private FlowLayout product_list_item_label_fl;
        private TextView product_list_item_price_tv;
        private RecyclerView product_list_item_pic_rv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            product_list_item_check_iv = itemView.findViewById(R.id.product_list_item_check_iv);
            product_list_item_name_tv = itemView.findViewById(R.id.product_list_item_name_tv);
            product_list_item_label_fl = itemView.findViewById(R.id.product_list_item_label_fl);
            product_list_item_price_tv = itemView.findViewById(R.id.product_list_item_price_tv);
            product_list_item_pic_rv = itemView.findViewById(R.id.product_list_item_pic_rv);
        }
    }

    public void setListener(OnItemListener listener) {
        this.listener = listener;
    }

    public interface OnItemListener {
        void onClick(int position);
    }

    public static class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.Holder> {
        private Context context;
        private List<String> attachments;
        private LayoutInflater inflater;

        private AttachmentAdapter.OnItemClickListener listener;

        public AttachmentAdapter(Context context, List<String> attachments) {
            this.context = context;
            this.attachments = new ArrayList<>();
            this.attachments.addAll(attachments);
            this.inflater = LayoutInflater.from(context);
        }

        public void update(List<String> attachments) {
            this.attachments.clear();
            if (attachments != null) {
                this.attachments.addAll(attachments);
            }
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(AttachmentAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public AttachmentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AttachmentAdapter.Holder(inflater.inflate(R.layout.activity_product_item_attachment_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AttachmentAdapter.Holder holder, final int position) {
            String attachment = attachments.get(position);
            GlideApp.with(context).load(attachment).transform(new RoundedCornersTransformation(CommonUtils.dp2px(context, 12), 1)).placeholder(R.drawable.image_load_round_default).into(holder.attachment_item_iv);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(attachments, position);
                }
            });
        }

        public class Holder extends RecyclerView.ViewHolder {
            private ImageView attachment_item_iv;

            public Holder(@NonNull View itemView) {
                super(itemView);
                attachment_item_iv = itemView.findViewById(R.id.attachment_item_iv);
            }
        }

        @Override
        public int getItemCount() {
            return attachments.size();
        }

        public interface OnItemClickListener {
            void OnItemClick(List<String> pics, int postion);
        }
    }
}
