package com.trungdz.appfood.data.model

data class ItemInOrder(
    val amount: Int,
    val id_item: Int,
    val id_order: Int,
    val image: String,
    var reviewed: Int,
    val name: String,
    val price: Int,
    val quantity: Int
)