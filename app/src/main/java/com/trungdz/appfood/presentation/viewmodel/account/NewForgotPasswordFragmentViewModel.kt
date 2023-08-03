package com.trungdz.appfood.presentation.viewmodel.account

import androidx.lifecycle.ViewModel
import com.trungdz.appfood.domain.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewForgotPasswordFragmentViewModel @Inject constructor(iRepository: IRepository):ViewModel() {
}