package com.baihe.lib_common.entity


data class UserEntity (
    /**
     * 账户
     */
    var account: String,
    /**
     * 头像
     */
    var avatar: String,
    /**
     * 公司id
     */
    var company_id: Int,
    /**
     * 公司名称
     */
    var company_name: String,
    /**
     * 公司标签
     */
    var company_tag: String,
    /**
     * 用户Id
     */
    var id: Int,
    /**
     * 用户名称
     */
    var name: String,
    /**
     * 用户性别
     */
    var sex: Int
    )