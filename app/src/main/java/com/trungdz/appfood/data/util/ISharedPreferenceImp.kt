package com.trungdz.appfood.data.util

import android.content.SharedPreferences
import com.trungdz.appfood.data.util.cache.ISharedPreference
import javax.inject.Inject

class ISharedPreferenceImp @Inject constructor(val sharedPreferences: SharedPreferences) :
    ISharedPreference {

    override fun putToken(token: String?) {
        sharedPreferences.edit().putString(Constants.ACCESS_TOKEN_KEY, token).commit();
    }

    override fun getToken(): String {
        return sharedPreferences.getString(Constants.ACCESS_TOKEN_KEY, "").toString();
    }

    override fun removeToken() {
        sharedPreferences.edit().remove(Constants.ACCESS_TOKEN_KEY).commit()
    }

    override fun saveLoggedInStatus(loggedIn: Boolean) {
//        sharedPreferences.edit().putBoolean(Constants.LOGGED_IN_KEY, loggedIn).apply()
        sharedPreferences.edit().putBoolean(Constants.LOGGED_IN_KEY, loggedIn).commit()

    }

    override fun getLoggedInStatus(): Boolean {
        return sharedPreferences.getBoolean(Constants.LOGGED_IN_KEY, false)
    }

    override fun saveUserInfo(userInfo: String?) {
        sharedPreferences.edit().putString(Constants.USER_INFO, userInfo).apply()
    }

    override fun getUserInfo(): String {
        return sharedPreferences.getString(Constants.USER_INFO, "").toString()
    }
}