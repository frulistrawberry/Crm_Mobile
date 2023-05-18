package com.baihe.lihepro.adapter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

/**
 * Author：xubo
 * Time：2020-08-02
 * Description：
 */
public abstract class KVFloadAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private Map<Integer, Boolean> flodMap = new HashMap<>();

    /**
     * 是否收起
     *
     * @param index
     * @return
     */
    public boolean getFlod(int index) {
        if (flodMap.containsKey(index)) {
            return flodMap.get(index);
        }
        return true;
    }

    /**
     * 设置索引对应的收起状态
     *
     * @param index
     * @param flod
     * @return
     */
    public void setFlod(int index, boolean flod) {
        flodMap.put(index, flod);
    }

    /**
     * 清空收缩索引
     */
    public void clearFLod(){
        flodMap.clear();
    }
}
