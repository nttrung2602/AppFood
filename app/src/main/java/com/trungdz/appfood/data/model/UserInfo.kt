package com.trungdz.appfood.data.model

data class UserInfo(
    var address: String,
    val email: String,
    val id_account: Int,
    val id_customer: Int,
    val image: String,
    var name: String,
    var phone: String
)