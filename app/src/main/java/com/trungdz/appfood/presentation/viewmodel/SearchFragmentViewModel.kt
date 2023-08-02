package com.trungdz.appfood.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trungdz.appfood.data.model.Item
import com.trungdz.appfood.data.model.modelresponse.ListItemsResponse
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.domain.repository.IRepository
import com.trungdz.appfood.presentation.eventWrapper.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(private val iRepository: IRepository) :
    ViewModel() {
    val itemList: MutableLiveData<Event<Resource<ListItemsResponse>>> = MutableLiveData()
    private var currentPage = 1
    private var totalPage = 0
    private var perpage = 12

    fun resetPage() {
        currentPage = 1
    }

    fun nextPage(): Boolean {
        if (currentPage < totalPage) {
            currentPage += 1
            return true
        }
        return false
    }

    fun getAllItemByName(name: String? = null) = viewModelScope.launch {
        itemList.postValue(Event(Resource.Loading()))
        val response = iRepository.getAllItemByName(currentPage, name)
        response.data?.let {
            totalPage = ceil(it.totalItems / perpage.toFloat()).toInt()
        }
        itemList.postValue(Event(response))
    }

    fun loadMoreItem(name: String) = viewModelScope.launch {
        val response = iRepository.getAllItemByName(currentPage, name)
        (itemList.value?.peekContent()?.data?.itemList as ArrayList).addAll(response.data?.itemList as ArrayList)
        itemList.postValue(itemList.value)
    }
}