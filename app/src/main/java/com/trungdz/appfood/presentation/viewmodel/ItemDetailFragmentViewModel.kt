package com.trungdz.appfood.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.model.modelresponse.ItemDetailResponse
import com.trungdz.appfood.data.model.modelresponse.WishlistResponse
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.data.util.cache.ISharedPreference
import com.trungdz.appfood.domain.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailFragmentViewModel @Inject constructor(
    val sharedPreference: ISharedPreference,
    val iRepository: IRepository,
) : ViewModel() {

    val listItemInWishlist: MutableLiveData<Resource<WishlistResponse>> = MutableLiveData()
    val itemDetailL: MutableLiveData<Resource<ItemDetailResponse>> = MutableLiveData()
    val statusUpdateToWishList: MutableLiveData<Resource<MessageResponse>> = MutableLiveData()
    val statusAddToCart:MutableLiveData<Resource<MessageResponse>> = MutableLiveData()
    var quantity: Int = 0

    fun getDetailItem(idItem: Int) = viewModelScope.launch {
        itemDetailL.postValue(Resource.Loading())

        val response = iRepository.getDetailItem(idItem)

        itemDetailL.postValue(response)
        response.data?.let {
            var itemDetail = it[0]
            quantity = itemDetail.quantity
        }

    }

    fun addToCart(idItem: Int,quantity:Int=1)=viewModelScope.launch {
        statusAddToCart.postValue(Resource.Loading())
        val response=iRepository.createItemInCart(idItem,quantity)
        statusAddToCart.postValue(response)
    }

    fun getAllItemInWishlist() = viewModelScope.launch {
        listItemInWishlist.postValue(Resource.Loading())

        val response = iRepository.getAllItemInWishlist()

        listItemInWishlist.postValue(response)
    }

    fun updateToWishList(idItem: Int) = viewModelScope.launch {
        statusUpdateToWishList.postValue(Resource.Loading())

        val response = iRepository.updateItemInWishlist(idItem)

        statusUpdateToWishList.postValue(response)
    }
}