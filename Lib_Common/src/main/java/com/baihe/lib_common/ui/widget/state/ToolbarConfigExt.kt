package com.baihe.lib_common.ui.widget.state

import com.baihe.lib_framework.widget.state.ktx.ToolbarConfig
import com.baihe.lib_framework.widget.state.ktx.ToolbarExtras.toolbarExtras

object ToolbarConfigExt {
    var ToolbarConfig.showSearch:Boolean? by toolbarExtras()
    var ToolbarConfig.onSearchListener:((keywords:String)->Unit)? by toolbarExtras()
    var ToolbarConfig.showSearchBehind:Boolean? by toolbarExtras()

    @JvmOverloads
    fun ToolbarConfig.showSearch(showSearchBehind:Boolean = true,onSearchListener:(keywords:String)->Unit){
        this.onSearchListener = onSearchListener
        this.showSearchBehind = showSearchBehind
        showSearch = true
    }
}