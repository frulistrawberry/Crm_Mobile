package com.baihe.lihepro.entity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-30
 * Description：
 */
public class ContactAddEntity {
    private List<KeyValueEntity> contract;
    private List<ProductEntity> product;
    private List<KeyValueEntity> customer_info;
    private List<KeyValueEntity> req_info;
    private List<KeyValueEntity> receivables;
    private List<List<KeyValueEntity>> discount;
    private List<String> contract_pic;



    public List<KeyValueEntity> getContract() {
        return contract;
    }

    public void setContract(List<KeyValueEntity> contract) {
        this.contract = contract;
    }

    public List<KeyValueEntity> getCustomer_info() {
        return customer_info;
    }

    public void setCustomer_info(List<KeyValueEntity> customer_info) {
        this.customer_info = customer_info;
    }

    public List<KeyValueEntity> getReq_info() {
        return req_info;
    }

    public void setReq_info(List<KeyValueEntity> req_info) {
        this.req_info = req_info;
    }

    public List<ProductEntity> getProduct() {
        return product;
    }

    public void setProduct(List<ProductEntity> product) {
        this.product = product;
    }

    public List<List<KeyValueEntity>> getDiscount() {
        return discount;
    }

    public void setDiscount(List<List<KeyValueEntity>> discount) {
        this.discount = discount;
    }

    public List<String> getContract_pic() {
        return contract_pic;
    }

    public void setContract_pic(List<String> contract_pic) {
        this.contract_pic = contract_pic;
    }

    public List<KeyValueEntity> getReceivables() {
        return receivables;
    }

    public void setReceivables(List<KeyValueEntity> receivables) {
        this.receivables = receivables;
    }
}

