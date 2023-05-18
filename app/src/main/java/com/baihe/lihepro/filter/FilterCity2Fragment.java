package com.baihe.lihepro.filter;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.CityEntity;
import com.baihe.lihepro.filter.entity.FilterEntity;
import com.github.xubo.statusbarutils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-02-21
 * Description：城市选页
 */
public class FilterCity2Fragment extends Fragment {
    public static final String TAG = FilterViewModel.TYPE_CITY2;

    private ImageView filter_city_title_back_iv;
    private TextView filter_city_title_tv;
    private EditText filter_city_search_et;
    private ImageView filter_city_search_delete_iv;
    private RecyclerView filter_city_rv;
    private TextView filter_city_ok_tv;

    private FilterViewModel filterViewModel;
    private FilterEntity filterEntity;

    private CitySelectAdapter citySelectAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FilterActivity filterActivity = (FilterActivity) getActivity();
        filterViewModel = filterActivity.getCity2FilterViewModel();
        filterEntity = filterViewModel.getFilterEntity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_city2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout filter_city_title_rl = view.findViewById(R.id.filter_city_title_rl);
        int statusHeight = StatusBarUtils.getStatusBarHeight(getContext());
        ((LinearLayout.LayoutParams) filter_city_title_rl.getLayoutParams()).topMargin = statusHeight;
        filter_city_title_back_iv = view.findViewById(R.id.filter_city_title_back_iv);
        filter_city_title_tv = view.findViewById(R.id.filter_city_title_tv);
        filter_city_search_et = view.findViewById(R.id.filter_city_search_et);
        filter_city_search_delete_iv = view.findViewById(R.id.filter_city_search_delete_iv);
        filter_city_rv = view.findViewById(R.id.filter_city_rv);
        filter_city_ok_tv = view.findViewById(R.id.filter_city_ok_tv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        listener();
    }

    private void initData() {
        filter_city_title_tv.setText(filterEntity.title);

        citySelectAdapter = new CitySelectAdapter(getContext(), filterViewModel.getDefaultOrSaveValueForCity2());
        filter_city_rv.setAdapter(citySelectAdapter);
        filter_city_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        citySelectAdapter.updateData(getCityList(""));
    }

    private List<CityEntity> getCityList(String keyword) {
        List<CityEntity> citys = new ArrayList<>();
        for (CityEntity cityEntity : filterViewModel.getAllRegion2()) {
            if (TextUtils.isEmpty(keyword)) {
                citys.add(cityEntity);
            } else if (cityEntity.getName().contains(keyword)) {
                citys.add(cityEntity);
            }
        }
        return citys;
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        TranslateAnimation animation = null;
        if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN && enter) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            animation.setInterpolator(new DecelerateInterpolator());
        } else if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE && !enter) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            animation.setInterpolator(new DecelerateInterpolator());
        }
        if (animation == null) {
            animation = new TranslateAnimation(0, 0, 0, 0);
        } else {
            animation.setDuration(300);
        }
        return animation;
    }

    private void listener() {
        filter_city_title_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        filter_city_search_et.addTextChangedListener(new TextWatcher() {
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
                    filter_city_search_delete_iv.setVisibility(View.GONE);
                } else {
                    filter_city_search_delete_iv.setVisibility(View.VISIBLE);
                }
                citySelectAdapter.updateData(getCityList(keyword));
            }
        });
        filter_city_search_delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_city_search_et.setText("");
            }
        });
        filter_city_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CityEntity> selectCitys = citySelectAdapter.getSelectCitys();
                filterViewModel.setDefaultOrSaveValueForCity2(selectCitys);
                getParentFragmentManager().popBackStack();
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
        private List<CityEntity> allCitys;
        private List<CityEntity> selectCitys;

        public CitySelectAdapter(Context context, List<CityEntity> selectCitys) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.allCitys = new ArrayList<>();
            this.selectCitys = new ArrayList<>();
            if (selectCitys != null) {
                this.selectCitys.addAll(selectCitys);
            }
        }

        public void updateData(List<CityEntity> allCitys) {
            this.allCitys.clear();
            if (allCitys != null) {
                this.allCitys.addAll(allCitys);
            }
            notifyDataSetChanged();
        }

        public List<CityEntity> getSelectCitys() {
            return selectCitys;
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
        public CitySelectAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        public void onBindViewHolder(@NonNull final CitySelectAdapter.Holder holder, int position) {
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

            final CityEntity cityEntity = allCitys.get(position);
            if (selectCitys.contains(cityEntity)) {
                holder.city_select_item_select_iv.setImageResource(R.drawable.check_icon);
            } else {
                holder.city_select_item_select_iv.setImageResource(R.drawable.unchecked_icon);
            }
            holder.city_select_item_name_tv.setText(cityEntity.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectCitys.contains(cityEntity)) {
                        selectCitys.remove(cityEntity);
                    } else {
                        selectCitys.add(cityEntity);
                    }
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return allCitys.size();
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
