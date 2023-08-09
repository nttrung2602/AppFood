package com.trungdz.appfood.presentation.viewmodel.account.register

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
class RegisterFragmentViewModel @Inject constructor(private val iRepository: IRepository):ViewModel(){
    val messageResponse: MutableLiveData<Resource<MessageResponse>> = MutableLiveData()

    fun createAccount(  username: String,
                        password: String,
                        name: String,
                        email: String,
                        phone: String,
                        address: String)= viewModelScope.launch {
        val response=iRepository.createAccount(username, password, name, email, phone, address)
        messageResponse.postValue(response)
    }
}