package com.baihe.lib_common.entity


data class UserEntity (
    /**
     * 账户
     */
    val account: String,
    /**
     * 头像
     */
    val avatar: String,
    /**
     * 公司id
     */
    val company_id: Int,
    /**
     * 公司名称
     */
    val company_name: String,
    /**
     * 公司标签
     */
    val company_tag: String,
    /**
     * 用户Id
     */
    val id: Int,
    /**
     * 用户名称
     */
    val name: String,
    /**
     * 用户性别
     */
    val sex: Int
    )