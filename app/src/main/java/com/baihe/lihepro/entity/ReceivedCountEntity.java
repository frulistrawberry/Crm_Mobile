package com.baihe.lihepro.entity;

import com.baihe.common.entity.MsgBean;

import java.io.Serializable;
import java.util.List;

public class ReceivedCountEntity implements Serializable {
    public List<MsgBean> row;
    public int count;

}
