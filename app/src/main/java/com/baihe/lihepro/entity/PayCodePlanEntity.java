package com.baihe.lihepro.entity;

public class PayCodePlanEntity {

    private String title;
    private Object plan_id;
    private int receivables_type;
    private String plan_receivables_money;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(Object plan_id) {
        this.plan_id = plan_id;
    }

    public int getReceivables_type() {
        return receivables_type;
    }

    public void setReceivables_type(int receivables_type) {
        this.receivables_type = receivables_type;
    }

    public String getPlan_receivables_money() {
        return plan_receivables_money;
    }

    public void setPlan_receivables_money(String plan_receivables_money) {
        this.plan_receivables_money = plan_receivables_money;
    }
}
