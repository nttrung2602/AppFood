package com.trungdz.appfood.presentation.di

import com.trungdz.appfood.data.api.AppFoodApiService
import com.trungdz.appfood.data.util.cache.ISharedPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

//    @Provides
//    @Singleton
//    fun providesRetrofit(): Retrofit {
//        val interceptor = HttpLoggingInterceptor().apply {
//            this.level = HttpLoggingInterceptor.Level.BODY
//        }
//
//        val client = OkHttpClient.Builder().apply {
//            this.addInterceptor(interceptor)
//        }.build()
//
//        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
//            .baseUrl("https://be-fastfood-production.up.railway.app/").build()
//    }

    @Provides
    @Singleton
    fun providesRetrofit(sharedPreference: ISharedPreference): Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor).addInterceptor(Interceptor() {
                val token = sharedPreference.getToken()

                var newRequest = it.request().newBuilder().addHeader("access_token", token).build()
                it.proceed(newRequest)
            })
        }.build()

        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(client)
            .baseUrl("http://10.0.2.2:3005/").build()
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): AppFoodApiService {
        return retrofit.create(AppFoodApiService::class.java)
    }
}