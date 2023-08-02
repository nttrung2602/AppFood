package com.trungdz.appfood.presentation.di

import com.trungdz.appfood.domain.repository.IRepository
import com.trungdz.appfood.domain.usecase.AuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun providesAuthUseCase(iRepository: IRepository): AuthUseCase {
        return AuthUseCase(iRepository)
    }
}