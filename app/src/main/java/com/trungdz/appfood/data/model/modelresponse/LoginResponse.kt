package com.trungdz.appfood.data.model.modelresponse

import com.trungdz.appfood.data.model.UserInfo

data class LoginResponse(
    val message: String,
    val token: String,
    val userInfo: UserInfo,
    val expireTime: String
)
