package com.trungdz.appfood.domain.usecase

import android.util.Log
import com.trungdz.appfood.data.model.modelrequest.LoginRequest
import com.trungdz.appfood.data.model.modelresponse.LoginResponse
import com.trungdz.appfood.data.util.Resource
import com.trungdz.appfood.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthUseCase @Inject constructor(val iRepository: IRepository) {

    fun loginUser(infoLogin: LoginRequest): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = iRepository.loginUser(infoLogin)
            emit(response)
        } catch (e: HttpException) {
            Log.d("AuthUseCase", e.localizedMessage!!)
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            Log.d("AuthUseCase",e.localizedMessage!!)
            emit(Resource.Error("Couldn't reach server. Check your internet"))
        }
    }

}