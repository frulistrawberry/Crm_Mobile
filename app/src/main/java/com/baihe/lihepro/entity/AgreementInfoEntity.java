package com.baihe.lihepro.entity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class AgreementInfoEntity {
    private int id;
    private int contract_id;
    private String type;
    private String content;
    private String agreement_num;
    private String agreement_amount;
    private String create_time;
    private String update_time;
    private int audit_status;
    private String people_amount;
    private String product_amount;
    private int user_id;
    private String audit_time;
    private String zhuchiren;
    private String huazhuangshi;
    private String sheyingshi;
    private String shexiangshi;
    private String changbu;
    private String huayi;
    private String teshu;
    private String desk_table;
    private String desk;
    private String type_txt;
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setContract_id(int contract_id) {
        this.contract_id = contract_id;
    }
    public int getContract_id() {
        return contract_id;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setAgreement_num(String agreement_num) {
        this.agreement_num = agreement_num;
    }
    public String getAgreement_num() {
        return agreement_num;
    }

    public void setAgreement_amount(String agreement_amount) {
        this.agreement_amount = agreement_amount;
    }
    public String getAgreement_amount() {
        return agreement_amount;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
    public String getCreate_time() {
        return create_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
    public String getUpdate_time() {
        return update_time;
    }

    public void setAudit_status(int audit_status) {
        this.audit_status = audit_status;
    }
    public int getAudit_status() {
        return audit_status;
    }
    public void setPeople_amount(String people_amount) {
        this.people_amount = people_amount;
    }
    public String getPeople_amount() {
        return people_amount;
    }

    public void setProduct_amount(String product_amount) {
        this.product_amount = product_amount;
    }
    public String getProduct_amount() {
        return product_amount;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public int getUser_id() {
        return user_id;
    }

    public void setAudit_time(String audit_time) {
        this.audit_time = audit_time;
    }
    public String getAudit_time() {
        return audit_time;
    }

    public void setZhuchiren(String zhuchiren) {
        this.zhuchiren = zhuchiren;
    }
    public String getZhuchiren() {
        if (TextUtils.isEmpty(zhuchiren) || "0".equals(zhuchiren))
            return "0";
        if ("2".equals(type))
            return "-"+zhuchiren;
        return zhuchiren;
    }

    public void setHuazhuangshi(String huazhuangshi) {
        this.huazhuangshi = huazhuangshi;
    }
    public String getHuazhuangshi() {
        if (TextUtils.isEmpty(huazhuangshi) || "0".equals(huazhuangshi))
            return "0";
        if ("2".equals(type))
            return "-"+huazhuangshi;
        return huazhuangshi;
    }

    public void setSheyingshi(String sheyingshi) {
        this.sheyingshi = sheyingshi;
    }
    public String getSheyingshi() {
        if (TextUtils.isEmpty(sheyingshi) || "0".equals(sheyingshi))
            return "0";
        if ("2".equals(type))
            return "-"+sheyingshi;
        return sheyingshi;
    }

    public void setShexiangshi(String shexiangshi) {
        this.shexiangshi = shexiangshi;
    }
    public String getShexiangshi() {
        if (TextUtils.isEmpty(shexiangshi) || "0".equals(shexiangshi))
            return "0";
        if ("2".equals(type))
            return "-"+shexiangshi;
        return shexiangshi;
    }

    public void setChangbu(String changbu) {
        this.changbu = changbu;
    }
    public String getChangbu() {
        return changbu;
    }

    public void setHuayi(String huayi) {
        this.huayi = huayi;
    }
    public String getHuayi() {
        if (TextUtils.isEmpty(huayi) || "0".equals(huayi))
            return "0";
        if ("2".equals(type))
            return "-"+huayi;
        return huayi;
    }

    public void setTeshu(String teshu) {
        this.teshu = teshu;
    }
    public String getTeshu() {
        if (TextUtils.isEmpty(teshu) || "0".equals(teshu))
            return "0";
        if ("2".equals(type))
            return "-"+teshu;
        return teshu;
    }

    public void setDesk_table(String desk_table) {
        this.desk_table = desk_table;
    }
    public String getDesk_table() {
        return desk_table;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }
    public String getDesk() {
        return desk;
    }

    public void setType_txt(String type_txt) {
        this.type_txt = type_txt;
    }
    public String getType_txt() {
        return type_txt;
    }

    public List<KeyValueEntity> covertToKeyValListForWedding(boolean isAdd){
        String pre = isAdd?"升级":"减项";
        List<KeyValueEntity> list = new ArrayList<>();
        KeyValueEntity keyValueEntity1 = new KeyValueEntity();
        keyValueEntity1.setKey("主持人"+pre);
        keyValueEntity1.setVal(getZhuchiren());
        KeyValueEntity keyValueEntity2 = new KeyValueEntity();
        keyValueEntity2.setKey("化妆师"+pre);
        keyValueEntity2.setVal(getHuazhuangshi());
        KeyValueEntity keyValueEntity3 = new KeyValueEntity();
        keyValueEntity3.setKey("摄影师"+pre);
        keyValueEntity3.setVal(getSheyingshi());
        KeyValueEntity keyValueEntity4 = new KeyValueEntity();
        keyValueEntity4.setKey("摄像师"+pre);
        keyValueEntity4.setVal(getShexiangshi());
        KeyValueEntity keyValueEntity5 = new KeyValueEntity();
        keyValueEntity5.setKey("花艺类"+pre);
        keyValueEntity5.setVal(getHuayi());
        KeyValueEntity keyValueEntity6 = new KeyValueEntity();
        keyValueEntity6.setKey("主持人"+pre);
        keyValueEntity6.setVal(getTeshu());
        list.add(keyValueEntity1);
        list.add(keyValueEntity2);
        list.add(keyValueEntity3);
        list.add(keyValueEntity4);
        list.add(keyValueEntity5);
        list.add(keyValueEntity6);
        return list;
    }

    public List<KeyValueEntity> covertToKeyValListForDesk(){
        List<KeyValueEntity> list = new ArrayList<>();
        KeyValueEntity keyValueEntity1 = new KeyValueEntity();
        keyValueEntity1.setKey("创建时间");
        keyValueEntity1.setVal(getCreate_time());
        KeyValueEntity keyValueEntity2 = new KeyValueEntity();
        keyValueEntity2.setKey("桌数");
        keyValueEntity2.setVal(getDesk_table());
        KeyValueEntity keyValueEntity3 = new KeyValueEntity();
        keyValueEntity3.setKey("每桌价格");
        keyValueEntity3.setVal(getDesk());
        KeyValueEntity keyValueEntity4 = new KeyValueEntity();
        keyValueEntity4.setKey("协议金额");
        keyValueEntity4.setVal(getAgreement_amount());
        list.add(keyValueEntity1);
        list.add(keyValueEntity2);
        list.add(keyValueEntity3);
        list.add(keyValueEntity4);
        return list;
    }

}
