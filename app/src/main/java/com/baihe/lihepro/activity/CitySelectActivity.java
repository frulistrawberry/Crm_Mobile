package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.CityEntity;
import com.baihe.lihepro.filter.entity.FilterRegionEntity;
import com.baihe.lihepro.manager.CitySelectManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-11
 * Description：
 */
public class CitySelectActivity extends BaseActivity {
    public static final String INTENT_TITLE_NAME = "INTENT_TITLE_NAME";
    public static final String INTENT_SELECT_CODE = "INTENT_SELECT_CODE";
    private static final String INTENT_BIND_TAG = "INTENT_BIND_TAG";

    public static void start(Context context, String tag) {
        start(context, null, tag);
    }

    public static void start(Context context, String title, String tag) {
        start(context, title, null, tag);
    }

    public static void start(Context context, String title, String code, String tag) {
        Intent intent = new Intent(context, CitySelectActivity.class);
        if (!TextUtils.isEmpty(title)) {
            intent.putExtra(INTENT_TITLE_NAME, title);
        }
        if (!TextUtils.isEmpty(code)) {
            intent.putExtra(INTENT_SELECT_CODE, code);
        }
        intent.putExtra(INTENT_BIND_TAG, tag);
        context.startActivity(intent);
    }

    private String selectCode;
    private String bindTag;

    private Toolbar city_select_list_title_tb;
    private TextView city_select_list_title_name_tv;
    private ImageView city_select_list_search_delete_iv;
    private EditText city_select_list_search_et;
    private RecyclerView city_select_list_rv;
    private TextView city_select_ok_tv;

    private List<CityEntity> srcCityEntities = new ArrayList<>();
    private CitySelectAdapter citySelectAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectCode = getIntent().getStringExtra(INTENT_SELECT_CODE);
        bindTag = getIntent().getStringExtra(INTENT_BIND_TAG);
        setTitleView(R.layout.activity_city_select_title);
        BaseLayoutParams params = new BaseLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //图片阴影间隙13dp
        params.topMargin = CommonUtils.dp2pxForInt(context, -13);
        setContentView(LayoutInflater.from(context).inflate(R.layout.activity_city_select, null), params);
        init();
        initData();
        listener();
    }

    private void init() {
        city_select_list_title_tb = findViewById(R.id.city_select_list_title_tb);
        city_select_list_title_name_tv = findViewById(R.id.city_select_list_title_name_tv);
        city_select_list_search_delete_iv = findViewById(R.id.city_select_list_search_delete_iv);
        city_select_list_search_et = findViewById(R.id.city_select_list_search_et);
        city_select_list_rv = findViewById(R.id.city_select_list_rv);
        city_select_ok_tv = findViewById(R.id.city_select_ok_tv);

        String title = getIntent().getStringExtra(INTENT_TITLE_NAME);
        title = TextUtils.isEmpty(title) ? "选择城市" : title;
        city_select_list_title_name_tv.setText(title);
    }

    private void initData() {
        //解析城市
        try {
            InputStream in = context.getResources().getAssets().open("area.json");
            int available = in.available();
            byte[] b = new byte[available];
            in.read(b);
            String json = new String(b, "UTF-8");
            List<FilterRegionEntity> entities = JsonUtils.parseList(json, FilterRegionEntity.class);
            for (FilterRegionEntity entity : entities) {
                //直辖市和海外
                if ("110000".equals(entity.code) || "120000".equals(entity.code) || "310000".equals(entity.code) || "500000".equals(entity.code) || "900000".equals(entity.code)) {
                    CityEntity cityEntity = new CityEntity();
                    cityEntity.setCode(entity.code);
                    cityEntity.setName(entity.name);
                    if (entity.code.equals(selectCode)) {
                        cityEntity.setSelect(true);
                    }
                    srcCityEntities.add(cityEntity);
                } else if (entity.children != null) {
                    for (FilterRegionEntity childEntity : entity.children) {
                        CityEntity cityEntity = new CityEntity();
                        cityEntity.setCode(childEntity.code);
                        cityEntity.setName(childEntity.name);
                        if (childEntity.code.equals(selectCode)) {
                            cityEntity.setSelect(true);
                        }
                        srcCityEntities.add(cityEntity);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        citySelectAdapter = new CitySelectAdapter(context);
        city_select_list_rv.setAdapter(citySelectAdapter);
        city_select_list_rv.setLayoutManager(new LinearLayoutManager(context));

        citySelectAdapter.updateData(getCityList(""));
    }

    private List<CityEntity> getCityList(String keyword) {
        List<CityEntity> citys = new ArrayList<>();
        for (CityEntity cityEntity : srcCityEntities) {
            if (TextUtils.isEmpty(keyword)) {
                citys.add(cityEntity);
            } else if (cityEntity.getName().contains(keyword)) {
                citys.add(cityEntity);
            }
        }
        return citys;
    }

    private void listener() {
        city_select_list_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        city_select_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityEntity cityEntity = citySelectAdapter.getSelectCity();
                if (cityEntity != null) {
                    CitySelectManager.newInstance().notifyObservers(bindTag, cityEntity);
                    finish();
                } else {
                    ToastUtils.toast("请选择城市区域");
                }
            }
        });
        city_select_list_search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    city_select_list_search_delete_iv.setVisibility(View.GONE);
                } else {
                    city_select_list_search_delete_iv.setVisibility(View.VISIBLE);
                }
                citySelectAdapter.updateData(getCityList(keyword));
            }
        });
        city_select_list_search_delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city_select_list_search_et.setText("");
            }
        });
    }

    public static class CitySelectAdapter extends RecyclerView.Adapter<CitySelectAdapter.Holder> {
        private final static int TOP_TYPE = 1;
        private final static int MIDDLE_TYPE = 2;
        private final static int BOTTOM_TYPE = 3;
        private final static int ONLY_TYPE = 4;

        private Context context;
        private LayoutInflater inflater;
        private List<CityEntity> cityEntities;

        public CitySelectAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.cityEntities = new ArrayList<>();
        }

        public void updateData(List<CityEntity> cityEntities) {
            this.cityEntities.clear();
            if (cityEntities != null) {
                this.cityEntities.addAll(cityEntities);
            }
            notifyDataSetChanged();
        }

        public CityEntity getSelectCity() {
            for (CityEntity cityEntity : cityEntities) {
                if (cityEntity.isSelect()) {
                    return cityEntity;
                }
            }
            return null;
        }

        private void restSelectStatus() {
            for (CityEntity cityEntity : cityEntities) {
                cityEntity.setSelect(false);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (getItemCount() == 1) {
                return ONLY_TYPE;
            } else if (position == 0) {
                return TOP_TYPE;
            } else if (position == getItemCount() - 1) {
                return BOTTOM_TYPE;
            } else {
                return MIDDLE_TYPE;
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ONLY_TYPE) {
                return new CitySelectAdapter.Holder(inflater.inflate(R.layout.activity_city_select_item_only, parent, false));
            } else if (viewType == TOP_TYPE) {
                return new CitySelectAdapter.Holder(inflater.inflate(R.layout.activity_city_select_item_top, parent, false));
            } else if (viewType == BOTTOM_TYPE) {
                return new CitySelectAdapter.Holder(inflater.inflate(R.layout.activity_city_select_item_bottom, parent, false));
            } else {
                return new CitySelectAdapter.Holder(inflater.inflate(R.layout.activity_city_select_item, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, int position) {
            int itemOffset = CommonUtils.dp2pxForInt(context, 8);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.city_select_item_ll.getLayoutParams();
            if (position == 0) {
                params.topMargin = itemOffset;
                params.bottomMargin = 0;
            } else if (position == getItemCount() - 1) {
                params.bottomMargin = 0;
                params.bottomMargin = itemOffset;
            } else {
                params.bottomMargin = 0;
                params.bottomMargin = 0;
            }

            final CityEntity cityEntity = cityEntities.get(position);
            if (cityEntity.isSelect()) {
                holder.city_select_item_select_iv.setImageResource(R.drawable.check_icon);
            } else {
                holder.city_select_item_select_iv.setImageResource(R.drawable.unchecked_icon);
            }
            holder.city_select_item_name_tv.setText(cityEntity.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cityEntity.isSelect()) {
                        cityEntity.setSelect(false);
                    } else {
                        restSelectStatus();
                        cityEntity.setSelect(true);
                    }
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return cityEntities.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            public LinearLayout city_select_item_ll;
            public TextView city_select_item_name_tv;
            public ImageView city_select_item_select_iv;

            public Holder(@NonNull View itemView) {
                super(itemView);
                city_select_item_ll = itemView.findViewById(R.id.city_select_item_ll);
                city_select_item_name_tv = itemView.findViewById(R.id.city_select_item_name_tv);
                city_select_item_select_iv = itemView.findViewById(R.id.city_select_item_select_iv);
            }
        }
    }
}
