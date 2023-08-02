package com.trungdz.appfood.data.util.cache

interface ISharedPreference {
    fun putToken(token: String?)
    fun getToken(): String
    fun saveLoggedInStatus(loggedIn:Boolean)
    fun getLoggedInStatus():Boolean
    fun saveUserInfo(userInfo:String?)
    fun getUserInfo():String
    fun removeToken()
}