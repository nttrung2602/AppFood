package com.trungdz.appfood.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trungdz.appfood.data.model.modelresponse.ListReviewsResponse
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.domain.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewFragmentViewModel @Inject constructor(val iRepository: IRepository) : ViewModel() {

    val listReviews: MutableLiveData<Resource<ListReviewsResponse>> = MutableLiveData()

    fun getAllReviewByItem(idItem: Int) = viewModelScope.launch {
        listReviews.postValue(Resource.Loading())

        val response = iRepository.getAllReviewByItem(idItem)
        listReviews.postValue(response)
    }
}