package com.baihe.lihepro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.dialog.BaseDialog;
import com.baihe.common.dialog.DialogBuilder;
import com.baihe.common.manager.TaskManager;
import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.filter.FilterChannelFragment;
import com.baihe.lihepro.filter.entity.FilterKVEntity;
import com.baihe.lihepro.manager.ChannelManager;
import com.baihe.lihepro.utils.Utils;
import com.baihe.lihepro.view.TextTransitionRadioButton;
import com.baihe.lihepro.view.TextTransitionRadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Author：xubo
 * Time：2020-09-17
 * Description：
 */
public class ChannelDialog extends BaseDialog {
    public ChannelDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder implements Observer {
        private boolean cancelable = true;
        private OnConfirmClickListener confirmListener;
        private String title;
        private Context context;
        private List<FilterKVEntity> channels;
        private String selectChannelId;

        private TextView channel_cancel_tv;
        private TextView channel_title_tv;
        private HorizontalScrollView channel_tab_hsv;
        private TextTransitionRadioGroup channel_tab_ttrg;
        private HorizontalScrollView channel_navigation_hsv;
        private TextView channel_navigation_tv;
        private ViewPager channel_content_vp;
        private TextView channel_confirm_tv;

        private ChannelManager channelManager;
        private FilterChannelFragment.FilterChannelTabManager tabManager;
        private Map<Integer, Integer> idForIndexs = new HashMap<>();
        private Map<Integer, RadioButton> indexForViews = new HashMap<>();
        private List<FilterKVEntity> tabs = new ArrayList<>();

        private ChannelTabAdapter channelTabAdapter;

        private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = idForIndexs.get(checkedId);
                channel_content_vp.setCurrentItem(index);
            }
        };
        private ChannelManager.StatusListener statusListener = new ChannelManager.StatusListener() {
            @Override
            public void onUpate() {
                initNavigation();
                channelTabAdapter.notifyDataSetChanged();
            }
        };

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setData(List<FilterKVEntity> channels) {
            this.channels = channels;
            return this;
        }

        public Builder setSelectChannelId(String selectChannelId) {
            this.selectChannelId = selectChannelId;
            return this;
        }

        public Builder setOnConfirmClickListener(OnConfirmClickListener confirmListener) {
            this.confirmListener = confirmListener;
            return this;
        }

        public ChannelDialog build() {
            DialogBuilder<ChannelDialog> dialogBuilder = new DialogBuilder<ChannelDialog>(context, R.layout.dialog_channel, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT) {
                @Override
                public void createView(final Dialog dialog, View view) {
                    initView(view);
                    initData();
                    listener(dialog);
                }

                @Override
                public ChannelDialog createDialog(Context context, int themeResId) {
                    return new ChannelDialog(context, themeResId);
                }

            }.setAnimationRes(R.style.dialog_style).setGravity(Gravity.BOTTOM).setCancelable(cancelable);
            ChannelDialog dialog = dialogBuilder.create();
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    tabManager.deleteObserver(Builder.this);
                    channelManager.removeListener(statusListener);
                }
            });
            return dialog;
        }

        private void listener(final Dialog dialog) {
            channelManager.addListener(statusListener);
            channel_cancel_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            channel_tab_ttrg.setOnCheckedChangeListener(checkedChangeListener);
            channel_content_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    channel_tab_ttrg.move(position, positionOffset);
                }

                @Override
                public void onPageSelected(int position) {
                    RadioButton radioButton = indexForViews.get(position);
                    if (radioButton != null) {
                        radioButton.setChecked(true);
                        scrollTitle(radioButton);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            channel_confirm_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<FilterKVEntity> selectChannles = channelManager.getSelectChannels();
                    if (selectChannles != null && selectChannles.size() > 0) {
                        if (confirmListener != null) {
                            dialog.dismiss();
                            confirmListener.onConfirm(dialog, selectChannles.get(0).item_key, selectChannles.get(0).item_val);
                        }
                    } else {
                        if (confirmListener != null) {
                            dialog.dismiss();
                            confirmListener.onConfirm(dialog, "", "");
                        }
                    }
                }
            });
        }

        private void initData() {
            channel_title_tv.setText(title);
            channelManager = ChannelManager.newInstance(false);
            List<String> values = new ArrayList<String>();
            if (!TextUtils.isEmpty(selectChannelId)) {
                values.add(selectChannelId);
            }
            channelManager.init(channels, values);
            tabManager = FilterChannelFragment.FilterChannelTabManager.newInstance();
            tabManager.addObserver(this);

            channelTabAdapter = new ChannelTabAdapter(context, tabs, channelManager, tabManager);
            channel_content_vp.setAdapter(channelTabAdapter);
            channel_content_vp.setOffscreenPageLimit(1);
            initTab();
            initNavigation();
        }

        private void initView(View view) {
            channel_cancel_tv = view.findViewById(R.id.channel_cancel_tv);
            channel_title_tv = view.findViewById(R.id.channel_title_tv);
            channel_tab_hsv = view.findViewById(R.id.channel_tab_hsv);
            channel_tab_ttrg = view.findViewById(R.id.channel_tab_ttrg);
            channel_navigation_hsv = view.findViewById(R.id.channel_navigation_hsv);
            channel_navigation_tv = view.findViewById(R.id.channel_navigation_tv);
            channel_content_vp = view.findViewById(R.id.channel_content_vp);
            channel_confirm_tv = view.findViewById(R.id.channel_confirm_tv);
        }

        @Override
        public void update(Observable o, Object arg) {
            if (tabManager.getTabIndex() > tabs.size()) {
                return;
            }
            //重置tab组
            if (tabManager.getTabIndex() == 0) {
                tabs.clear();
                tabs.add(tabManager.getTabChannel());
            } else if (tabs.size() == tabManager.getTabIndex()) {  //添加tab
                tabs.add(tabManager.getTabChannel());
            } else {  //在某个tab重置
                int count = tabs.size() - tabManager.getTabIndex();
                for (int i = 0; i < count; i++) {
                    tabs.remove(tabs.size() - 1);
                }
                tabs.add(tabManager.getTabChannel());
            }
            channel_tab_ttrg.removeAllViews();
            channel_tab_ttrg.setOnCheckedChangeListener(null);
            initTab();
            channelTabAdapter.notifyDataSetChanged();
            channel_tab_ttrg.setOnCheckedChangeListener(checkedChangeListener);
            TaskManager.newInstance().runOnUi(new Runnable() {
                @Override
                public void run() {
                    channel_content_vp.setCurrentItem(tabManager.getTabIndex() + 1);
                }
            }, 10);
        }

        private void scrollTitle(View view) {
            final int left = view.getLeft();
            final int right = view.getRight();
            final int childWidth = right - left;
            final int screenWidth = CommonUtils.getScreenWidth(context);
            int scrollX = left - (screenWidth - childWidth) / 2;
            channel_tab_hsv.smoothScrollTo(scrollX, 0);
        }

        private void initTab() {
            idForIndexs.clear();
            indexForViews.clear();
            int tabNum = tabs.size() + 1;
            int margin = CommonUtils.dp2pxForInt(context, 16);
            for (int i = 0; i < tabNum; i++) {
                TextTransitionRadioButton textTransitionRadioButton = new TextTransitionRadioButton(context);
                textTransitionRadioButton.setTransition(true);
                textTransitionRadioButton.setSelectedTextBold(true);
                textTransitionRadioButton.setSelectedTextColor(Color.parseColor("#4A4C5C"));
                textTransitionRadioButton.setSelectedTextSize(CommonUtils.sp2px(context, 16));
                textTransitionRadioButton.setTextGravity(TextTransitionRadioButton.TextGravity.CENTER);
                textTransitionRadioButton.setUnSelectedTextBold(false);
                textTransitionRadioButton.setUnSelectedTextColor(Color.parseColor("#4A4C5C"));
                textTransitionRadioButton.setUnSelectedTextSize(CommonUtils.sp2px(context, 14));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textTransitionRadioButton.setButtonDrawable(null);
                } else {
                    textTransitionRadioButton.setButtonDrawable(new BitmapDrawable());
                }
                textTransitionRadioButton.setText(Utils.formatNumText(i + 1) + "级渠道");
                if (tabManager.getTabIndex() == i) {
                    textTransitionRadioButton.setChecked(true);
                    textTransitionRadioButton.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
                int id = textTransitionRadioButton.getId();
                if (id == View.NO_ID) {
                    id = (int) (i + System.currentTimeMillis());
                    textTransitionRadioButton.setId(id);
                }
                idForIndexs.put(id, i);
                indexForViews.put(i, textTransitionRadioButton);

                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.MATCH_PARENT);
                if (i == 0) {
                    textTransitionRadioButton.setChecked(true);
                    params.leftMargin = 0;
                    params.rightMargin = margin;
                } else if (i == tabNum - 1) {
                    params.leftMargin = margin;
                    params.rightMargin = 0;
                } else {
                    params.leftMargin = margin;
                    params.rightMargin = margin;
                }
                channel_tab_ttrg.addView(textTransitionRadioButton, params);
            }
        }

        private void initNavigation() {
            List<FilterKVEntity> selectChannels = channelManager.getSelectChannels();
            if (selectChannels != null && selectChannels.size() > 0) {
                FilterKVEntity selectChannel = selectChannels.get(0);
                channel_navigation_tv.setText(gettNavigationName(selectChannel));
                channel_navigation_tv.requestLayout();
                channel_navigation_hsv.setVisibility(View.VISIBLE);
            } else {
                channel_navigation_hsv.setVisibility(View.GONE);
            }
        }

        private String gettNavigationName(FilterKVEntity channel) {
            if (channel == null) {
                return "";
            }
            StringBuffer stringBuffer = new StringBuffer();
            if (channel.getParent() != null) {
                stringBuffer.append(gettNavigationName(channel.getParent()));
                stringBuffer.append(" > " + channel.getItem_key());
            } else {
                stringBuffer.append(channel.getItem_key());
            }
            String name = stringBuffer.toString();
            return name;
        }

    }

    public interface OnConfirmClickListener {
        void onConfirm(Dialog dialog, String selectChannelName, String selectChannelId);
    }

    public static class ChannelTabAdapter extends PagerAdapter {
        private Context context;
        private List<FilterKVEntity> tabs;
        private Map<Integer, View> mapView;
        private ChannelManager channelManager;
        private FilterChannelFragment.FilterChannelTabManager tabManager;

        public ChannelTabAdapter(Context context, List<FilterKVEntity> tabs, ChannelManager channelManager, FilterChannelFragment.FilterChannelTabManager tabManager) {
            this.context = context;
            this.tabs = tabs;
            this.mapView = new HashMap<>();
            this.channelManager = channelManager;
            this.tabManager = tabManager;
        }

        @Override
        public int getCount() {
            return tabs.size() + 1;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(getView(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = getView(position);
            refreshView(view, position, position > 0 ? tabs.get(position - 1).getItem_val() : null, channelManager, tabManager);
            container.addView(view);
            return view;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        public View getView(int position) {
            View view = mapView.get(position);
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.fragment_filter_child_channel, null);
                mapView.put(position, view);
            }
            return view;
        }

        private void refreshView(View view, int position, String parentChannelId, ChannelManager channelManager, FilterChannelFragment.FilterChannelTabManager tabManager) {
            if (view != null && view.getTag() != null && view.getTag() instanceof ChannelListAdapter) {
                ChannelListAdapter channelListAdapter = (ChannelListAdapter) view.getTag();
                if (parentChannelId != null && parentChannelId.equals(channelListAdapter.getParentChannelId())) {
                    channelListAdapter.notifyDataSetChanged();
                } else if (channelListAdapter.getParentChannelId() != null && channelListAdapter.getParentChannelId().equals(parentChannelId)) {
                    channelListAdapter.notifyDataSetChanged();
                } else if (parentChannelId == null && channelListAdapter.getParentChannelId() == null) {
                    channelListAdapter.notifyDataSetChanged();
                } else {
                    List<FilterKVEntity> list;
                    if (parentChannelId == null) {
                        list = channelManager.getChannels();
                        channelListAdapter.updateList(parentChannelId, list);
                    } else {
                        FilterKVEntity parentChannel = channelManager.findChannelById(parentChannelId);
                        if (parentChannel == null) {
                            list = new ArrayList<>();
                        } else {
                            list = parentChannel.children;
                        }
                    }
                    channelListAdapter.updateList(parentChannelId, list);
                }
            } else {
                List<FilterKVEntity> list;
                if (parentChannelId == null) {
                    list = channelManager.getChannels();
                } else {
                    FilterKVEntity parentChannel = channelManager.findChannelById(parentChannelId);
                    if (parentChannel == null) {
                        list = new ArrayList<>();
                    } else {
                        list = parentChannel.children;
                    }
                }
                ChannelListAdapter channelListAdapter = new ChannelListAdapter(context, list, tabManager, position, parentChannelId, channelManager);
                RecyclerView filter_child_child_rv = view.findViewById(R.id.filter_child_child_rv);
                filter_child_child_rv.setAdapter(channelListAdapter);
                filter_child_child_rv.setLayoutManager(new LinearLayoutManager(context));
                final int driverHeight = CommonUtils.dp2pxForInt(context, 0.5F);
                final int driverMagin = CommonUtils.dp2pxForInt(context, 20F);
                final Paint driverPaint = new Paint();
                driverPaint.setColor(Color.parseColor("#ECECF0"));
                filter_child_child_rv.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.onDraw(c, parent, state);
                        int childSize = parent.getChildCount();
                        for (int i = 0; i < childSize - 1; i++) {
                            View child = parent.getChildAt(i);
                            float top = child.getBottom();
                            float bottom = top + driverHeight;
                            float right = child.getRight();
                            c.drawRect(driverMagin, top, right, bottom, driverPaint);
                        }
                    }
                });

                view.setTag(channelListAdapter);
            }
        }
    }

    public static class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.Holder> {
        private List<FilterKVEntity> list;
        private LayoutInflater inflater;
        private FilterChannelFragment.FilterChannelTabManager tabManager;
        private int tabIndex;
        private String parentChannelId;
        private ChannelManager channelManager;

        public ChannelListAdapter(Context context, List<FilterKVEntity> list, FilterChannelFragment.FilterChannelTabManager tabManager, int tabIndex, String parentChannelId, ChannelManager channelManager) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
            this.tabManager = tabManager;
            this.tabIndex = tabIndex;
            this.parentChannelId = parentChannelId;
            this.channelManager = channelManager;
        }

        public String getParentChannelId() {
            return parentChannelId;
        }

        public void updateList(String parentChannelId, List<FilterKVEntity> list) {
            this.parentChannelId = parentChannelId;
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ChannelListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.fragment_filter_child_channel_item, parent, false);
            ChannelListAdapter.Holder holder = new ChannelListAdapter.Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ChannelListAdapter.Holder holder, int position) {
            final FilterKVEntity channel = list.get(position);
            holder.filter_child_channel_item_text_tv.setText(channel.item_key);
            if (!channelManager.isMultiple() && channel.getChildren() != null && channel.getChildren().size() > 0) {
                holder.filter_child_channel_item_select_iv.setVisibility(View.INVISIBLE);
            } else {
                holder.filter_child_channel_item_select_iv.setVisibility(View.VISIBLE);
            }
            if (channel.isSelect()) {
                holder.filter_child_channel_item_select_iv.setImageResource(R.drawable.check_icon);
            } else {
                holder.filter_child_channel_item_select_iv.setImageResource(R.drawable.unchecked_icon);
            }
            if (channel.children != null && channel.children.size() > 0) {
                holder.filter_child_channel_item_right_iv.setVisibility(View.VISIBLE);
            } else {
                holder.filter_child_channel_item_right_iv.setVisibility(View.INVISIBLE);
            }
            holder.filter_child_channel_item_select_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    channel.setSelect(!channel.isSelect());
                    notifyDataSetChanged();
                }
            });
            holder.filter_child_channel_item_other_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (channel.children != null && channel.children.size() > 0) {
                        tabManager.setTab(channel, tabIndex);
                    } else {
                        channel.setSelect(!channel.isSelect());
                        notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list != null ? list.size() : 0;
        }

        public class Holder extends RecyclerView.ViewHolder {
            ImageView filter_child_channel_item_select_iv;
            ImageView filter_child_channel_item_right_iv;
            RelativeLayout filter_child_channel_item_other_rl;
            TextView filter_child_channel_item_text_tv;

            public Holder(@NonNull View itemView) {
                super(itemView);
                filter_child_channel_item_select_iv = itemView.findViewById(R.id.filter_child_channel_item_select_iv);
                filter_child_channel_item_right_iv = itemView.findViewById(R.id.filter_child_channel_item_right_iv);
                filter_child_channel_item_other_rl = itemView.findViewById(R.id.filter_child_channel_item_other_rl);
                filter_child_channel_item_text_tv = itemView.findViewById(R.id.filter_child_channel_item_text_tv);
            }
        }
    }
}
