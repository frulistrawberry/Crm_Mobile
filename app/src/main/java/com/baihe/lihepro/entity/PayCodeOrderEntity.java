package com.baihe.lihepro.entity;

import java.util.List;

public class PayCodeOrderEntity {
    private Object id;
    private Double amount;
    private String categore_name;
    private int category;
    private List<PayCodePlanEntity> plan_list;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategore_name() {
        return categore_name;
    }

    public void setCategore_name(String categore_name) {
        this.categore_name = categore_name;
    }


    public int getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public List<PayCodePlanEntity> getPlan_list() {
        return plan_list;
    }

    public void setPlan_list(List<PayCodePlanEntity> plan_list) {
        this.plan_list = plan_list;
    }
}
