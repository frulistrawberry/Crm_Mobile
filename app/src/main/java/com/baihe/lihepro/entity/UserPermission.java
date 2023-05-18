package com.baihe.lihepro.entity;

import java.io.Serializable;

public class UserPermission implements Serializable {

    private boolean transfercustomerreq;
    private boolean transfercustomerleads;

    public boolean isTransfercustomerleads() {
        return transfercustomerleads;
    }

    public void setTransfercustomerleads(boolean transfercustomerleads) {
        this.transfercustomerleads = transfercustomerleads;
    }

    public boolean isTransfercustomerreq() {
        return transfercustomerreq;
    }

    public void setTransfercustomerreq(boolean transfercustomerreq) {
        this.transfercustomerreq = transfercustomerreq;
    }
}
