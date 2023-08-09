package com.trungdz.appfood.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.model.modelresponse.OrderDetailResponse
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.domain.repository.IRepository
import com.trungdz.appfood.presentation.ui.OrderDetailFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailFragmentViewModel @Inject constructor(val iRepository: IRepository) : ViewModel() {

    val orderDetail: MutableLiveData<Resource<OrderDetailResponse>> = MutableLiveData()
    val messageResponse: MutableLiveData<Resource<MessageResponse>> = MutableLiveData()
    fun getAllItemInOrder(idOrder: Int) = viewModelScope.launch {
        orderDetail.postValue(Resource.Loading())
        val response = iRepository.getAllItemInOrder(idOrder)
        orderDetail.postValue(response)
    }

    fun cancelOrder(idOrder: Int)=viewModelScope.launch {
        val response= iRepository.cancelOrder(idOrder)
        messageResponse.postValue(response)
    }
}