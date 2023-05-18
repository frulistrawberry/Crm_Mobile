package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.base.BaseLayoutParams;
import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.StructureListAdapter;
import com.baihe.lihepro.adapter.StructureTabAdapter;
import com.baihe.lihepro.entity.structure.RoleEntity;
import com.baihe.lihepro.entity.structure.StructureBaseEntity;
import com.baihe.lihepro.entity.structure.StructureEntity;
import com.baihe.lihepro.manager.StructureManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-07
 * Description：
 */
public class CustomerStructureListActivity extends BaseActivity {

    public static void start(Context context, String titleName, int uiType) {
        Intent intent = new Intent(context, CustomerStructureListActivity.class);
        intent.putExtra(CustomerStructureActivity.INTENT_TITLE_NAME, titleName);
        intent.putExtra(CustomerStructureActivity.INTENT_UI_TYPE, uiType);
        context.startActivity(intent);
    }

    private Toolbar customer_structure_title_tb;
    private TextView customer_structure_name_tv;
    private LinearLayout customer_structure_search_ll;

    private TextView customer_structure_list_type_name_tv;
    private RecyclerView customer_structure_list_tb_rv;
    private RecyclerView customer_structure_list_rv;

    private TextView customer_structure_list_bottom_des_tv;
    private TextView customer_structure_list_bottom_ok_tv;

    private int uiType;
    private StructureManager structureManager;
    private StructureTabAdapter structureTabAdapter;
    private StructureListAdapter structureListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_customer_structure_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_customer_structure_list, null), params);
        uiType = getIntent().getIntExtra(CustomerStructureActivity.INTENT_UI_TYPE, CustomerStructureActivity.UI_TYPE_STRUCTURE);
        init();
        initData();
        listener();
    }

    private void init() {
        customer_structure_title_tb = findViewById(R.id.customer_structure_title_tb);
        customer_structure_name_tv = findViewById(R.id.customer_structure_name_tv);
        customer_structure_search_ll = findViewById(R.id.customer_structure_search_ll);

        customer_structure_list_type_name_tv = findViewById(R.id.customer_structure_list_type_name_tv);
        customer_structure_list_tb_rv = findViewById(R.id.customer_structure_list_tb_rv);
        customer_structure_list_rv = findViewById(R.id.customer_structure_list_rv);

        customer_structure_list_bottom_des_tv = findViewById(R.id.customer_structure_list_bottom_des_tv);
        customer_structure_list_bottom_ok_tv = findViewById(R.id.customer_structure_list_bottom_ok_tv);
    }

    private void initData() {
        structureManager = StructureManager.newInstance();
        customer_structure_name_tv.setText(getIntent().getStringExtra(CustomerStructureActivity.INTENT_TITLE_NAME));

        structureTabAdapter = new StructureTabAdapter(context, structureManager.getCompanyName());
        customer_structure_list_tb_rv.setAdapter(structureTabAdapter);
        customer_structure_list_tb_rv.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

        structureListAdapter = new StructureListAdapter(context);
        customer_structure_list_rv.setAdapter(structureListAdapter);
        customer_structure_list_rv.setLayoutManager(new LinearLayoutManager(context));
        customer_structure_list_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.set(0, CommonUtils.dp2pxForInt(context, 1), 0, CommonUtils.dp2pxForInt(context, -4));
                } else if (position == structureListAdapter.getItemCount() - 1) {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(context, 77));
                } else {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(context, -4));
                }
            }
        });

        //刷新列表数据
        if (uiType == CustomerStructureActivity.UI_TYPE_ROLE) {
            customer_structure_list_type_name_tv.setText("按角色");
            updateDataList(null);
        } else if (uiType == CustomerStructureActivity.UI_TYPE_STRUCTURE_SELF) {
            customer_structure_list_type_name_tv.setText("我所属部门");
            updateDataList(null);
        } else {
            customer_structure_list_type_name_tv.setText("按组织架构");
            updateDataList(null);
        }

    }

    private void listener() {
        customer_structure_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customer_structure_search_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerStructureSearchActivity.start(context, getIntent().getStringExtra(CustomerStructureActivity.INTENT_TITLE_NAME));
            }
        });
        structureTabAdapter.setOnItemClickListener(new StructureTabAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, StructureBaseEntity structureBaseEntity) {
                forwardTab(position);
                updateDataList(structureBaseEntity);
            }
        });
        structureListAdapter.setOnItemClickListener(new StructureListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(StructureBaseEntity structureBaseEntity) {
                backTab(structureBaseEntity);
                updateDataList(structureBaseEntity);
            }

            @Override
            public void notifySelectText() {
                updateSelectText(structureTabAdapter.getCurrentTab());
            }
        });
        customer_structure_list_bottom_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 导航向前
     *
     * @param position
     */
    private void forwardTab(int position) {
        structureTabAdapter.selectPosition(position);
        customer_structure_list_tb_rv.scrollToPosition(position);
    }

    /**
     * 导航向后
     *
     * @param structureBaseEntity
     */
    private void backTab(StructureBaseEntity structureBaseEntity) {
        structureTabAdapter.add(structureBaseEntity);
        customer_structure_list_tb_rv.scrollToPosition(structureTabAdapter.getItemCount() - 1);
    }

    private void updateDataList(StructureBaseEntity structureBaseEntity) {
        List<StructureBaseEntity> dataList = new ArrayList<>();
        if (structureBaseEntity == null) {
            if (uiType == CustomerStructureActivity.UI_TYPE_ROLE) {
                dataList.addAll(structureManager.getRoles());
            } else if (uiType == CustomerStructureActivity.UI_TYPE_STRUCTURE_SELF) {
                dataList.addAll(structureManager.getStructureSelf());
            } else {
                dataList.addAll(structureManager.getStructures());
            }
        } else {
            StructureBaseEntity.Type type = structureBaseEntity.getType();
            if (type == StructureBaseEntity.Type.STRUCTURE) {
                StructureEntity structureEntity = (StructureEntity) structureBaseEntity;
                if (structureEntity.getChildlist() != null) {
                    dataList.addAll(structureEntity.getChildlist());
                }
                if (structureEntity.getUserList() != null) {
                    dataList.addAll(structureEntity.getUserList());
                }
            } else if (type == StructureBaseEntity.Type.ROLE) {
                RoleEntity roleEntity = (RoleEntity) structureBaseEntity;
                if (roleEntity.getUserList() != null) {
                    dataList.addAll(roleEntity.getUserList());
                }
            }
        }
        structureListAdapter.updateData(dataList);

        updateSelectText(structureBaseEntity);
    }

    private void updateSelectText(StructureBaseEntity structureBaseEntity) {
        if (structureBaseEntity == null) {
            if (uiType == CustomerStructureActivity.UI_TYPE_ROLE) {
                int childCount = 0;
                int roleCount = 0;
                for (StructureBaseEntity entity : structureManager.getRoles()) {
                    childCount += structureListAdapter.getSelectMemberNum(entity);
                    if (StructureBaseEntity.Type.ROLE == entity.getType()) {
                        RoleEntity roleEntity = (RoleEntity) entity;
                        if (roleEntity.isSelect()) {
                            roleCount++;
                        }
                    }
                }
                if (roleCount > 0) {
                    customer_structure_list_bottom_des_tv.setText("已选：" + roleCount + "个角色，" + childCount + "人");
                } else {
                    customer_structure_list_bottom_des_tv.setText("已选：" + childCount + "人");
                }
            } else if (uiType == CustomerStructureActivity.UI_TYPE_STRUCTURE_SELF) {
                int childCount = 0;
                int structureCount = 0;
                for (StructureBaseEntity entity : structureManager.getStructureSelf()) {
                    childCount += structureListAdapter.getSelectMemberNum(entity);
                    if (StructureBaseEntity.Type.STRUCTURE == entity.getType()) {
                        StructureEntity structureEntity = (StructureEntity) entity;
                        if (structureEntity.isSelect()) {
                            structureCount++;
                        }
                    }
                }
                if (structureCount > 0) {
                    customer_structure_list_bottom_des_tv.setText("已选：" + structureCount + "个部门，" + childCount + "人");
                } else {
                    customer_structure_list_bottom_des_tv.setText("已选：" + childCount + "人");
                }
            } else {
                int childCount = 0;
                int structureCount = 0;
                for (StructureBaseEntity entity : structureManager.getStructures()) {
                    childCount += structureListAdapter.getSelectMemberNum(entity);
                    if (StructureBaseEntity.Type.STRUCTURE == entity.getType()) {
                        StructureEntity structureEntity = (StructureEntity) entity;
                        if (structureEntity.isSelect()) {
                            structureCount++;
                        }
                    }
                }
                if (structureCount > 0) {
                    customer_structure_list_bottom_des_tv.setText("已选：" + structureCount + "个部门，" + childCount + "人");
                } else {
                    customer_structure_list_bottom_des_tv.setText("已选：" + childCount + "人");
                }
            }
        } else {
            int childCount = structureListAdapter.getSelectMemberNum(structureBaseEntity);
            StructureBaseEntity.Type type = structureBaseEntity.getType();
            if (type == StructureBaseEntity.Type.STRUCTURE) {
                StructureEntity structureEntity = (StructureEntity) structureBaseEntity;
                int structureCount = 0;
                if (structureEntity.getChildlist() != null) {
                    for (StructureEntity entity : structureEntity.getChildlist()) {
                        if (entity.isSelect()) {
                            structureCount++;
                        }
                    }
                }
                if (structureCount > 0) {
                    customer_structure_list_bottom_des_tv.setText("已选：" + structureCount + "个部门，" + childCount + "人");
                } else {
                    customer_structure_list_bottom_des_tv.setText("已选：" + childCount + "人");
                }
            } else if (type == StructureBaseEntity.Type.ROLE) {
                customer_structure_list_bottom_des_tv.setText("已选：" + childCount + "人");
            } else {
                customer_structure_list_bottom_des_tv.setText("已选：" + childCount + "人");
            }
        }
    }
}
