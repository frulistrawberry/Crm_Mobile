package com.baihe.lihepro.entity.schedule;

import com.baihe.lihepro.entity.ProductEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MySchedule implements Serializable {
    private int total;
    private int page = 1;
    private int pageSize = 20;
    private List<MyScheduleItem> rows = new ArrayList<>();

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

    public List<MyScheduleItem> getRows() {
        return rows;
    }

    public void setRows(List<MyScheduleItem> rows) {
        this.rows = rows;
    }
}
