package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.baihe.lihepro.adapter.StructureSearchAdapter;
import com.baihe.lihepro.entity.structure.MemberEntity;
import com.baihe.lihepro.manager.StructureManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-07
 * Description：
 */
public class CustomerStructureSearchActivity extends BaseActivity {

    public static void start(Context context, String titleName) {
        Intent intent = new Intent(context, CustomerStructureSearchActivity.class);
        intent.putExtra(CustomerStructureActivity.INTENT_TITLE_NAME, titleName);
        context.startActivity(intent);
    }

    private Toolbar customer_structure_title_tb;
    private TextView customer_structure_name_tv;
    private EditText customer_structure_search_et;
    private ImageView customer_structure_search_delete_iv;
    private RecyclerView customer_structure_search_rv;

    private TextView customer_structure_search_bottom_des_tv;
    private TextView customer_structure_search_bottom_ok_tv;

    private StructureSearchAdapter structureSearchAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_customer_structure_search_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_customer_structure_search, null), params);
        init();
        initData();
        listener();
    }

    private void init() {
        customer_structure_title_tb = findViewById(R.id.customer_structure_title_tb);
        customer_structure_name_tv = findViewById(R.id.customer_structure_name_tv);
        customer_structure_search_et = findViewById(R.id.customer_structure_search_et);
        customer_structure_search_delete_iv = findViewById(R.id.customer_structure_search_delete_iv);
        customer_structure_search_rv = findViewById(R.id.customer_structure_search_rv);

        customer_structure_search_bottom_des_tv = findViewById(R.id.customer_structure_search_bottom_des_tv);
        customer_structure_search_bottom_ok_tv = findViewById(R.id.customer_structure_search_bottom_ok_tv);
    }

    private void initData() {
        customer_structure_name_tv.setText(getIntent().getStringExtra(CustomerStructureActivity.INTENT_TITLE_NAME));

        structureSearchAdapter = new StructureSearchAdapter(context);
        customer_structure_search_rv.setAdapter(structureSearchAdapter);
        customer_structure_search_rv.setLayoutManager(new LinearLayoutManager(context));
        customer_structure_search_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0) {
                    outRect.set(0, CommonUtils.dp2pxForInt(context, 9), 0, CommonUtils.dp2pxForInt(context, -4));
                } else if (position == structureSearchAdapter.getItemCount() - 1) {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(context, 77));
                } else {
                    outRect.set(0, 0, 0, CommonUtils.dp2pxForInt(context, -4));
                }
            }
        });

        structureSearchAdapter.updateData(StructureManager.newInstance().getMembers());
        updateSelectText();
    }

    private void listener() {
        customer_structure_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customer_structure_search_bottom_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customer_structure_search_delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customer_structure_search_et.setText("");
            }
        });
        customer_structure_search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    customer_structure_search_delete_iv.setVisibility(View.VISIBLE);
                } else {
                    customer_structure_search_delete_iv.setVisibility(View.GONE);
                }
                fitler(s.toString().trim());
            }
        });
        structureSearchAdapter.setOnItemClickListener(new StructureSearchAdapter.OnItemClickListener() {
            @Override
            public void notifySelectText() {
                updateSelectText();
            }
        });
    }

    private void fitler(String keyWord) {
        if (TextUtils.isEmpty(keyWord)) {
            structureSearchAdapter.updateData(StructureManager.newInstance().getMembers());
        } else {
            List<MemberEntity> memberEntityList = new ArrayList<>();
            for (MemberEntity entity : StructureManager.newInstance().getMembers()) {
                if (entity.name.contains(keyWord)) {
                    memberEntityList.add(entity);
                }
            }
            structureSearchAdapter.updateData(memberEntityList);
        }
    }

    private void updateSelectText() {
        int selectCount = 0;
        for (MemberEntity entity : StructureManager.newInstance().getMembers()) {
            if (entity.isSelect()) {
                selectCount++;
            }
        }
        customer_structure_search_bottom_des_tv.setText("已选：" + selectCount + "人");
    }
}
