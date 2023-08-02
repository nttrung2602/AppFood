package com.trungdz.appfood.data.model.chart

data class OrderOfChart(
    val Order_details: List<OrderDetailOfChart>,
    val datetime: String,
    val total: Int
)