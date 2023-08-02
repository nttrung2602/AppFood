package com.trungdz.appfood.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trungdz.appfood.data.model.OrderItem
import com.trungdz.appfood.data.model.modelresponse.OrdersListResponse
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.domain.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryFragmentViewModel @Inject constructor(val iRepository: IRepository) :
    ViewModel() {

    val orderList: MutableLiveData<Resource<OrdersListResponse>> = MutableLiveData()
    val totalCostAllOrder: MutableLiveData<Int> = MutableLiveData()
    val totalNumOrder: MutableLiveData<Int> = MutableLiveData()

    fun totalCostAllOrder() {
        var sum = 0
        totalCostAllOrder.postValue(sum)
        (orderList.value?.data as ArrayList<OrderItem>).forEach {
            sum += it.total
        }


        totalCostAllOrder.postValue(sum)
    }

    fun totalNumOrder() {
        totalNumOrder.postValue(0)
        var num = (orderList.value?.data as ArrayList<OrderItem>).size ?: 0

        totalNumOrder.postValue(num)
    }

    fun getAllOrder() = viewModelScope.launch {
        orderList.postValue(Resource.Loading())
        val response = iRepository.getAllOrder()
        orderList.postValue(response)
    }


}