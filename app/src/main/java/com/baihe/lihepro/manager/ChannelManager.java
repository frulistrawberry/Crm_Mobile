package com.baihe.lihepro.manager;

import com.baihe.lihepro.entity.structure.StructureBaseEntity;
import com.baihe.lihepro.filter.entity.FilterKVEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Author：xubo
 * Time：2020-08-06
 * Description：渠道客资管理
 */
public class ChannelManager {
    private boolean isInit;
    private List<FilterKVEntity> channels = new ArrayList<>();

    //选中观察者
    private ChannelObserver channelObserver;
    //是否多选模式
    private boolean isMultiple;

    public static ChannelManager newInstance(boolean isMultiple) {
        return new ChannelManager(isMultiple);
    }

    public static ChannelManager newInstance() {
        return new ChannelManager(true);
    }

    private ChannelManager(boolean isMultiple) {
        this.isMultiple = isMultiple;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void init(List<FilterKVEntity> channels, List<String> selectValues) {
        if (channels == null) {
            return;
        }
        isInit = true;
        this.channels.clear();
        channelObserver = new ChannelObserver(isMultiple);
        //渠道数据关联
        for (FilterKVEntity filterKVEntity : channels) {
            overallChannel(filterKVEntity, true, this.channels);
        }
        initSelectChannels(this.channels, selectValues);
        channelObserver.setRootChannels(this.channels);
    }

    private void initSelectChannels(List<FilterKVEntity> list, List<String> selectValues) {
        for (FilterKVEntity channel : list) {
            if (selectValues != null && selectValues.contains(channel.getItem_val())) {
                channel.setSelect(true);
            } else {
                channel.setSelect(false);
            }
            if (channel.getChildren() != null && channel.getChildren().size() > 0) {
                initSelectChannels(channel.getChildren(), selectValues);
            }
        }
    }

    public boolean isInit() {
        return isInit;
    }

    private void checkInit() {
        if (!isInit) {
            throw new IllegalStateException("ChannelManager初始化数据后才能使用");
        }
    }

    /**
     * 渠道数据关联
     *
     * @param filterKVEntity
     * @param isTopLevel
     * @param receiveList
     */
    private void overallChannel(FilterKVEntity filterKVEntity, boolean isTopLevel, List<FilterKVEntity> receiveList) {
        if (filterKVEntity == null) {
            return;
        }
        if (isTopLevel) {
            receiveList.add(filterKVEntity);
            filterKVEntity.addObserver(channelObserver);
        }
        if (filterKVEntity.children != null) {
            for (FilterKVEntity childFilterKVEntity : filterKVEntity.children) {
                childFilterKVEntity.addParent(filterKVEntity);
                //注册观察者
                childFilterKVEntity.addObserver(channelObserver);
                overallChannel(childFilterKVEntity, false, receiveList);
            }
        }
    }

    public FilterKVEntity findChannelById(String channelId) {
        return findChannelById(channelId, this.channels);
    }

    private FilterKVEntity findChannelById(String channelId, List<FilterKVEntity> channels) {
        if (channels == null) {
            return null;
        }
        for (FilterKVEntity channel : channels) {
            if (channel.getItem_val().equals(channelId)) {
                return channel;
            } else {
                FilterKVEntity filterKVEntity = findChannelById(channelId, channel.children);
                if (filterKVEntity != null) {
                    return filterKVEntity;
                }
            }
        }
        return null;
    }

    public List<FilterKVEntity> getChannels() {
        checkInit();
        return channels;
    }

    private List<String> getSelectChannelIds(List<FilterKVEntity> channels) {
        List<String> list = new ArrayList<>();
        if (channels == null) {
            return list;
        }
        for (FilterKVEntity filterKVEntity : channels) {
            if (isMultiple) {  //多选只匹配最底层子类
                if (filterKVEntity.getChildren() != null && filterKVEntity.getChildren().size() > 0) {
                    list.addAll(getSelectChannelIds(filterKVEntity.getChildren()));
                } else {
                    if (filterKVEntity.isSelect()) {
                        list.add(filterKVEntity.getItem_val());
                    }
                }
            } else {  //匹配所有
                if (filterKVEntity.isSelect()) {
                    list.add(filterKVEntity.getItem_val());
                }
                if (filterKVEntity.getChildren() != null && filterKVEntity.getChildren().size() > 0) {
                    list.addAll(getSelectChannelIds(filterKVEntity.getChildren()));
                }
            }
        }
        return list;
    }

    public List<String> getSelectChannelIds() {
        return getSelectChannelIds(channels);
    }


    private List<FilterKVEntity> getSelectChannels(List<FilterKVEntity> channels) {
        List<FilterKVEntity> list = new ArrayList<>();
        if (channels == null) {
            return list;
        }
        for (FilterKVEntity filterKVEntity : channels) {
            if (isMultiple) {  //多选只匹配最底层子类
                if (filterKVEntity.getChildren() != null && filterKVEntity.getChildren().size() > 0) {
                    list.addAll(getSelectChannels(filterKVEntity.getChildren()));
                } else {
                    if (filterKVEntity.isSelect()) {
                        list.add(filterKVEntity);
                    }
                }
            } else {  //匹配所有
                if (filterKVEntity.isSelect()) {
                    list.add(filterKVEntity);
                }
                if (filterKVEntity.getChildren() != null && filterKVEntity.getChildren().size() > 0) {
                    list.addAll(getSelectChannels(filterKVEntity.getChildren()));
                }
            }
        }
        return list;
    }

    public List<FilterKVEntity> getSelectChannels() {
        return getSelectChannels(channels);
    }

    public void addListener(StatusListener statusListener) {
        this.channelObserver.addListener(statusListener);
    }

    public void removeListener(StatusListener statusListener) {
        this.channelObserver.removeListener(statusListener);
    }

    public interface StatusListener {
        void onUpate();
    }

    public static class ChannelObserver implements Observer {
        private List<StatusListener> listeners;
        private boolean isMultiple;
        private List<FilterKVEntity> rootChannels;

        public ChannelObserver(boolean isMultiple) {
            this.isMultiple = isMultiple;
        }

        public void addListener(StatusListener statusListener) {
            if (listeners == null) {
                listeners = new ArrayList<>();
            }
            if (!listeners.contains(statusListener)) {
                listeners.add(statusListener);
            }
        }

        public void removeListener(StatusListener statusListener) {
            if (listeners != null && listeners.contains(statusListener)) {
                listeners.remove(statusListener);
            }
        }

        public void setRootChannels(List<FilterKVEntity> rootChannels) {
            this.rootChannels = rootChannels;
        }

        @Override
        public void update(Observable o, Object arg) {
            if (o instanceof FilterKVEntity && arg instanceof FilterKVEntity.SelectEvent) {
                FilterKVEntity filterKVEntity = (FilterKVEntity) o;
                if (isMultiple) {  //多选模式联动勾选
                    FilterKVEntity.SelectEvent selectEvent = (FilterKVEntity.SelectEvent) arg;
                    switch (selectEvent) {
                        case SELF:
                            //更新父级选中
                            updateSelectForParent(filterKVEntity);
                            //更新子级选中
                            updateSelectForChild(filterKVEntity);
                            break;
                        case TO_CHILD:
                            //更新子级选中
                            updateSelectForChild(filterKVEntity);
                            break;
                        case TO_PARENT:
                            //更新父级选中
                            updateSelectForParent(filterKVEntity);
                            break;
                    }
                } else {  //单选只选其一
                    //清空其他选择
                    if (filterKVEntity.isSelect()) {
                        clearSelect(rootChannels, filterKVEntity);
                    }
                }
                if (listeners != null) {
                    for (StatusListener listener : listeners) {
                        listener.onUpate();
                    }
                }
            }
        }

        private void clearSelect(List<FilterKVEntity> list, FilterKVEntity select) {
            if (list == null) {
                return;
            }
            for (FilterKVEntity channel : list) {
                if (!select.equals(channel)) {
                    channel.setSelect(false);
                }
                if (channel.getChildren() != null && channel.getChildren().size() > 0) {
                    clearSelect(channel.getChildren(), select);
                }
            }
        }

        /**
         * 更新父级选中
         *
         * @param filterKVEntity
         */
        private void updateSelectForParent(FilterKVEntity filterKVEntity) {
            FilterKVEntity parentChannel = filterKVEntity.getParent();
            if (parentChannel != null) {
                parentChannel.setSelect(isSelectForChannel(parentChannel), FilterKVEntity.SelectEvent.TO_PARENT);
            }
        }

        /**
         * 更新子级选中
         *
         * @param filterKVEntity
         */
        private void updateSelectForChild(FilterKVEntity filterKVEntity) {
            List<FilterKVEntity> childChannels = filterKVEntity.getChildren();
            if (childChannels != null) {
                for (FilterKVEntity childChannel : childChannels) {
                    childChannel.setSelect(filterKVEntity.isSelect(), FilterKVEntity.SelectEvent.TO_CHILD);
                }
            }
        }

        /**
         * channel是否选中
         *
         * @param filterKVEntity
         * @return
         */
        private boolean isSelectForChannel(FilterKVEntity filterKVEntity) {
            List<FilterKVEntity> childChannels = filterKVEntity.getChildren();
            if (childChannels != null) {
                for (FilterKVEntity childChannel : childChannels) {
                    if (childChannel.isSelect()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
