package com.baihe.lihepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseFragment;
import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.ButtonTypeEntity;
import com.baihe.lihepro.entity.ContactEntity;
import com.baihe.lihepro.entity.CustomerEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.utils.Utils;
import com.baihe.lihepro.view.FlowLayout;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-03
 * Description：
 */
public class CustomerDetailBaseAdapter extends RecyclerView.Adapter<CustomerDetailBaseAdapter.Holder> {
    private static final int HEADER_TYPE = 1;
    private static final int NORMAL_TYPE = 2;
    private static final int FOOTER_TYPE = 3;

    private Context context;
    private LayoutInflater inflater;
    private String customerId;
    private BaseFragment baseFragment;

    private List<ContactEntity> contactUserData;
    private List<KeyValueEntity> customerData;
    private CustomerEntity customer;
    private List<ButtonTypeEntity> buttonType;

    private OnItemClickListener listener;

    public CustomerDetailBaseAdapter(Context context, String customerId) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.customerId = customerId;
        this.contactUserData = new ArrayList<>();
    }

    public void setData(List<ContactEntity> contactUserData, List<KeyValueEntity> customerData, CustomerEntity customer, List<ButtonTypeEntity> buttonType) {
        this.contactUserData.clear();
        if (contactUserData != null) {
            this.contactUserData.addAll(contactUserData);
        }
        this.customerData = customerData;
        this.customer = customer;
        this.buttonType = buttonType;
        notifyDataSetChanged();
    }

    public void setBaseFragment(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }

    private void addContact(ContactEntity contactEntity) {
        this.contactUserData.add(contactEntity);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_TYPE;
        } else if (position == getItemCount() - 1) {
            return FOOTER_TYPE;
        } else {
            return NORMAL_TYPE;
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == HEADER_TYPE) {
            view = inflater.inflate(R.layout.fragment_customer_detail_base_header, parent, false);
        } else if (viewType == FOOTER_TYPE) {
            view = inflater.inflate(R.layout.fragment_customer_detail_base_footer, parent, false);
        } else {
            view = inflater.inflate(R.layout.fragment_customer_detail_base_item, parent, false);
        }
        return new Holder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (getItemViewType(position) == NORMAL_TYPE) {
            int index = position - 1;
            final ContactEntity contactEntity = contactUserData.get(index);
            holder.customer_item_name_tv.setText(contactEntity.getUserName());
            holder.customer_item_label_fl.setLabelAdapter(new FlowLayout.FlowLabelAdapter() {
                @Override
                public int getSize() {
                    return 1;
                }

                @Override
                public String getLabelText(int position) {
                    return contactEntity.getIdentityText();
                }

                @Override
                public Drawable getLabelBackgroundDrawable(int position) {
                    GradientDrawable labelDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.round_label_drawable);
                    String color = contactEntity.getIdentityColor();
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
            holder.customer_item_call_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (baseFragment != null) {
                        Utils.call(customerId, "", contactEntity.getContactId(), baseFragment);
                    }
                }
            });
            holder.customer_item_data_kvl.setData(contactEntity.getInfo());
        } else if (getItemViewType(position) == HEADER_TYPE) {
            if (customerData != null) {
                holder.customer_base_data_kvl.setData(customerData);
            }
            if (customer != null) {
                holder.customer_base_name_tv.setText(customer.getName());
                holder.customer_base_wedding_time_tv.setText(customer.getWedding_date().getKey() + "：" + (TextUtils.isEmpty(customer.getWedding_date().getVal()) ? "未填写" : customer.getWedding_date().getVal()));
            }
            holder.customer_base_edit_contact_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.editContact();
                    }
                }
            });
            if (buttonType != null && buttonType.size() > 0) {
                holder.customer_base_edit_ll.setVisibility(View.VISIBLE);
            } else {
                holder.customer_base_edit_ll.setVisibility(View.GONE);
            }
            holder.customer_base_edit_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.editCustomer();
                    }
                }
            });
            holder.customer_base_data_kvl.setOnCallListener(new KeyValueLayout.OnCallListener() {
                @Override
                public void call(KeyValueEntity keyValueEntity) {
                    if (baseFragment != null) {
                        Utils.call(customerId, "", baseFragment);
                    }
                }
            });
            holder.customer_base_data_kvl.setOnUnlockMobileListener(new KeyValueLayout.OnUnlockMobileListener() {
                @Override
                public void unLock(View parentView, TextView mobile, TextView unlock, ImageView lockIcon, KeyValueEntity keyValueEntity) {
                    if (baseFragment != null) {
                        Utils.unLockMobile(parentView, mobile, unlock, lockIcon, keyValueEntity, customerId, baseFragment);
                    }
                }
            });

            holder.customer_base_data_kvl.setOnUnlockWechatListener(new KeyValueLayout.OnUnlockWechatListener() {
                @Override
                public void unLock(View parentView, TextView mobile, TextView unlock, ImageView lockIcon, KeyValueEntity keyValueEntity) {
                    if (baseFragment != null) {
                        Utils.unLockWechat(parentView, mobile, unlock, lockIcon, keyValueEntity, customerId, baseFragment);
                    }
                }
            });

            if (contactUserData != null && contactUserData.size() > 0) {
                holder.customer_base_edit_contact_ll.setVisibility(View.VISIBLE);
            } else {
                holder.customer_base_edit_contact_ll.setVisibility(View.GONE);
            }
        } else if (getItemViewType(position) == FOOTER_TYPE) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.addContact();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return contactUserData.size() + 2;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView customer_item_name_tv;
        private FlowLayout customer_item_label_fl;
        private ImageView customer_item_call_iv;
        private KeyValueLayout customer_item_data_kvl;

        private TextView customer_base_name_tv;
        private LinearLayout customer_base_edit_ll;
        private TextView customer_base_wedding_time_tv;
        private KeyValueLayout customer_base_data_kvl;
        private LinearLayout customer_base_edit_contact_ll;

        private TextView customer_item_add_contact_tv;

        public Holder(@NonNull View itemView, int viewType) {
            super(itemView);
            if (viewType == NORMAL_TYPE) {
                customer_item_name_tv = itemView.findViewById(R.id.customer_item_name_tv);
                customer_item_label_fl = itemView.findViewById(R.id.customer_item_label_fl);
                customer_item_call_iv = itemView.findViewById(R.id.customer_item_call_iv);
                customer_item_data_kvl = itemView.findViewById(R.id.customer_item_data_kvl);
            } else if (viewType == HEADER_TYPE) {
                customer_base_name_tv = itemView.findViewById(R.id.customer_base_name_tv);
                customer_base_edit_ll = itemView.findViewById(R.id.customer_base_edit_ll);
                customer_base_wedding_time_tv = itemView.findViewById(R.id.customer_base_wedding_time_tv);
                customer_base_data_kvl = itemView.findViewById(R.id.customer_base_data_kvl);
                customer_base_edit_contact_ll = itemView.findViewById(R.id.customer_base_edit_contact_ll);
            } else if (viewType == FOOTER_TYPE) {
                customer_item_add_contact_tv = itemView.findViewById(R.id.customer_item_add_contact_tv);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void addContact();

        void editContact();

        void editCustomer();
    }
}
