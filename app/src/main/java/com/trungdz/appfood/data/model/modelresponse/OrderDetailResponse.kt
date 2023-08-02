package com.trungdz.appfood.data.model.modelresponse

import com.trungdz.appfood.data.model.ItemInOrder

data class OrderDetailResponse(
    val datetime: String,
    val itemList: List<ItemInOrder>,
    val name_payment: String,
    val status: Int,
    val total: Int,
)