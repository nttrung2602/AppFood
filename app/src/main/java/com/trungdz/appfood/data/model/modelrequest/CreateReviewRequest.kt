package com.trungdz.appfood.data.model.modelrequest

data class CreateReviewRequest(
    val rating: Int = 5,
    val comment: String? = null,
    val image: String? = null,
)
