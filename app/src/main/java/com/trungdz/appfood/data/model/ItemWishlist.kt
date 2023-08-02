package com.trungdz.appfood.data.model

data class ItemWishlist(
    val description: String,
    val energy: Float,
    val id_item: Int,
    val image: String,
    val ingredient: String,
    val name: String,
    val price: Int
)