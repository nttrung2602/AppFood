package com.trungdz.appfood.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.model.modelrequest.CheckoutRequest
import com.trungdz.appfood.data.model.modelresponse.ItemListInCartResponse
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.domain.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckOutFragmentViewModel @Inject constructor(val iRepository: IRepository) : ViewModel() {

    val itemListInCart: MutableLiveData<Resource<ItemListInCartResponse>> = MutableLiveData()
    val statusMessageCheckout: MutableLiveData<Resource<MessageResponse>> = MutableLiveData()

    fun getAllItemInCart() = viewModelScope.launch {
        itemListInCart.postValue(Resource.Loading())

        val response = iRepository.getAllItemInCart()
        itemListInCart.postValue(response)
    }

    fun checkout(idPayment: Int, description: String="") = viewModelScope.launch {
        statusMessageCheckout.postValue(Resource.Loading())
        val response = iRepository.checkout(CheckoutRequest(idPayment, description))
        statusMessageCheckout.postValue(response)
    }
}