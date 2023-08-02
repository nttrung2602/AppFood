package com.trungdz.appfood.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getAllItemInOrder(idOrder: Int) = viewModelScope.launch {
        orderDetail.postValue(Resource.Loading())
        val response = iRepository.getAllItemInOrder(idOrder)
        orderDetail.postValue(response)
    }

}