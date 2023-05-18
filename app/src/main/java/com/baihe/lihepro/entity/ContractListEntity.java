package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class ContractListEntity {
    private int total;
    private int page;
    private int pageSize;
    private String amount_total;
    private List<ContractItemEntity> contractList;

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

    public String getAmount_total() {
        return amount_total;
    }

    public void setAmount_total(String amount_total) {
        this.amount_total = amount_total;
    }

    public List<ContractItemEntity> getContractList() {
        return contractList;
    }

    public void setContractList(List<ContractItemEntity> contractList) {
        this.contractList = contractList;
    }
}
