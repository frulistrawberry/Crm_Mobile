package com.baihe.lib_common.dialog.adapter

/**
 * 一般布局数据适配器
 */
abstract class SelectDataAdapter {
    /**
     * 数据数量
     *
     * @return
     */
    abstract fun getCount():Int

    /**
     * 每条数据对应的文本描述
     *
     * @param dataPosition 数据索引
     * @return
     */
    abstract fun getText(dataPosition: Int): String?

    /**
     * 初始普通数据选中索引
     *
     * @return
     */
    open fun initSelectDataPosition(): Int {
        return -1
    }

}