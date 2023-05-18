package com.baihe.lihepro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.structure.MemberEntity;
import com.baihe.lihepro.entity.structure.RoleEntity;
import com.baihe.lihepro.entity.structure.StructureBaseEntity;
import com.baihe.lihepro.entity.structure.StructureEntity;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-07
 * Description：
 */
public class StructureListAdapter extends RecyclerView.Adapter<StructureListAdapter.Holder> {
    private static final int ITEM_TYPE_NORMAL = 1;
    private static final int ITEM_TYPE_USER = 2;

    private List<StructureBaseEntity> dataList = new ArrayList<>();
    private LayoutInflater inflater;

    public OnItemClickListener listener;

    public StructureListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void updateData(List<StructureBaseEntity> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_NORMAL;
        } else {
            int index = position - 1;
            StructureBaseEntity structureBaseEntity = dataList.get(index);
            StructureBaseEntity.Type type = structureBaseEntity.getType();
            if (type == StructureBaseEntity.Type.USER) {
                return ITEM_TYPE_USER;
            } else {
                return ITEM_TYPE_NORMAL;
            }
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holder holder;
        if (viewType == ITEM_TYPE_NORMAL) {
            holder = new Holder(inflater.inflate(R.layout.activity_customer_structure_list_item, parent, false), viewType);
        } else {
            holder = new Holder(inflater.inflate(R.layout.activity_customer_structure_list_item_user, parent, false), viewType);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        if (position == 0) {
            if (isSelectAll()) {
                holder.customer_structure_list_item_select_iv.setImageResource(R.drawable.check_icon);
            } else {
                holder.customer_structure_list_item_select_iv.setImageResource(R.drawable.unchecked_icon);
            }
            holder.customer_structure_list_item_name_tv.setText("全选");
            holder.customer_structure_list_item_right_iv.setVisibility(View.INVISIBLE);
            holder.customer_structure_list_item_select_num_tv.setVisibility(View.INVISIBLE);
            holder.customer_structure_list_item_parent_ll.setOnClickListener(null);
            holder.customer_structure_list_item_select_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSelectAll()) {
                        selectAll(false);
                    } else {
                        selectAll(true);
                    }
                    notifyDataSetChanged();
                    if (listener != null) {
                        listener.notifySelectText();
                    }
                }
            });
        } else {
            int itemType = getItemViewType(position);
            int index = position - 1;
            final StructureBaseEntity structureBaseEntity = dataList.get(index);
            int childCount;
            if (itemType == ITEM_TYPE_NORMAL) {
                if (structureBaseEntity.isSelect()) {
                    holder.customer_structure_list_item_select_iv.setImageResource(R.drawable.check_icon);
                } else {
                    holder.customer_structure_list_item_select_iv.setImageResource(R.drawable.unchecked_icon);
                }
                if (structureBaseEntity.getType() == StructureBaseEntity.Type.STRUCTURE) {
                    StructureEntity structureEntity = (StructureEntity) structureBaseEntity;
                    childCount = getMemberNum(structureEntity);
                    holder.customer_structure_list_item_name_tv.setText(structureEntity.getName() + (childCount > 0 ? ("（" + childCount + "）") : ""));
                } else {
                    RoleEntity roleEntity = (RoleEntity) structureBaseEntity;
                    childCount = getMemberNum(roleEntity);
                    holder.customer_structure_list_item_name_tv.setText(roleEntity.getName() + (childCount > 0 ? ("（" + childCount + "）") : ""));
                }
                int selectChildCount = getSelectMemberNum(structureBaseEntity);
                holder.customer_structure_list_item_select_num_tv.setText("已选" + selectChildCount + "人");
                if (selectChildCount > 0) {
                    holder.customer_structure_list_item_select_num_tv.setVisibility(View.VISIBLE);
                } else {
                    holder.customer_structure_list_item_select_num_tv.setVisibility(View.INVISIBLE);
                }
                if (childCount > 0) {
                    holder.customer_structure_list_item_right_iv.setVisibility(View.VISIBLE);
                } else {
                    holder.customer_structure_list_item_right_iv.setVisibility(View.INVISIBLE);
                }
                final int finalChildCount = childCount;
                holder.customer_structure_list_item_parent_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null && finalChildCount > 0) {
                            listener.onItemClick(structureBaseEntity);
                        }
                    }
                });

                //选择逻辑
                holder.customer_structure_list_item_select_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (structureBaseEntity.isSelect()) {
                            structureBaseEntity.setSelect(false);
                        } else {
                            structureBaseEntity.setSelect(true);
                        }
                        notifyDataSetChanged();
                        if (listener != null) {
                            listener.notifySelectText();
                        }
                    }
                });
            } else {
                if (structureBaseEntity.isSelect()) {
                    holder.customer_structure_list_item_user_select_iv.setImageResource(R.drawable.check_icon);
                } else {
                    holder.customer_structure_list_item_user_select_iv.setImageResource(R.drawable.unchecked_icon);
                }
                MemberEntity memberEntity = (MemberEntity) structureBaseEntity;
                holder.customer_structure_list_item_user_name_tv.setText(memberEntity.getName());
                holder.customer_structure_list_item_user_data_kvl.setData(memberEntity.getOther());

                //选择逻辑
                holder.customer_structure_list_item_user_select_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (structureBaseEntity.isSelect()) {
                            structureBaseEntity.setSelect(false);
                        } else {
                            structureBaseEntity.setSelect(true);
                        }
                        notifyDataSetChanged();
                        if (listener != null) {
                            listener.notifySelectText();
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    private void selectAll(boolean isSelect) {
        for (StructureBaseEntity structureBaseEntity : dataList) {
            structureBaseEntity.setSelect(isSelect);
        }
    }

    private boolean isSelectAll() {
        for (StructureBaseEntity structureBaseEntity : dataList) {
            if (!structureBaseEntity.isSelect()) {
                return false;
            }
        }
        return true;
    }

    private int getMemberNum(StructureBaseEntity structureBaseEntity) {
        if (structureBaseEntity.getType() == StructureBaseEntity.Type.STRUCTURE) {
            StructureEntity structureEntity = (StructureEntity) structureBaseEntity;
            int memberCount = 0;
            if (structureEntity.getChildlist() != null) {
                for (StructureEntity child : structureEntity.getChildlist()) {
                    memberCount += getMemberNum(child);
                }
            }
            if (structureEntity.getUserList() != null) {
                memberCount += structureEntity.getUserList().size();
            }
            return memberCount;
        } else if (structureBaseEntity.getType() == StructureBaseEntity.Type.ROLE) {
            RoleEntity roleEntity = (RoleEntity) structureBaseEntity;
            int memberCount = 0;
            if (roleEntity.getUserList() != null) {
                memberCount += roleEntity.getUserList().size();
            }
            return memberCount;
        } else {
            return 1;
        }
    }

    public int getSelectMemberNum(StructureBaseEntity structureBaseEntity) {
        if (structureBaseEntity.getType() == StructureBaseEntity.Type.STRUCTURE) {
            StructureEntity structureEntity = (StructureEntity) structureBaseEntity;
            int memberCount = 0;
            if (structureEntity.getChildlist() != null) {
                for (StructureEntity child : structureEntity.getChildlist()) {
                    memberCount += getSelectMemberNum(child);
                }
            }
            if (structureEntity.getUserList() != null) {
                for (MemberEntity child : structureEntity.getUserList()) {
                    if (child.isSelect()) {
                        memberCount += 1;
                    }
                }
            }
            return memberCount;
        } else if (structureBaseEntity.getType() == StructureBaseEntity.Type.ROLE) {
            RoleEntity roleEntity = (RoleEntity) structureBaseEntity;
            int memberCount = 0;
            if (roleEntity.getUserList() != null) {
                for (MemberEntity child : roleEntity.getUserList()) {
                    if (child.isSelect()) {
                        memberCount += 1;
                    }
                }
            }
            return memberCount;
        } else {
            if (structureBaseEntity.isSelect()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        public ImageView customer_structure_list_item_select_iv;
        public TextView customer_structure_list_item_name_tv;
        public TextView customer_structure_list_item_select_num_tv;
        private ImageView customer_structure_list_item_right_iv;
        private LinearLayout customer_structure_list_item_parent_ll;

        public ImageView customer_structure_list_item_user_select_iv;
        public TextView customer_structure_list_item_user_name_tv;
        public KeyValueLayout customer_structure_list_item_user_data_kvl;


        public Holder(@NonNull View itemView, int itemType) {
            super(itemView);
            if (itemType == ITEM_TYPE_NORMAL) {
                customer_structure_list_item_select_iv = itemView.findViewById(R.id.customer_structure_list_item_select_iv);
                customer_structure_list_item_name_tv = itemView.findViewById(R.id.customer_structure_list_item_name_tv);
                customer_structure_list_item_select_num_tv = itemView.findViewById(R.id.customer_structure_list_item_select_num_tv);
                customer_structure_list_item_right_iv = itemView.findViewById(R.id.customer_structure_list_item_right_iv);
                customer_structure_list_item_parent_ll = itemView.findViewById(R.id.customer_structure_list_item_parent_ll);
            } else if (itemType == ITEM_TYPE_USER) {
                customer_structure_list_item_user_select_iv = itemView.findViewById(R.id.customer_structure_list_item_user_select_iv);
                customer_structure_list_item_user_name_tv = itemView.findViewById(R.id.customer_structure_list_item_user_name_tv);
                customer_structure_list_item_user_data_kvl = itemView.findViewById(R.id.customer_structure_list_item_user_data_kvl);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(StructureBaseEntity structureBaseEntity);

        void notifySelectText();
    }
}
