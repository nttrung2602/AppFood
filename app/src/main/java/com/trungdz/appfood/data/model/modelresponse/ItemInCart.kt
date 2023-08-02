package com.trungdz.appfood.data.model.modelresponse

data class ItemInCart(
    var amount: Int,
    val id_cart: Int,
    val id_item: Int,
    val image: String,
    val name: String,
    val price: Int,
    var quantity: Int
)