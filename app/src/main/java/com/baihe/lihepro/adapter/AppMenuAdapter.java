package com.baihe.lihepro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.MenuEntity;
import com.baihe.lihepro.manager.AccountManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class AppMenuAdapter extends RecyclerView.Adapter<AppMenuAdapter.Holder> {
    //客户
    public static final String MENU_CUSTOMER_CODE = "Tigongrenliebiao";
    //邀约
    public static final String MENU_REQUIREMENT_CODE = "Yaoyueliebiao";
    //销售
    public static final String MENU_SALES_CODE = "Dingdan";
    //审批
    public static final String MENU_APPROVE_CODE = "Shenheguanli";
    //合同
    public static final String MENU_CONTRACT_CODE = "Hetong";
    //档期
    public static final String MENU_SCHEDULE_CODE = "DangQi";
    public static final String MENU_SCHEDULE_CODE_NEW = "DangQi1";
    //案例库
    public static final String MENU_ANLIKU_CODE = "Anliku";
    //付款码
    public static final String MENU_PAY_CODE = "PayCode";
    //客服
    public static final String MENU_KEFU = "Kefu";


    private Context context;
    private LayoutInflater inflater;
    private List<MenuEntity> menuEntities;

    private OnItemClickListener listener;

    public AppMenuAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.menuEntities = new ArrayList<>();
    }

    public void setData(List<MenuEntity> entities) {
        this.menuEntities.clear();
        this.menuEntities.addAll(entities);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.fragment_home_menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final MenuEntity menuEntity = menuEntities.get(position);
        holder.home_app_menu_item_name_tv.setText(menuEntity.getName());
        if (MENU_CUSTOMER_CODE.equals(menuEntity.getCode())) {
            holder.home_app_menu_item_icon_iv.setImageResource(R.drawable.home_customer);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(MENU_CUSTOMER_CODE);
                    }
                }
            });
        } else if (MENU_REQUIREMENT_CODE.equals(menuEntity.getCode())) {
            holder.home_app_menu_item_icon_iv.setImageResource(R.drawable.home_requirement);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(MENU_REQUIREMENT_CODE);
                    }
                }
            });
        } else if (MENU_SALES_CODE.equals(menuEntity.getCode())) {
            holder.home_app_menu_item_icon_iv.setImageResource(R.drawable.home_sales);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(MENU_SALES_CODE);
                    }
                }
            });
        } else if (MENU_APPROVE_CODE.equals(menuEntity.getCode())) {
            holder.home_app_menu_item_icon_iv.setImageResource(R.drawable.home_approve);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(MENU_APPROVE_CODE);
                    }
                }
            });
        } else if (MENU_CONTRACT_CODE.equals(menuEntity.getCode())) {
            holder.home_app_menu_item_icon_iv.setImageResource(R.drawable.home_contract2);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(MENU_CONTRACT_CODE);
                    }
                }
            });
        }else if (MENU_SCHEDULE_CODE.equals(menuEntity.getCode())) {
            holder.home_app_menu_item_icon_iv.setImageResource(R.drawable.home_schedule);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(MENU_SCHEDULE_CODE);
                    }
                }
            });
        }else if (MENU_SCHEDULE_CODE_NEW.equals(menuEntity.getCode())) {
            holder.home_app_menu_item_icon_iv.setImageResource(R.drawable.icon_schedule);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(MENU_SCHEDULE_CODE_NEW);
                    }
                }
            });
        }

        else if (MENU_KEFU.equals(menuEntity.getCode())) {
            holder.home_app_menu_item_icon_iv.setImageResource(R.drawable.kefu);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(MENU_KEFU);
                    }
                }
            });
        }
        else if (MENU_ANLIKU_CODE.equals(menuEntity.getCode())){
            holder.home_app_menu_item_icon_iv.setImageResource(R.drawable.icon_anliku);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(MENU_ANLIKU_CODE);
                    }
                }
            });
        }else if (MENU_PAY_CODE.equals(menuEntity.getCode())){
            holder.home_app_menu_item_icon_iv.setImageResource(R.drawable.icon_pay_code);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(MENU_PAY_CODE);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return menuEntities.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView home_app_menu_item_icon_iv;
        private TextView home_app_menu_item_name_tv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            home_app_menu_item_icon_iv = itemView.findViewById(R.id.home_app_menu_item_icon_iv);
            home_app_menu_item_name_tv = itemView.findViewById(R.id.home_app_menu_item_name_tv);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String menuCode);
    }

    /**
     * 过滤数据
     *
     * @param entities
     */
    public List<MenuEntity> filterData(List<MenuEntity> entities) {
        List<MenuEntity> list = new ArrayList<>();
        for (MenuEntity entity : entities) {
            //可以显示并且预埋
            if (entity.getIs_show() == 0 && isEmbeddedMenu(entity)) {
                list.add(entity);
            }
        }
        //排序
        Collections.sort(list, new Comparator<MenuEntity>() {
            @Override
            public int compare(MenuEntity o1, MenuEntity o2) {
                int diff = o1.getSort() - o1.getSort();
                if (diff < 0) {
                    return 1;
                } else if (diff > 0) {
                    return -1;
                }
                return 0;
            }
        });
        return list;
    }

    private boolean isEmbeddedMenu(MenuEntity entity) {
        if (MENU_CUSTOMER_CODE.equals(entity.getCode()) || MENU_REQUIREMENT_CODE.equals(entity.getCode())
                || MENU_SALES_CODE.equals(entity.getCode()) || MENU_APPROVE_CODE.equals(entity.getCode())
                || MENU_CONTRACT_CODE.equals(entity.getCode()) || MENU_SCHEDULE_CODE.equals(entity.getCode())
                || MENU_ANLIKU_CODE.equals(entity.getCode())
                || MENU_PAY_CODE.equals(entity.getCode())
                || MENU_KEFU.equals(entity.getCode())
                || MENU_SCHEDULE_CODE_NEW.equals(entity.getCode())) {
            return true;
        }
        return false;
    }
}
