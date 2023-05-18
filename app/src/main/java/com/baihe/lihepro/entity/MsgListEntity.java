package com.baihe.lihepro.entity;

import java.io.Serializable;
import java.util.List;

public class MsgListEntity implements Serializable {
    private int total;
    private int page;
    private int pageSize;
    private List<MsgEntity> rows;

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

    public List<MsgEntity> getRows() {
        return rows;
    }

    public void setRows(List<MsgEntity> rows) {
        this.rows = rows;
    }
}
