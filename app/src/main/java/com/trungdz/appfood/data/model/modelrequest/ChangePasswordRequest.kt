package com.trungdz.appfood.data.model.modelrequest

data class ChangePasswordRequest(val oldPassword:String,val newPassword:String, val repeatPassword:String)
