package com.trungdz.appfood.presentation.di

import com.trungdz.appfood.data.util.cache.ISharedPreference
import com.trungdz.appfood.domain.repository.IRepository
import com.trungdz.appfood.domain.usecase.AuthUseCase
import com.trungdz.appfood.presentation.viewmodel.HomeFragmentViewModel
import com.trungdz.appfood.presentation.viewmodel.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ViewModelModule {
    // khởi tạo 1 thể hiện viewmodel duy nhất
    // dùng để tiêm vào biến khi không khởi tạo ViewModel bằng từ khóa "by"
    @Provides
    @Singleton
    fun providesLoginViewModel(authUseCase: AuthUseCase,sharedPreference: ISharedPreference):LoginViewModel{
        return LoginViewModel(authUseCase,sharedPreference)
    }

//    @Provides
//    @Singleton
//    fun providesHomeFragment(sharedPreference: ISharedPreference,iRepository: IRepository):HomeFragmentViewModel{
//        return HomeFragmentViewModel(sharedPreference,iRepository)
//    }
}