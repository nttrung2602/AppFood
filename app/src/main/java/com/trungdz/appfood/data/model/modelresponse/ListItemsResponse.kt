package com.trungdz.appfood.data.model.modelresponse

import com.trungdz.appfood.data.model.Item

data class ListItemsResponse(
    val totalItems: Int,
    var itemList: List<Item>
)