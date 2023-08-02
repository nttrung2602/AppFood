package com.trungdz.appfood.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trungdz.appfood.data.model.MessageResponse
import com.trungdz.appfood.data.model.modelrequest.CreateReviewRequest
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.domain.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewDialogViewModel @Inject constructor(private val iRepository: IRepository) : ViewModel() {

    val statusMessageCreateReview: MutableLiveData<Resource<MessageResponse>> = MutableLiveData()
    fun createReview(idItem: Int, idOrder: Int, reviewRequest: CreateReviewRequest) =
        viewModelScope.launch {
            statusMessageCreateReview.postValue(Resource.Loading())
            val response = iRepository.createReview(idItem, idOrder, reviewRequest)
            statusMessageCreateReview.postValue(response)
        }
}