package com.baihe.lihepro.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class ListItemEntity implements Parcelable {
    private String order_id;
    private String customer_id;
    private String customer_name;
    private String phone;
    private String encode_phone;
    private String order_num;
    private String category;
    private String category_text;
    private String category_color;
    private String group_customer_id;
    private String wechat;
    private List<KeyValueEntity> show_array;
    private RelationEntity relation_info;

    public ListItemEntity() {

    }

    protected ListItemEntity(Parcel in) {
        order_id = in.readString();
        customer_id = in.readString();
        customer_name = in.readString();
        phone = in.readString();
        encode_phone = in.readString();
        order_num = in.readString();
        category = in.readString();
        category_text = in.readString();
        category_color = in.readString();
        group_customer_id = in.readString();
        wechat = in.readString();
        show_array = in.createTypedArrayList(KeyValueEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(order_id);
        dest.writeString(customer_id);
        dest.writeString(customer_name);
        dest.writeString(phone);
        dest.writeString(encode_phone);
        dest.writeString(order_num);
        dest.writeString(category);
        dest.writeString(category_text);
        dest.writeString(category_color);
        dest.writeString(group_customer_id);
        dest.writeString(wechat);
        dest.writeTypedList(show_array);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListItemEntity> CREATOR = new Creator<ListItemEntity>() {
        @Override
        public ListItemEntity createFromParcel(Parcel in) {
            return new ListItemEntity(in);
        }

        @Override
        public ListItemEntity[] newArray(int size) {
            return new ListItemEntity[size];
        }
    };

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_text() {
        return category_text;
    }

    public void setCategory_text(String category_text) {
        this.category_text = category_text;
    }

    public String getCategory_color() {
        return category_color;
    }

    public void setCategory_color(String category_color) {
        this.category_color = category_color;
    }

    public String getGroup_customer_id() {
        return group_customer_id;
    }

    public void setGroup_customer_id(String group_customer_id) {
        this.group_customer_id = group_customer_id;
    }

    public List<KeyValueEntity> getShow_array() {
        return show_array;
    }

    public void setShow_array(List<KeyValueEntity> show_array) {
        this.show_array = show_array;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEncode_phone() {
        return encode_phone;
    }

    public void setEncode_phone(String encode_phone) {
        this.encode_phone = encode_phone;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public RelationEntity getRelation_info() {
        return relation_info;
    }

    public void setRelation_info(RelationEntity relation_info) {
        this.relation_info = relation_info;
    }
}
