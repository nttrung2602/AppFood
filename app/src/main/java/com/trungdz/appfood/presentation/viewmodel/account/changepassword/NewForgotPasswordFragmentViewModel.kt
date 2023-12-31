package com.trungdz.appfood.presentation.viewmodel.account.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.domain.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewForgotPasswordFragmentViewModel @Inject constructor(val iRepository: IRepository) :
    ViewModel() {
    val messageResponse: MutableLiveData<Resource<MessageResponse>> = MutableLiveData()

    fun newPassword(username: String, newPassword: String, repeatNewPassword: String) =
        viewModelScope.launch {
            val response = iRepository.accessNewPassword(username, newPassword, repeatNewPassword)
            messageResponse.postValue(response)
        }
}