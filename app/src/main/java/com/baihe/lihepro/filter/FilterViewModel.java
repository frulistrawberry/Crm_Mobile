package com.baihe.lihepro.filter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.dialog.DateDialogUtils;
import com.baihe.lihepro.entity.CityEntity;
import com.baihe.lihepro.filter.adapter.SelectCity2Adapter;
import com.baihe.lihepro.filter.adapter.SelectCityAdapter;
import com.baihe.lihepro.filter.adapter.SelectMultilevelAdapter;
import com.baihe.lihepro.filter.entity.FilterEntity;
import com.baihe.lihepro.filter.entity.FilterKVEntity;
import com.baihe.lihepro.filter.entity.FilterRangeEntity;
import com.baihe.lihepro.filter.entity.FilterRegionEntity;
import com.baihe.lihepro.view.FlowFixedLayout;
import com.baihe.lihepro.view.LiheTimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-02-23
 * Description：
 */
public class FilterViewModel {
    /**
     * 城市选择,匹配到哪个城市字段就以哪个样式展示，把字段改对即可
     */
    public static final String TYPE_CITY = "city_old";
    /**
     * 新城市选择,匹配到哪个城市字段就以哪个样式展示，把字段改对即可
     */
    public static final String TYPE_CITY2 = "city";
    /**
     * 选择
     */
    public static final String TYPE_SELECT = "select";
    /**
     * singleInput 只有单选
     */
    public static final String TYPE_SINGLE_INPUT = "singleInput";
    /**
     * doubleInput 只有单选
     */
    public static final String TYPE_DOUBLE_INPUT = "doubleInput";
    /**
     * timeRange
     */
    public static final String TYPE_TIME_RANGE = "timeRange";
    /**
     * multilevel
     */
    public static final String TYPE_MULTILEVEL = "multilevel";

    private FilterEntity filterEntity;
    private boolean isMultiple;
    private LinearLayout layout;
    private Context context;
    private LayoutInflater inflater;
    private int magin5;
    private int magin15;
    private int magin20;

    private FlowFixedLayout flowFixedLayout;
    private TextView filter_double_input_first_et;
    private TextView filter_double_input_second_et;
    private EditText filter_single_input_first_et;

    private List<String> defaultValueForLabel;
    private FilterRangeEntity defaultValueForTimeRange;
    private FilterRangeEntity defaultValueForDoubleInput;
    private String defaultValueForSingleInput;
    private List<FilterRegionEntity> defaultOrSaveValueForCity;
    private List<String> defaultOrSaveValueForMultilevel;
    private List<FilterRegionEntity> allRegionEntitis;
    private List<CityEntity> allCityEntities;
    private List<CityEntity> defaultOrSaveValueForCity2;

    private AddFilterPageListener addFilterPageListener;
    private MultilevelValueListener multilevelValueListener;
    private CityValueListener cityValueListener;
    private City2ValueListener city2ValueListener;

    public FilterViewModel(Context context, FilterEntity filterEntity, Object defaultValue) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.filterEntity = filterEntity;
        this.isMultiple = filterEntity.is_multiple.equals("1");
        this.magin5 = CommonUtils.dp2pxForInt(context, 5);
        this.magin15 = CommonUtils.dp2pxForInt(context, 15);
        this.magin20 = CommonUtils.dp2pxForInt(context, 20);
        //城市类型需要初始化区域信息
        if (TYPE_CITY.equals(filterEntity.type)) {
            initAreaData();
        }
        //城市类型需要初始化区域信息-新样式
        if (TYPE_CITY2.equals(filterEntity.type)) {
            initAreaData2();
        }
        filterDefaultValue(defaultValue);
        createView();
    }

    //初始区域
    private void initAreaData() {
        try {
            InputStream in = context.getResources().getAssets().open("area.json");
            int available = in.available();
            byte[] b = new byte[available];
            in.read(b);
            String json = new String(b, "UTF-8");
            List<FilterRegionEntity> entities = JsonUtils.parseList(json, FilterRegionEntity.class);
            allRegionEntitis = new ArrayList<>();
            for (FilterRegionEntity entity : entities) {
                FilterRegionEntity province = new FilterRegionEntity();
                province.code = entity.code;
                province.name = entity.name;
                province.children = new ArrayList<>();
                List<FilterRegionEntity> cityEntitis;
                if (entity.children != null) {
                    if (entity.children.size() == 1 && "市辖区".equals(entity.children.get(0).name)) {
                        cityEntitis = entity.children.get(0).children;
                    } else {
                        cityEntitis = entity.children;
                    }
                    for (FilterRegionEntity cityEntity : cityEntitis) {
                        FilterRegionEntity city = new FilterRegionEntity();
                        city.code = cityEntity.code;
                        city.name = cityEntity.name;
                        province.children.add(city);
                    }
                }
                allRegionEntitis.add(province);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //初始区域-新样式
    private void initAreaData2() {
        try {
            InputStream in = context.getResources().getAssets().open("area.json");
            int available = in.available();
            byte[] b = new byte[available];
            in.read(b);
            String json = new String(b, "UTF-8");
            List<FilterRegionEntity> entities = JsonUtils.parseList(json, FilterRegionEntity.class);
            allCityEntities = new ArrayList<>();
            for (FilterRegionEntity entity : entities) {
                //直辖市和海外
                if ("110000".equals(entity.code) || "120000".equals(entity.code) || "310000".equals(entity.code) || "500000".equals(entity.code) || "900000".equals(entity.code)) {
                    CityEntity cityEntity = new CityEntity();
                    cityEntity.setCode(entity.code);
                    cityEntity.setName(entity.name);
                    allCityEntities.add(cityEntity);
                } else if (entity.children != null) {
                    for (FilterRegionEntity childEntity : entity.children) {
                        CityEntity cityEntity = new CityEntity();
                        cityEntity.setCode(childEntity.code);
                        cityEntity.setName(childEntity.name);
                        allCityEntities.add(cityEntity);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 筛选默认值，分离出各个组件的值
     */
    private void filterDefaultValue(Object defaultValue) {
        if (defaultValue == null) {
            return;
        }
        //区域选择固定页不判断多选和单选
        if (TYPE_CITY.equals(filterEntity.type)) {
            List<String> codes = (List<String>) defaultValue;
            if (codes.size() == 0) {
                return;
            }
            defaultOrSaveValueForCity = new ArrayList<>();
            for (FilterRegionEntity filterRegionEntity : allRegionEntitis) {
                FilterRegionEntity province = new FilterRegionEntity();
                boolean isProvince = false;
                ArrayList<FilterRegionEntity> citys = new ArrayList<>();
                for (FilterRegionEntity cityFilterRegionEntity : filterRegionEntity.children) {
                    FilterRegionEntity city = new FilterRegionEntity();
                    city.name = cityFilterRegionEntity.name;
                    city.code = cityFilterRegionEntity.code;
                    if (codes.contains(cityFilterRegionEntity.code)) {
                        isProvince = true;
                        citys.add(city);
                    }
                }
                if (isProvince) {
                    province.name = filterRegionEntity.name;
                    province.code = filterRegionEntity.code;
                    province.children = citys;
                    defaultOrSaveValueForCity.add(province);
                }

            }
            return;
        } else if (TYPE_CITY2.equals(filterEntity.type)) {
            List<String> codes = (List<String>) defaultValue;
            if (codes.size() == 0) {
                return;
            }
            defaultOrSaveValueForCity2 = new ArrayList<>();
            for (CityEntity cityEntity : allCityEntities) {
                if (codes.contains(cityEntity.getCode())) {
                    defaultOrSaveValueForCity2.add(cityEntity);
                }
            }
            return;
        }
        if (isMultiple) { //多选
            List<Object> objects = (List<Object>) defaultValue;
            if (TYPE_SELECT.equals(filterEntity.type)) {
                defaultValueForLabel = new ArrayList<>();
                for (Object object : objects) {
                    defaultValueForLabel.add((String) object);
                }
            } else if (TYPE_TIME_RANGE.equals(filterEntity.type)) {
                defaultValueForLabel = new ArrayList<>();
                for (Object object : objects) {
                    if (object instanceof FilterRangeEntity) {
                        defaultValueForTimeRange = (FilterRangeEntity) object;
                    } else {
                        defaultValueForLabel.add((String) object);
                    }
                }
            } else if (TYPE_SINGLE_INPUT.equals(filterEntity.type)) {  //不存在多选

            } else if (TYPE_DOUBLE_INPUT.equals(filterEntity.type)) {  //不存在多选

            } else if (TYPE_CITY.equals(filterEntity.type)) {

            } else if (TYPE_CITY2.equals(filterEntity.type)) {

            } else if (TYPE_MULTILEVEL.equals(filterEntity.type)) {
                defaultOrSaveValueForMultilevel = new ArrayList<>();
                for (Object object : objects) {
                    defaultOrSaveValueForMultilevel.add((String) object);
                }
            }
        } else { //单选
            if (TYPE_SELECT.equals(filterEntity.type) || TYPE_TIME_RANGE.equals(filterEntity.type)) {
                if (defaultValue instanceof FilterRangeEntity) {
                    defaultValueForTimeRange = (FilterRangeEntity) defaultValue;
                } else {
                    defaultValueForLabel = new ArrayList<>();
                    defaultValueForLabel.add((String) defaultValue);
                }
            } else if (TYPE_SINGLE_INPUT.equals(filterEntity.type)) {
                defaultValueForSingleInput = (String) defaultValue;
            } else if (TYPE_DOUBLE_INPUT.equals(filterEntity.type)) {
                defaultValueForDoubleInput = (FilterRangeEntity) defaultValue;
            } else if (TYPE_CITY.equals(filterEntity.type)) {

            } else if (TYPE_CITY2.equals(filterEntity.type)) {

            } else if (TYPE_MULTILEVEL.equals(filterEntity.type)) {
                defaultOrSaveValueForMultilevel = new ArrayList<>();
                defaultOrSaveValueForMultilevel.add((String) defaultValue);
            }
        }
    }

    /**
     * 获取View
     *
     * @return
     */
    public View getView() {
        return layout;
    }

    /**
     * 获取值
     *
     * @return
     */
    public Map<String, Object> getValue() {
        Map<String, Object> mapValue = null;
        if (TYPE_SELECT.equals(filterEntity.type)) {
            if (flowFixedLayout != null && flowFixedLayout.getSelectLabelsIndex().size() > 0) {
                mapValue = new HashMap<>();
                if (isMultiple) {  //如果多选
                    List<String> value = new ArrayList<>();
                    for (int index : flowFixedLayout.getSelectLabelsIndex()) {
                        value.add(filterEntity.list.get(index).item_val);
                    }
                    mapValue.put(filterEntity.filter_key, value);
                } else {
                    int index = flowFixedLayout.getSelectLabelsIndex().get(0);
                    mapValue.put(filterEntity.filter_key, filterEntity.list.get(index).item_val);
                }
            }
        } else if (TYPE_TIME_RANGE.equals(filterEntity.type)) {
            String firstString = null;
            String sencondString = null;
            if (filter_double_input_first_et != null && filter_double_input_second_et != null) {
                firstString = filter_double_input_first_et.getText().toString().trim();
                sencondString = filter_double_input_second_et.getText().toString().trim();
            }
            if ((flowFixedLayout != null && flowFixedLayout.getSelectLabelsIndex().size() > 0) || (!TextUtils.isEmpty(firstString) && !TextUtils.isEmpty(sencondString))) {
                mapValue = new HashMap<>();
                if (isMultiple) {  //如果多选
                    if (flowFixedLayout != null && flowFixedLayout.getSelectLabelsIndex().size() > 0) {
                        List<String> value = new ArrayList<>();
                        for (int index : flowFixedLayout.getSelectLabelsIndex()) {
                            value.add(filterEntity.list.get(index).item_val);
                        }
                        mapValue.put(filterEntity.filter_key, value);
                    }
                    if (!TextUtils.isEmpty(firstString) && !TextUtils.isEmpty(sencondString)) {
                        FilterRangeEntity filterRangeEntity = new FilterRangeEntity();
                        filterRangeEntity.first = firstString;
                        filterRangeEntity.second = sencondString;
                        mapValue.put(filterEntity.filter_key, filterRangeEntity);
                    }
                } else {
                    if (flowFixedLayout != null && flowFixedLayout.getSelectLabelsIndex().size() > 0) {
                        int index = flowFixedLayout.getSelectLabelsIndex().get(0);
                        mapValue.put(filterEntity.filter_key, filterEntity.list.get(index).item_val);
                    } else if (!TextUtils.isEmpty(firstString) && !TextUtils.isEmpty(sencondString)) {
                        FilterRangeEntity filterRangeEntity = new FilterRangeEntity();
                        filterRangeEntity.first = firstString;
                        filterRangeEntity.second = sencondString;
                        mapValue.put(filterEntity.filter_key, filterRangeEntity);
                    }
                }
            }
        } else if (TYPE_SINGLE_INPUT.equals(filterEntity.type)) {
            String inputString = null;
            if (filter_single_input_first_et != null) {
                inputString = filter_single_input_first_et.getText().toString().trim();
            }
            if (!TextUtils.isEmpty(inputString)) {
                mapValue = new HashMap<>();
                mapValue.put(filterEntity.filter_key, inputString);
            }
        } else if (TYPE_DOUBLE_INPUT.equals(filterEntity.type)) {
            String firstString = null;
            String sencondString = null;
            if (filter_double_input_first_et != null && filter_double_input_second_et != null) {
                firstString = filter_double_input_first_et.getText().toString().trim();
                sencondString = filter_double_input_second_et.getText().toString().trim();
            }
            if ((!TextUtils.isEmpty(firstString) && !TextUtils.isEmpty(sencondString))) {
                mapValue = new HashMap<>();
                FilterRangeEntity filterRangeEntity = new FilterRangeEntity();
                filterRangeEntity.first = firstString;
                filterRangeEntity.second = sencondString;
                mapValue.put(filterEntity.filter_key, filterRangeEntity);
            }
        } else if (TYPE_CITY.equals(filterEntity.type)) {
            if (defaultOrSaveValueForCity != null && defaultOrSaveValueForCity.size() > 0) {
                List<String> codes = new ArrayList<>();
                for (FilterRegionEntity filterRegionEntity : defaultOrSaveValueForCity) {
                    for (FilterRegionEntity entity : filterRegionEntity.children) {
                        codes.add(entity.code);
                    }
                }
                mapValue = new HashMap<>();
                mapValue.put(filterEntity.filter_key, codes);
            }
        } else if (TYPE_CITY2.equals(filterEntity.type)) {
            if (defaultOrSaveValueForCity2 != null && defaultOrSaveValueForCity2.size() > 0) {
                List<String> codes = new ArrayList<>();
                for (CityEntity cityEntity : defaultOrSaveValueForCity2) {
                    codes.add(cityEntity.getCode());
                }
                mapValue = new HashMap<>();
                mapValue.put(filterEntity.filter_key, codes);
            }
        } else if (TYPE_MULTILEVEL.equals(filterEntity.type)) {
            if (defaultOrSaveValueForMultilevel != null && defaultOrSaveValueForMultilevel.size() > 0) {
                if (isMultiple) {
                    mapValue = new HashMap<>();
                    mapValue.put(filterEntity.filter_key, defaultOrSaveValueForMultilevel);
                } else {
                    mapValue = new HashMap<>();
                    mapValue.put(filterEntity.filter_key, defaultOrSaveValueForMultilevel.get(0));
                }
            }
        }
        return mapValue;
    }

    /**
     * 重制
     */
    public void rest() {
        if (TYPE_CITY.equals(filterEntity.type)) {
            setDefaultOrSaveValueForCity(new ArrayList<FilterRegionEntity>());
        }
        if (TYPE_CITY2.equals(filterEntity.type)) {
            setDefaultOrSaveValueForCity2(new ArrayList<CityEntity>());
        }
        if (TYPE_MULTILEVEL.equals(filterEntity.type)) {
            setDefaultOrSaveValueForMultilevel(new ArrayList<String>());
        }
        if (flowFixedLayout != null) {
            flowFixedLayout.clearSelectLabelStatus();
        }
        if (filter_double_input_first_et != null) {
            filter_double_input_first_et.setEnabled(true);
            filter_double_input_first_et.setText("");
        }
        if (filter_double_input_second_et != null) {
            filter_double_input_second_et.setEnabled(true);
            filter_double_input_second_et.setText("");
        }
        if (filter_single_input_first_et != null) {
            filter_single_input_first_et.setEnabled(true);
            filter_single_input_first_et.setText("");
        }
    }

    /**
     * 是否多选
     *
     * @return
     */
    public boolean isMultiple() {
        return isMultiple;
    }

    /**
     * 获取城市区域选中值
     *
     * @return
     */
    public List<FilterRegionEntity> getDefaultOrSaveValueForCity() {
        return defaultOrSaveValueForCity;
    }

    /**
     * 获取城市区域
     *
     * @return
     */
    public List<FilterRegionEntity> getAllRegion() {
        return allRegionEntitis;
    }

    /**
     * 获取城市区域选中值-新样式
     *
     * @return
     */
    public List<CityEntity> getDefaultOrSaveValueForCity2() {
        return defaultOrSaveValueForCity2;
    }

    /**
     * 获取城市区域-新样式
     *
     * @return
     */
    public List<CityEntity> getAllRegion2() {
        return allCityEntities;
    }

    /**
     * 设置选中城市区域值
     *
     * @param defaultOrSaveValueForCity
     */
    public void setDefaultOrSaveValueForCity(List<FilterRegionEntity> defaultOrSaveValueForCity) {
        this.defaultOrSaveValueForCity = new ArrayList<>();
        this.defaultOrSaveValueForCity.addAll(defaultOrSaveValueForCity);
        if (cityValueListener != null) {
            cityValueListener.cityValueChange(this.defaultOrSaveValueForCity);
        }
    }

    /**
     * 设置选中城市区域值
     *
     * @param defaultOrSaveValueForCity2
     */
    public void setDefaultOrSaveValueForCity2(List<CityEntity> defaultOrSaveValueForCity2) {
        this.defaultOrSaveValueForCity2 = new ArrayList<>();
        this.defaultOrSaveValueForCity2.addAll(defaultOrSaveValueForCity2);
        if (city2ValueListener != null) {
            city2ValueListener.cityValueChange(this.defaultOrSaveValueForCity2);
        }
    }

    /**
     * 获取筛选信息
     *
     * @return
     */
    public FilterEntity getFilterEntity() {
        return filterEntity;
    }

    /**
     * 获取二级页选中值
     *
     * @return
     */
    public List<String> getDefaultOrSaveValueForMultilevel() {
        return defaultOrSaveValueForMultilevel;
    }

    /**
     * 设置二级页选中值
     *
     * @param defaultOrSaveValueForMultilevel
     */
    public void setDefaultOrSaveValueForMultilevel(List<String> defaultOrSaveValueForMultilevel) {
        this.defaultOrSaveValueForMultilevel = new ArrayList<>();
        this.defaultOrSaveValueForMultilevel.addAll(defaultOrSaveValueForMultilevel);
        if (multilevelValueListener != null) {
            multilevelValueListener.multilValueChange(this.defaultOrSaveValueForMultilevel);
        }
    }

    /**
     * 创建布局view
     */
    private void createView() {
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        View titleView = creatTitle();
        if (titleView != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.addView(creatTitle(), params);
        }
        View contentView = createContent();
        if (contentView != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.addView(createContent(), params);
        }
    }

    /**
     * 创建标题
     * TYPE_CITY、 TYPE_MULTILEVEL有小箭头
     *
     * @return
     */
    private View creatTitle() {
        if (TYPE_SELECT.equals(filterEntity.type) || TYPE_TIME_RANGE.equals(filterEntity.type) || TYPE_SINGLE_INPUT.equals(filterEntity.type) || TYPE_DOUBLE_INPUT.equals(filterEntity.type)) {
            RelativeLayout titlelayout = new RelativeLayout(context);
            TextView textView = new TextView(context);
            textView.setTextSize(16);
            textView.setIncludeFontPadding(false);
            textView.setText(filterEntity.title);
            textView.setTextColor(Color.parseColor("#4A4C5C"));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            titlelayout.addView(textView, params);
            if (!TextUtils.isEmpty(filterEntity.remark)) {
                textView = new TextView(context);
                textView.setTextSize(12);
                textView.setIncludeFontPadding(false);
                textView.setText(filterEntity.remark);
                textView.setTextColor(Color.parseColor("#4A4C5C"));
                params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                titlelayout.addView(textView, params);
            }
            titlelayout.setPadding(0, magin15, 0, magin15);
            return titlelayout;
        } else if (TYPE_CITY.equals(filterEntity.type)) {
            View view = inflater.inflate(R.layout.layout_filter_city_title, null);
            TextView filter_city_title_name_tv = view.findViewById(R.id.filter_city_title_name_tv);
            LinearLayout filter_city_title_add_ll = view.findViewById(R.id.filter_city_title_add_ll);
            filter_city_title_name_tv.setText(filterEntity.title);
            filter_city_title_add_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addFilterPageListener != null) {
                        addFilterPageListener.addFilterPage(filterEntity.type);
                    }
                }
            });
            RecyclerView filter_city_title_label_rv = view.findViewById(R.id.filter_city_title_label_rv);
            final SelectCityAdapter selectCityAdapter = new SelectCityAdapter(allRegionEntitis, defaultOrSaveValueForCity, context);
            filter_city_title_label_rv.setLayoutManager(new GridLayoutManager(context, 2));
            filter_city_title_label_rv.setAdapter(selectCityAdapter);
            final int magin505 = CommonUtils.dp2pxForInt(context, 5.5f);
            final int magin12 = CommonUtils.dp2pxForInt(context, 12f);
            filter_city_title_label_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    int position = parent.getChildAdapterPosition(view);
                    int count = selectCityAdapter.getItemCount();
                    int left = 0;
                    int right = 0;
                    int top = 0;
                    int bottom = 0;
                    if (position % 2 == 0) {
                        right = magin505;
                        if (position < count - 2) {
                            bottom = magin12;
                        }
                    } else {
                        left = magin505;
                        if (position < count - 1) {
                            bottom = magin12;
                        }
                    }
                    if (position / 2 == 0) {
                        top = magin12;
                    }
                    outRect.set(left, top, right, bottom);
                }
            });
            setCityValueListener(new CityValueListener() {
                @Override
                public void cityValueChange(List<FilterRegionEntity> values) {
                    selectCityAdapter.setSelect(values);
                }
            });
            return view;
        } else if (TYPE_CITY2.equals(filterEntity.type)) {
            View view = inflater.inflate(R.layout.layout_filter_city_title, null);
            TextView filter_city_title_name_tv = view.findViewById(R.id.filter_city_title_name_tv);
            LinearLayout filter_city_title_add_ll = view.findViewById(R.id.filter_city_title_add_ll);
            filter_city_title_name_tv.setText(filterEntity.title);
            filter_city_title_add_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addFilterPageListener != null) {
                        addFilterPageListener.addFilterPage(filterEntity.type);
                    }
                }
            });
            RecyclerView filter_city_title_label_rv = view.findViewById(R.id.filter_city_title_label_rv);
            final SelectCity2Adapter selectCityAdapter = new SelectCity2Adapter(allCityEntities, defaultOrSaveValueForCity2, context);
            filter_city_title_label_rv.setLayoutManager(new GridLayoutManager(context, 2));
            filter_city_title_label_rv.setAdapter(selectCityAdapter);
            final int magin505 = CommonUtils.dp2pxForInt(context, 5.5f);
            final int magin12 = CommonUtils.dp2pxForInt(context, 12f);
            filter_city_title_label_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    int position = parent.getChildAdapterPosition(view);
                    int count = selectCityAdapter.getItemCount();
                    int left = 0;
                    int right = 0;
                    int top = 0;
                    int bottom = 0;
                    if (position % 2 == 0) {
                        right = magin505;
                        if (position < count - 2) {
                            bottom = magin12;
                        }
                    } else {
                        left = magin505;
                        if (position < count - 1) {
                            bottom = magin12;
                        }
                    }
                    if (position / 2 == 0) {
                        top = magin12;
                    }
                    outRect.set(left, top, right, bottom);
                }
            });
            setCity2ValueListener(new City2ValueListener() {
                @Override
                public void cityValueChange(List<CityEntity> values) {
                    selectCityAdapter.setSelect(values);
                }
            });
            return view;
        } else if (TYPE_MULTILEVEL.equals(filterEntity.type)) {
            View view = inflater.inflate(R.layout.layout_filter_multilevel_title, null);
            TextView filter_multil_title_name_tv = view.findViewById(R.id.filter_multil_title_name_tv);
            filter_multil_title_name_tv.setText(filterEntity.title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addFilterPageListener != null) {
                        addFilterPageListener.addFilterPage(filterEntity.type);
                    }
                }
            });
            RecyclerView filter_multil_title_label_rv = view.findViewById(R.id.filter_multil_title_label_rv);
            final SelectMultilevelAdapter selectMultilevelAdapter = new SelectMultilevelAdapter(filterEntity.list, defaultOrSaveValueForMultilevel, context, isMultiple);
            filter_multil_title_label_rv.setLayoutManager(new GridLayoutManager(context, 2));
            filter_multil_title_label_rv.setAdapter(selectMultilevelAdapter);
            final int magin505 = CommonUtils.dp2pxForInt(context, 5.5f);
            final int magin12 = CommonUtils.dp2pxForInt(context, 12f);
            filter_multil_title_label_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    int position = parent.getChildAdapterPosition(view);
                    int count = selectMultilevelAdapter.getItemCount();
                    int left = 0;
                    int right = 0;
                    int top = 0;
                    int bottom = 0;
                    if (position % 2 == 0) {
                        right = magin505;
                        if (position < count - 2) {
                            bottom = magin12;
                        }
                    } else {
                        left = magin505;
                        if (position < count - 1) {
                            bottom = magin12;
                        }
                    }
                    if (position / 2 == 0) {
                        top = magin12;
                    }
                    outRect.set(left, top, right, bottom);
                }
            });
            setMultilevelValueListener(new MultilevelValueListener() {
                @Override
                public void multilValueChange(List<String> values) {
                    selectMultilevelAdapter.setSelect(values);
                }
            });
            return view;
        }
        return null;
    }

    /**
     * 创建内容
     * TYPE_CITY、 TYPE_MULTILEVEL没有内容
     *
     * @return
     */
    private View createContent() {
        String type = filterEntity.type;
        LinearLayout contentLayout = new LinearLayout(context);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        //select和timerange才有label
        if (TYPE_SELECT.equals(type)) {
            //创建labels
            if (filterEntity.list != null && filterEntity.list.size() > 0) {
                flowFixedLayout = createLabel(filterEntity.list, isMultiple, defaultValueForLabel);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = magin15;
                contentLayout.addView(flowFixedLayout, params);
                flowFixedLayout.setOnLabelClickListener(new FlowFixedLayout.OnLabelClickListener() {
                    @Override
                    public void onLabelClick(String text, int index) {

                    }
                });
            }
        } else if (TYPE_TIME_RANGE.equals(type)) {
            //创建labels
            if (filterEntity.list != null && filterEntity.list.size() > 0) {
                flowFixedLayout = createLabel(filterEntity.list, isMultiple, defaultValueForLabel);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = magin15;
                contentLayout.addView(flowFixedLayout, params);

                //label选中时不可操作输入框
                if (flowFixedLayout.getSelectLabelsIndex().size() > 0) {
                    filter_double_input_first_et.setEnabled(false);
                    filter_double_input_second_et.setEnabled(false);
                }
                flowFixedLayout.setOnLabelClickListener(new FlowFixedLayout.OnLabelClickListener() {
                    @Override
                    public void onLabelClick(String text, int index) {
                        if (filter_double_input_first_et != null && filter_double_input_second_et != null) {
                            if (flowFixedLayout.getSelectLabelsIndex().size() > 0) {
                                filter_double_input_first_et.setEnabled(false);
                                filter_double_input_second_et.setEnabled(false);
                            } else {
                                filter_double_input_first_et.setEnabled(true);
                                filter_double_input_second_et.setEnabled(true);
                            }
                        }
                    }
                });
            }
            String firstHint = filterEntity.input_tip != null ? filterEntity.input_tip.first : null;
            String secondHint = filterEntity.input_tip != null ? filterEntity.input_tip.second : null;
            View inputView = createDoubleInput(defaultValueForTimeRange, firstHint, secondHint, type);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = magin15;
            contentLayout.addView(inputView, params);
        } else if (TYPE_SINGLE_INPUT.equals(type)) {
            String firstHint = filterEntity.input_tip != null ? filterEntity.input_tip.first : null;
            if (firstHint == null) {
                firstHint = "请输入" + filterEntity.title;
            }
            View inputView = createSingleInput(defaultValueForSingleInput, firstHint, type);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = magin5;
            params.bottomMargin = magin20;
            contentLayout.addView(inputView, params);
        } else if (TYPE_DOUBLE_INPUT.equals(type)) {
            String firstHint = filterEntity.input_tip != null ? filterEntity.input_tip.first : null;
            String secondHint = filterEntity.input_tip != null ? filterEntity.input_tip.second : null;
            View inputView = createDoubleInput(defaultValueForDoubleInput, firstHint, secondHint, type);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin = magin5;
            params.bottomMargin = magin20;
            contentLayout.addView(inputView, params);
        } else if (TYPE_CITY.equals(type)) {

        } else if (TYPE_CITY2.equals(type)) {

        } else if (TYPE_MULTILEVEL.equals(type)) {

        }
        return contentLayout;
    }

    /**
     * 创建标签
     *
     * @param list
     * @param isMulti
     * @return
     */
    private FlowFixedLayout createLabel(final List<FilterKVEntity> list, boolean isMulti, final List<String> defaultSelectValues) {
        FlowFixedLayout flowFixedLayout = new FlowFixedLayout(context);
        if (isMulti) {
            flowFixedLayout.setLabelSelect(FlowFixedLayout.LabelSelect.MULTI, R.drawable.light_blue_round_drawable, Color.WHITE);
            flowFixedLayout.setMultiSelectMaxNum(Integer.MAX_VALUE);
        } else {
            flowFixedLayout.setLabelSelect(FlowFixedLayout.LabelSelect.SINGLE, R.drawable.light_blue_round_drawable, Color.WHITE);
            flowFixedLayout.setEnableSingleCancel(true);
        }
        flowFixedLayout.setEnableFillMode(true);
        flowFixedLayout.setLabelAdapter(new FlowFixedLayout.FlowLabelAdapter() {
            @Override
            public int getSize() {
                return list.size();
            }

            @Override
            public String getLabelText(int position) {
                return list.get(position).item_key;
            }

            @Override
            public boolean isSelect(int position) {
                return defaultSelectValues != null && defaultSelectValues.contains(list.get(position).item_val);
            }
        });
        return flowFixedLayout;
    }

    /**
     * 创建双输入框
     *
     * @param filterRangeEntity
     * @param hint1
     * @param hint2
     * @return
     */
    private View createDoubleInput(FilterRangeEntity filterRangeEntity, String hint1, String hint2, String type) {
        View view;
        if (TYPE_TIME_RANGE.equals(type)) {
            view = inflater.inflate(R.layout.layout_filter_range_time, null);
            if (TextUtils.isEmpty(hint1)) {
                hint1 = "起始时间";
            }
            if (TextUtils.isEmpty(hint2)) {
                hint2 = "结束时间";
            }
        } else {
            view = inflater.inflate(R.layout.layout_filter_double_input, null);
            if (TextUtils.isEmpty(hint1)) {
                hint1 = "最小值";
            }
            if (TextUtils.isEmpty(hint2)) {
                hint2 = "最大值";
            }
        }
        filter_double_input_first_et = view.findViewById(R.id.filter_double_input_first_et);
        filter_double_input_second_et = view.findViewById(R.id.filter_double_input_second_et);
        if (!TextUtils.isEmpty(hint1)) {
            filter_double_input_first_et.setHint(hint1);
        }
        if (!TextUtils.isEmpty(hint2)) {
            filter_double_input_second_et.setHint(hint2);
        }
        if (TYPE_TIME_RANGE.equals(type)) {
            setTimeRange(filterRangeEntity, filter_double_input_first_et, filter_double_input_second_et);
        } else {
            if (filterRangeEntity != null && !TextUtils.isEmpty(filterRangeEntity.first) && !TextUtils.isEmpty(filterRangeEntity.second)) {
                filter_double_input_first_et.setText(filterRangeEntity.first);
                filter_double_input_second_et.setText(filterRangeEntity.second);
            }
        }
        return view;
    }

    private void setTimeRange(FilterRangeEntity filterRangeEntity, final TextView first_tv, final TextView second_tv) {
        Calendar firstCalendar = null;
        Calendar secondCalendar = null;
        if (filterRangeEntity != null && !TextUtils.isEmpty(filterRangeEntity.first) && !TextUtils.isEmpty(filterRangeEntity.second)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date firstDate = simpleDateFormat.parse(filterRangeEntity.first);
                first_tv.setText(filterRangeEntity.first);
                firstCalendar = Calendar.getInstance();
                firstCalendar.setTime(firstDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                Date secondDate = simpleDateFormat.parse(filterRangeEntity.second);
                second_tv.setText(filterRangeEntity.second);
                secondCalendar = Calendar.getInstance();
                secondCalendar.setTime(secondDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        final Calendar finalFirstCalendar = firstCalendar;
        first_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiheTimePickerBuilder builder = DateDialogUtils.createPickerViewBuilder(context, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String time = format.format(date);
                        first_tv.setText(time);
                    }
                });
                if (finalFirstCalendar != null) {
                    builder.setDate(finalFirstCalendar);
                }
                if (filterEntity.title.equals("合同签订时间")){
                   Calendar calendar1 = Calendar.getInstance();
                   calendar1.set(Calendar.YEAR, calendar1.get(Calendar.YEAR) - 5);
                   builder.setRangDate(calendar1,Calendar.getInstance());
                }else if (filterEntity.title.equals("合同执行时间")){
                    Calendar calendar1 = Calendar.getInstance();
                    Calendar calendar2 = Calendar.getInstance();
                    calendar1.set(Calendar.YEAR, calendar1.get(Calendar.YEAR) - 5);
                    calendar2.set(Calendar.YEAR, calendar2.get(Calendar.YEAR) + 5);
                    builder.setRangDate(calendar1,calendar2);
                }
                builder.setType(new boolean[]{true, true, true, false, false, false});
                builder.build().show();
            }
        });
        final Calendar finalSecondCalendar = secondCalendar;
        second_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiheTimePickerBuilder builder = DateDialogUtils.createPickerViewBuilder(context, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String time = format.format(date);
                        second_tv.setText(time);
                    }
                });
                if (finalSecondCalendar != null) {
                    builder.setDate(finalSecondCalendar);
                }
                builder.setType(new boolean[]{true, true, true, false, false, false});
                builder.build().show();
            }
        });
    }

    /**
     * 创建单输入框
     *
     * @param value
     * @param hint
     * @return
     */
    private View createSingleInput(String value, String hint, String type) {
        View view = inflater.inflate(R.layout.layout_filter_single_input, null);
        filter_single_input_first_et = view.findViewById(R.id.filter_single_input_first_et);
        if (!TextUtils.isEmpty(hint)) {
            filter_single_input_first_et.setHint(hint);
        }
        if (!TextUtils.isEmpty(value)) {
            filter_single_input_first_et.setText(value);
        }
        return view;
    }

    /**
     * 设置新增筛选页面监听
     *
     * @param addFilterPageListener
     */
    public void setFilterPageListener(AddFilterPageListener addFilterPageListener) {
        this.addFilterPageListener = addFilterPageListener;
    }

    /**
     * 新增筛选页面监听
     */
    public interface AddFilterPageListener {
        void addFilterPage(String type);
    }

    /**
     * 设置二级页选中值变化监听
     *
     * @param multilevelValueListener
     */
    public void setMultilevelValueListener(MultilevelValueListener multilevelValueListener) {
        this.multilevelValueListener = multilevelValueListener;
    }

    /**
     * 二级页选中值变化监听
     */
    public interface MultilevelValueListener {
        void multilValueChange(List<String> values);
    }

    /**
     * 设置城市区域选中值变化监听
     *
     * @param cityValueListener
     */
    public void setCityValueListener(CityValueListener cityValueListener) {
        this.cityValueListener = cityValueListener;
    }

    /**
     * 城市区域选中值变化监听
     */
    public interface CityValueListener {
        void cityValueChange(List<FilterRegionEntity> values);
    }

    /**
     * 设置城市区域选中值变化监听-新样式
     *
     * @param city2ValueListener
     */
    public void setCity2ValueListener(City2ValueListener city2ValueListener) {
        this.city2ValueListener = city2ValueListener;
    }

    /**
     * 城市区域选中值变化监听-新样式
     */
    public interface City2ValueListener {
        void cityValueChange(List<CityEntity> values);
    }

}
