package com.baihe.lihepro.entity;

import java.util.List;

public class PayCodeListEntity {

    private int total;
    private int page;
    private int pageSize;
    private List<PayCodeEntity> rows;

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

    public List<PayCodeEntity> getRows() {
        return rows;
    }

    public void setRows(List<PayCodeEntity> rows) {
        this.rows = rows;
    }
}
