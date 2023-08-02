package com.trungdz.appfood.presentation.di

import android.content.Context
import android.content.SharedPreferences
import com.trungdz.appfood.data.util.ISharedPreferenceImp
import com.trungdz.appfood.data.util.cache.ISharedPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CacheModule {
    @Singleton
    @Provides
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("AppFood", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesSharedPreference(sharedPreferences: SharedPreferences): ISharedPreference {
        return ISharedPreferenceImp(sharedPreferences)
    }
}