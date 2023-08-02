package com.trungdz.appfood.presentation.di

import com.trungdz.appfood.data.api.AppFoodApiService
import com.trungdz.appfood.data.repository.datasource.IAppFoodRemoteDatasource
import com.trungdz.appfood.data.repository.datasourceImp.IAppFoodRemoteDataSourceImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    @Provides
    @Singleton
    fun providesIAppFoodRemoteDataSource(appFoodApiService: AppFoodApiService):IAppFoodRemoteDatasource{
        return IAppFoodRemoteDataSourceImp(appFoodApiService)
    }
}