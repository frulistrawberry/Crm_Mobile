package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.base.BaseLayoutParams;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.structure.CompanyEntity;
import com.baihe.lihepro.entity.structure.MemberEntity;
import com.baihe.lihepro.manager.StructureManager;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-05
 * Description：
 */
public class CustomerStructureActivity extends BaseActivity {
    public static final String INTENT_TITLE_NAME = "INTENT_TITLE_NAME";
    public static final String INTENT_UI_TYPE = "INTENT_UI_TYPE";

    public static final int UI_TYPE_STRUCTURE = 1;
    public static final int UI_TYPE_ROLE = 2;
    public static final int UI_TYPE_STRUCTURE_SELF = 3;

    public static void start(Context context, String titleName) {
        Intent intent = new Intent(context, CustomerStructureActivity.class);
        intent.putExtra(INTENT_TITLE_NAME, titleName);
        context.startActivity(intent);
    }

    private Toolbar customer_structure_title_tb;
    private TextView customer_structure_name_tv;
    private LinearLayout customer_structure_search_ll;
    private LinearLayout customer_structure_title_bottom_ll;

    private TextView customer_structure_company_name_tv;
    private LinearLayout customer_structure_by_structure_ll;
    private LinearLayout customer_structure_by_role_ll;
    private LinearLayout customer_structure_by_my_structure_ll;

    private TextView customer_structure_bottom_des_tv;
    private TextView customer_structure_bottom_ok_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_customer_structure_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_customer_structure, null), params);
        init();
        initData();
        loadData();
        listener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (StructureManager.newInstance().isInit()) {
            List<MemberEntity> selectMembers = StructureManager.newInstance().getSelectMemebers();
            customer_structure_bottom_des_tv.setText("已选：" + selectMembers.size() + "人");
        }
    }

    private void init() {
        customer_structure_title_tb = findViewById(R.id.customer_structure_title_tb);
        customer_structure_name_tv = findViewById(R.id.customer_structure_name_tv);
        customer_structure_name_tv.setText(getIntent().getStringExtra(INTENT_TITLE_NAME));
        customer_structure_search_ll = findViewById(R.id.customer_structure_search_ll);
        customer_structure_title_bottom_ll = findViewById(R.id.customer_structure_title_bottom_ll);

        customer_structure_company_name_tv = findViewById(R.id.customer_structure_company_name_tv);
        customer_structure_by_structure_ll = findViewById(R.id.customer_structure_by_structure_ll);
        customer_structure_by_role_ll = findViewById(R.id.customer_structure_by_role_ll);
        customer_structure_by_my_structure_ll = findViewById(R.id.customer_structure_by_my_structure_ll);

        customer_structure_bottom_des_tv = findViewById(R.id.customer_structure_bottom_des_tv);
        customer_structure_bottom_ok_tv = findViewById(R.id.customer_structure_bottom_ok_tv);
    }

    private void initData() {
        customer_structure_title_bottom_ll.setVisibility(View.GONE);
    }

    private void loadData() {
        HttpRequest.create(UrlConstant.GET_CANDIDATE_INIT_URL).get(new CallBack<CompanyEntity>() {
            @Override
            public CompanyEntity doInBackground(String response) {
                return JsonUtils.parse(response, CompanyEntity.class);
            }

            @Override
            public void success(CompanyEntity entity) {
                statusLayout.normalStatus();
                StructureManager.newInstance().init(entity);
                customer_structure_company_name_tv.setText(StructureManager.newInstance().getCompanyName());
                customer_structure_title_bottom_ll.setVisibility(View.VISIBLE);
            }

            @Override
            public void error() {
                statusLayout.netErrorStatus();
            }

            @Override
            public void fail() {
                statusLayout.netFailStatus();
            }

            @Override
            public void before() {
                super.before();
                statusLayout.loadingStatus();
            }
        });
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
                CustomerStructureSearchActivity.start(context, getIntent().getStringExtra(INTENT_TITLE_NAME));
            }
        });
        customer_structure_by_structure_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerStructureListActivity.start(context, getIntent().getStringExtra(INTENT_TITLE_NAME), UI_TYPE_STRUCTURE);
            }
        });
        customer_structure_by_role_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerStructureListActivity.start(context, getIntent().getStringExtra(INTENT_TITLE_NAME), UI_TYPE_ROLE);
            }
        });
        customer_structure_by_my_structure_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerStructureListActivity.start(context, getIntent().getStringExtra(INTENT_TITLE_NAME), UI_TYPE_STRUCTURE_SELF);
            }
        });
        customer_structure_bottom_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
