package com.baihe.lihepro.filter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseFragment;
import com.baihe.lihepro.R;
import com.baihe.lihepro.filter.entity.FilterKVEntity;
import com.baihe.lihepro.manager.ChannelManager;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-17
 * Description：
 */
public class FilterChannelChildFragment extends BaseFragment {
    public static final String INTENT_TAB_INDEX = "INTENT_TAB_INDEX";
    public static final String INTENT_CHANNEL_ID = "INTENT_CHANNEL_ID";

    private RecyclerView filter_child_child_rv;
    private String parentChannelId;
    private int tabIndex;
    private ChannelListAdapter channelListAdapter;
    private FilterChannelFragment.FilterChannelTabManager filterChannelTabManager;
    private ChannelManager channelManager;

    private ChannelManager.StatusListener statusListener = new ChannelManager.StatusListener() {
        @Override
        public void onUpate() {
            channelListAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_filter_child_channel;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        FilterActivity crmFilterActivity = (FilterActivity) getActivity();
        filterChannelTabManager = crmFilterActivity.getFilterChannelTabManager();
        channelManager = crmFilterActivity.getChannelManager();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        channelManager.removeListener(statusListener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filter_child_child_rv = view.findViewById(R.id.filter_child_child_rv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabIndex = getArguments().getInt(INTENT_TAB_INDEX);
        parentChannelId = getArguments().getString(INTENT_CHANNEL_ID);
        initData();
        listener();
    }

    private void initData() {
        List<FilterKVEntity> list = null;
        if (parentChannelId == null) {
            list = channelManager.getChannels();
        } else {
            FilterKVEntity parentChannel = channelManager.findChannelById(parentChannelId);
            if (parentChannel != null) {
                list = parentChannel.children;
            }
        }
        channelListAdapter = new ChannelListAdapter(getContext(), list);
        filter_child_child_rv.setAdapter(channelListAdapter);
        filter_child_child_rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void listener() {
        channelManager.addListener(statusListener);
    }

    public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.Holder> {
        private List<FilterKVEntity> list;
        private LayoutInflater inflater;

        public ChannelListAdapter(Context context, List<FilterKVEntity> list) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @NonNull
        @Override
        public ChannelListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.fragment_filter_child_channel_item, parent, false);
            Holder holder = new Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ChannelListAdapter.Holder holder, int position) {
            final FilterKVEntity channel = list.get(position);
            holder.filter_child_channel_item_text_tv.setText(channel.item_key);
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
                        filterChannelTabManager.setTab(channel, tabIndex);
                    }else{
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
