package com.trungdz.appfood.presentation.di

import com.trungdz.appfood.data.repository.IRepositoryImp
import com.trungdz.appfood.data.repository.datasource.IAppFoodRemoteDatasource
import com.trungdz.appfood.domain.repository.IRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun providesIRepository(iAppFoodRemoteDatasource: IAppFoodRemoteDatasource): IRepository {
        return IRepositoryImp(iAppFoodRemoteDatasource)
    }
}