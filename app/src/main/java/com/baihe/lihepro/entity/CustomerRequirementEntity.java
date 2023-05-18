package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class CustomerRequirementEntity {
    private int total;
    private int page;
    private int pageSize;
    private List<CustomerDemandEntity> rows;

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

    public List<CustomerDemandEntity> getRows() {
        return rows;
    }

    public void setRows(List<CustomerDemandEntity> rows) {
        this.rows = rows;
    }
}
