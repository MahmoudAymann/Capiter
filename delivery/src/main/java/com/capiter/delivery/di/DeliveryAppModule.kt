package com.capiter.delivery.di

import com.capiter.delivery.api.DeliveryApiService
import com.capiter.main.di.modules.SharedAppModule
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(
    includes = [
        SharedAppModule::class
    ]
)
class DeliveryAppModule {

    @Singleton
    @Provides
    fun provideUserApiService(retrofit: Retrofit): DeliveryApiService {
        return retrofit
            .create(DeliveryApiService::class.java)
    }

}