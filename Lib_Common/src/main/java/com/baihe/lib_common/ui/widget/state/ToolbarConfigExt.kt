package com.baihe.lib_common.ui.widget.state

import com.baihe.lib_framework.widget.state.ktx.ToolbarConfig
import com.baihe.lib_framework.widget.state.ktx.ToolbarExtras.toolbarExtras

object ToolbarConfigExt {
    var ToolbarConfig.showSearch:Boolean? by toolbarExtras()
    var ToolbarConfig.onSearchListener:((keywords:String)->Unit)? by toolbarExtras()
    fun ToolbarConfig.showSearch(onSearchListener:(keywords:String)->Unit){
        this.onSearchListener = onSearchListener
        showSearch = true
    }
}