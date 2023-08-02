package com.trungdz.appfood.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.trungdz.appfood.data.api.AppFoodApiService
import com.trungdz.appfood.data.model.UserInfo
import com.trungdz.appfood.data.model.modelrequest.LoginRequest
import com.trungdz.appfood.data.repository.IRepositoryImp
import com.trungdz.appfood.data.repository.datasource.IAppFoodRemoteDatasource
import com.trungdz.appfood.data.repository.datasourceImp.IAppFoodRemoteDataSourceImp
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.data.util.cache.ISharedPreference
import com.trungdz.appfood.domain.repository.IRepository
import com.trungdz.appfood.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val sharedPreference: ISharedPreference,
) : ViewModel() {
    val loggedIn: MutableLiveData<Boolean?> = MutableLiveData()
    val error: MutableLiveData<String?> = MutableLiveData()
    private fun saveAccessToken(token: String?) = sharedPreference.putToken(token)
    private fun saveUserInfo(userInfo: String) = sharedPreference.saveUserInfo(userInfo)
    private fun saveLoggedInStatus(status: Boolean) = sharedPreference.saveLoggedInStatus(status)
    fun login(infoLogin: LoginRequest) {
        authUseCase.loginUser(infoLogin).onEach { result ->
            when (result) {
                is Resource.Loading -> Log.d("LoginViewModel", "I'm here, Loading")
                is Resource.Success -> {
                    loggedIn.postValue(true)
                    saveLoggedInStatus(true)
                    saveAccessToken(result.data?.token)
                    val gson = Gson()

                    saveUserInfo(gson.toJson(result.data?.userInfo))
                    Log.d("TrangThaiDangNhap", "${sharedPreference.getLoggedInStatus()}")
                    Log.d("LoginViewModel", "I'm here, Success ${result.message}")
                }
                is Resource.Error -> {
                    loggedIn.postValue(false)
                    saveLoggedInStatus(false)

                    error.value = result.message
                    Log.d("LoginViewModel", "I'm here, Error ${result.message}")
                }
            }
        }.launchIn(viewModelScope)
    }
}