package com.trungdz.appfood.data.model

data class ValidationResult(
    val successful: Boolean, val error: String?=null
)