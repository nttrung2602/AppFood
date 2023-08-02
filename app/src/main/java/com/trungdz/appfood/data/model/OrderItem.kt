package com.trungdz.appfood.data.model

data class OrderItem(
    val datetime: String,
    val description: String,
    val id_order: Int,
    val reviewingCount: Int,
    val status: Int,
    val total: Int
)