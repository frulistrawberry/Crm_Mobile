package com.baihe.lihepro.filter.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.structure.StructureBaseEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;

/**
 * Author：xubo
 * Time：2020-02-22
 * Description：
 */
public class FilterKVEntity extends Observable implements Parcelable, Serializable {
    public String item_key;
    public String item_val;
    public String pid;
    public List<FilterKVEntity> children;

    //是否选中
    private boolean isSelect;
    //关联父channel
    private FilterKVEntity parent;

    public enum SelectEvent {
        SELF,
        TO_CHILD,
        TO_PARENT,
    }

    public FilterKVEntity() {

    }

    protected FilterKVEntity(Parcel in) {
        item_key = in.readString();
        item_val = in.readString();
        pid = in.readString();
        children = in.createTypedArrayList(FilterKVEntity.CREATOR);
        isSelect = in.readByte() != 0;
        parent = in.readParcelable(FilterKVEntity.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(item_key);
        dest.writeString(item_val);
        dest.writeString(pid);
        dest.writeTypedList(children);
        dest.writeByte((byte) (isSelect ? 1 : 0));
        dest.writeParcelable(parent, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FilterKVEntity> CREATOR = new Creator<FilterKVEntity>() {
        @Override
        public FilterKVEntity createFromParcel(Parcel in) {
            return new FilterKVEntity(in);
        }

        @Override
        public FilterKVEntity[] newArray(int size) {
            return new FilterKVEntity[size];
        }
    };

    /**
     * 深拷贝
     *
     * @return
     */
    public FilterKVEntity copy() {
        FilterKVEntity filterKVEntity = new FilterKVEntity();
        filterKVEntity.setPid(pid);
        filterKVEntity.setItem_key(item_key);
        filterKVEntity.setItem_val(item_val);
        filterKVEntity.isSelect = isSelect;
        if (children != null) {
            List<FilterKVEntity> copyChildren = new ArrayList<>();
            for (FilterKVEntity copyChild : children) {
                copyChildren.add(copyChild.copy());
            }
            filterKVEntity.setChildren(copyChildren);
        }
        return filterKVEntity;
    }

    public String getItem_key() {
        return item_key;
    }

    public void setItem_key(String item_key) {
        this.item_key = item_key;
    }

    public String getItem_val() {
        return item_val;
    }

    public void setItem_val(String item_val) {
        this.item_val = item_val;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<FilterKVEntity> getChildren() {
        return children;
    }

    public void setChildren(List<FilterKVEntity> children) {
        this.children = children;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        //选中变更后通知关联项变更
        if (this.isSelect != select) {
            this.isSelect = select;
            setChanged();
            notifyObservers(SelectEvent.SELF);
        }
    }

    public void setSelect(boolean select, SelectEvent event) {
        //选中变更后通知关联项变更
        if (this.isSelect != select) {
            this.isSelect = select;
            setChanged();
            notifyObservers(event);
        }
    }

    public void addParent(FilterKVEntity filterKVEntity) {
        parent = filterKVEntity;
    }

    public FilterKVEntity getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterKVEntity that = (FilterKVEntity) o;
        return Objects.equals(item_key, that.item_key);
    }
}
