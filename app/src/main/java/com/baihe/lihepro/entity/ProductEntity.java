package com.baihe.lihepro.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Objects;

/**
 * Author：xubo
 * Time：2020-09-04
 * Description：
 */
public class ProductEntity implements Parcelable {
    private String id;
    private String name;
    private String price;
    private List<String> accessory;
    private String category;
    private String category_color;

    public ProductEntity() {

    }

    protected ProductEntity(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readString();
        accessory = in.createStringArrayList();
        category = in.readString();
        category_color = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeStringList(accessory);
        dest.writeString(category);
        dest.writeString(category_color);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductEntity> CREATOR = new Creator<ProductEntity>() {
        @Override
        public ProductEntity createFromParcel(Parcel in) {
            return new ProductEntity(in);
        }

        @Override
        public ProductEntity[] newArray(int size) {
            return new ProductEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getAccessory() {
        return accessory;
    }

    public void setAccessory(List<String> accessory) {
        this.accessory = accessory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_color() {
        return category_color;
    }

    public void setCategory_color(String category_color) {
        this.category_color = category_color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return Objects.equals(id, that.id);
    }

}
