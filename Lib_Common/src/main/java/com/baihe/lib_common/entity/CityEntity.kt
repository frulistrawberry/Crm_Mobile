package com.baihe.lib_common.entity

data class CityEntity(
    val full:String?,
    val name:String?,
    val code:String,
    val children:List<CityEntity>?,
    var select:Boolean)