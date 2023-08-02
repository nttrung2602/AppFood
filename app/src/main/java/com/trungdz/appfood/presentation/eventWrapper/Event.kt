package com.trungdz.appfood.presentation.eventWrapper

open class Event< T>(private val content: T) {
    var hasBeenHandled = false

    fun setHandled() {
        hasBeenHandled = true
    }

//    fun getContentIfNotHandle(): T? {
//        return if (!hasBeenHandled) {
//            hasBeenHandled = true
//            content
//        } else null
//    }

    fun peekContent(): T = content
}