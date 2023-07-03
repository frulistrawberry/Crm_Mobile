package com.baihe.lib_common.entity;

public enum ButtonAction {
    FOLLOW("follow"),
    DISPATCH_ORDER("dispatchOrder"),
    EDIT_OPPO("editOppo"),
    TRANSFER_OPPO("transferOppo");


    final String value;

    ButtonAction(String value) {
        this.value = value;
    }

    String valueOf() {
        return value;
    }
}
