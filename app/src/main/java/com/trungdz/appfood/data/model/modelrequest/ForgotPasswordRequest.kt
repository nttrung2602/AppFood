package com.trungdz.appfood.data.model.modelrequest

data class ForgotPasswordRequest(val username: String, val verifyID:String?=null,val password:String?=null,val repeatPassword:String?=null)
