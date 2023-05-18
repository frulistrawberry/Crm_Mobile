package com.baihe.lihepro.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-09-04
 * Description：
 */
public class ProductListEntity implements Parcelable {
    private int total;
    private int page;
    private int pageSize;
    private List<ProductEntity> rows;

    public ProductListEntity() {

    }

    protected ProductListEntity(Parcel in) {
        total = in.readInt();
        page = in.readInt();
        pageSize = in.readInt();
        rows = in.createTypedArrayList(ProductEntity.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total);
        dest.writeInt(page);
        dest.writeInt(pageSize);
        dest.writeTypedList(rows);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductListEntity> CREATOR = new Creator<ProductListEntity>() {
        @Override
        public ProductListEntity createFromParcel(Parcel in) {
            return new ProductListEntity(in);
        }

        @Override
        public ProductListEntity[] newArray(int size) {
            return new ProductListEntity[size];
        }
    };

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<ProductEntity> getRows() {
        return rows;
    }

    public void setRows(List<ProductEntity> rows) {
        this.rows = rows;
    }
}
