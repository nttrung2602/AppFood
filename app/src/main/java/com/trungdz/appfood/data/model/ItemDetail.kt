package com.trungdz.appfood.data.model

data class ItemDetail(
    val countComment: Int,
    val description: String,
    val energy: Float,
    val id_item: Int,
    val id_type: Int,
    val image: String,
    val ingredient: String,
    val name: String,
    val name_type: String,
    val price: Int,
    val quantity: Int,
    val rating: String,
    val status: Int
)