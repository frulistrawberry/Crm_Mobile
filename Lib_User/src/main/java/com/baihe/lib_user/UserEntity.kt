package com.baihe.lib_user

data class BossSeaEntity(val id: String, val name: String)

data class VersionEntity(
    val version: String,
    val forced: Int,
    val url: String,
    val channel: String,
    val msgContent: String,
)