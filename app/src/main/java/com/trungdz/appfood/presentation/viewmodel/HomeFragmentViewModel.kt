package com.trungdz.appfood.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.model.modelresponse.ItemListInCartResponse
import com.trungdz.appfood.data.model.modelresponse.ListItemsResponse
import com.trungdz.appfood.data.model.modelresponse.ListTypesResponse
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.data.util.cache.ISharedPreference
import com.trungdz.appfood.domain.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val sharedPreference: ISharedPreference,
    private val iRepository: IRepository,
) :
    ViewModel() {
    init {
        Log.d("Khoi tao HomeViewModel", "1111")
    }

    val listTypes: MutableLiveData<Resource<ListTypesResponse>> = MutableLiveData()
    val listItem: MutableLiveData<Resource<ListItemsResponse>> = MutableLiveData()
    val itemListInCart: MutableLiveData<Resource<ItemListInCartResponse>> = MutableLiveData()
    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    var currentTypePosition: Int = 0
    private var totalItem = 0
    var totalPage = 0
    var currentPage = 1
    private val itemsPerPage = 12
    var statusMessageAddToCart: MutableLiveData<Resource<MessageResponse>> = MutableLiveData()

    fun resetPageNumber() {
        currentPage = 1
    }

    private fun setTotalPage() {
        totalPage = ceil(totalItem / itemsPerPage.toFloat()).toInt()
    }

    // Type
    fun getAllTypes() = viewModelScope.launch {
        listTypes.postValue(Resource.Loading())
        val response = iRepository.getAllTypes()
        listTypes.postValue(response)
    }

    // ItemInOrder
    fun getAllItem() = viewModelScope.launch {
        listItem.postValue(Resource.Loading())
        val response = iRepository.getAllItem(currentPage)
        currentPage += 1
        totalItem = response.data!!.totalItems
        setTotalPage()

        listItem.postValue(response)
    }

    fun getAllItemByIdType(idType: Int) = viewModelScope.launch {
        listItem.postValue(Resource.Loading())
        val response = iRepository.getAllItemByIdType(currentPage, idType)

        totalItem = response.data!!.totalItems
        setTotalPage()
        currentPage += 1
        listItem.postValue(response)
    }

    // Cart
    fun addToCart(idItem: Int, quantity: Int = 1) = viewModelScope.launch {
        statusMessageAddToCart.postValue(Resource.Loading())
        val response = iRepository.createItemInCart(idItem, quantity)
        statusMessageAddToCart.postValue(response)
    }


    // refresh
    fun refresh() = viewModelScope.launch {
        isRefreshing.postValue(true)
        resetPageNumber()
        currentTypePosition = 0
        getAllTypes()
        getAllItem()
        isRefreshing.postValue(false)
    }

    fun getAllItemInCart() = viewModelScope.launch {
        itemListInCart.postValue(Resource.Loading())

        val response = iRepository.getAllItemInCart()

        itemListInCart.postValue(response)
    }
}