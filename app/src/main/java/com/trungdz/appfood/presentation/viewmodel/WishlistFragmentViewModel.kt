package com.trungdz.appfood.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.model.modelresponse.WishlistResponse
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.domain.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistFragmentViewModel @Inject constructor(val iRepository: IRepository) : ViewModel() {

    val wishlistList: MutableLiveData<Resource<WishlistResponse>> = MutableLiveData()
    val statusUpdateToWishlist: MutableLiveData<Resource<MessageResponse>> = MutableLiveData()

    fun getAllItemInWishList() = viewModelScope.launch {
        wishlistList.postValue(Resource.Loading())

        val response = iRepository.getAllItemInWishlist()
        wishlistList.postValue(response)

    }

    fun updateToItemInWishlist(idItem: Int) = viewModelScope.launch {
        statusUpdateToWishlist.postValue(Resource.Loading())

        val response = iRepository.updateItemInWishlist(idItem)
        statusUpdateToWishlist.postValue(response)
    }
}