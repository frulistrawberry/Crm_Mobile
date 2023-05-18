package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class SalesListEntity {
    private int total;
    private int page;
    private int pageSize;
    private List<ListItemEntity> rows;
    private String amount_total;

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

    public List<ListItemEntity> getRows() {
        return rows;
    }

    public void setRows(List<ListItemEntity> rows) {
        this.rows = rows;
    }

    public String getAmount_total() {
        return amount_total;
    }

    public void setAmount_total(String amount_total) {
        this.amount_total = amount_total;
    }
}
