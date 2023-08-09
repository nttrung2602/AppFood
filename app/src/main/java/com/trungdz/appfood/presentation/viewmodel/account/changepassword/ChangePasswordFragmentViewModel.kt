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
class ChangePasswordFragmentViewModel @Inject constructor(private val iRepository: IRepository) :
    ViewModel() {
    val messageResponse: MutableLiveData<Resource<MessageResponse>> = MutableLiveData()

    fun changePassword(oldPassword: String, newPassword: String, repeatPassword: String)=viewModelScope.launch {
        val response = iRepository.changePassword(oldPassword, newPassword, repeatPassword)
        messageResponse.postValue(response)
    }
}