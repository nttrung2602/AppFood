package com.trungdz.appfood.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.model.modelresponse.ItemInCart
import com.trungdz.appfood.data.model.modelresponse.ItemListInCartResponse
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.domain.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Qualifier

@HiltViewModel
class CartFragmentViewModel @Inject constructor(val iRepository: IRepository) : ViewModel() {

    val itemListInCart: MutableLiveData<Resource<ItemListInCartResponse>> = MutableLiveData()
    val totalLiveData: MutableLiveData<Int> = MutableLiveData(0)
    private var total = 0
    val priceDiscountLiveData: MutableLiveData<Int> = MutableLiveData(0)
    private var priceDiscount = 0f
    val finalTotalPriceLiveData: MutableLiveData<Int> = MutableLiveData(0)
    val statusMessageIncreaseItemInCart: MutableLiveData<Resource<MessageResponse>> =
        MutableLiveData()
    val statusMessageDecreaseItemInCart: MutableLiveData<Resource<MessageResponse>> =
        MutableLiveData()
    val statusMessageDeleteOneItemInCart: MutableLiveData<Resource<MessageResponse>> =
        MutableLiveData()
    val statusMessageUpdateItemInCart: MutableLiveData<Resource<MessageResponse>> =
        MutableLiveData()

    private fun totalPrice() {
        total = 0
        if (itemListInCart.value != null) {
            itemListInCart.value!!.data?.itemList?.map {
                total += (it.amount * it.price)
            }

            totalLiveData.postValue(total)
        }
    }

    private fun priceDiscount(discountPercent: Float = 0f) {
        priceDiscount = 0f
        priceDiscount = total.toFloat() * discountPercent
        priceDiscountLiveData.postValue(priceDiscount.toInt())
    }

    fun finalTotalPrice(discountPercent: Float = 0f) {
        totalPrice()
        priceDiscount(discountPercent)
        finalTotalPriceLiveData.postValue(total - priceDiscount.toInt())
    }

    fun getAllItemInCart() = viewModelScope.launch {
        itemListInCart.postValue(Resource.Loading())

        val response = iRepository.getAllItemInCart()
        // delete item have quantity = 0
        response.data?.let {
            val itemList = it.itemList
            var newItemList = ArrayList<ItemInCart>()
            itemList.map { item ->
                if (item.quantity < 1) deleteOneItemInCart(item.id_item)
                else newItemList.add(item)
            }

            // catch condition below. Eg: when cart history have 20, but actually item quantity just only 10 then update counter quantity of item equal 1
            newItemList.forEach { item ->
                if (item.amount > item.quantity) {
                    item.amount = 1
                    updateItemInCart(item.id_item, 1)
                }
            }

            it.itemList = newItemList as List<ItemInCart>
        }

        itemListInCart.postValue(response)
    }

    fun increaseNumItemInCart(idItem: Int) = viewModelScope.launch {
        statusMessageIncreaseItemInCart.postValue(Resource.Loading())
        val response = iRepository.increaseNumItemInCart(idItem)
        statusMessageIncreaseItemInCart.postValue(response)
    }

    fun decreaseNumItemInCart(idItem: Int) = viewModelScope.launch {
        statusMessageDecreaseItemInCart.postValue(Resource.Loading())
        val response = iRepository.decreaseNumItemInCart(idItem)
        statusMessageDecreaseItemInCart.postValue(response)
    }

    fun deleteOneItemInCart(idItem: Int) = viewModelScope.launch {
        statusMessageDeleteOneItemInCart.postValue(Resource.Loading())
        val response = iRepository.deleteOneItemInCart(idItem)
        statusMessageDeleteOneItemInCart.postValue(response)
    }

    private fun updateItemInCart(idItem: Int, quantity: Int) = viewModelScope.launch {
        statusMessageUpdateItemInCart.postValue(Resource.Loading())
        val response = iRepository.updateItemInCart(idItem, quantity)
        statusMessageUpdateItemInCart.postValue(response)
    }
}