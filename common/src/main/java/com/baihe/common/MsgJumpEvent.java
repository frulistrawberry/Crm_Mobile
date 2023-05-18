package com.baihe.common;

import com.baihe.common.entity.MsgBean;

public class MsgJumpEvent {
    public boolean isToList;
    public MsgBean customMsg;

    public MsgJumpEvent(boolean isToList, MsgBean customMsg) {
        this.isToList = isToList;
        this.customMsg = customMsg;
    }
}
