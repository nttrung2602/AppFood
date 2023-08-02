package com.trungdz.appfood.presentation.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trungdz.appfood.data.model.modelresponse.ChartDataResponse
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.domain.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartFragmentViewModel @Inject constructor(private val iRepository: IRepository) :
    ViewModel() {

    val orderList: MutableLiveData<Resource<ChartDataResponse>> = MutableLiveData()

    fun getChart() = viewModelScope.launch {
        orderList.postValue(Resource.Loading())

        val response = iRepository.chart()

        orderList.postValue(response)

    }

}