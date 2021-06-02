package com.capiter.delivery.di

import android.app.Application
import com.capiter.delivery.app.DeliveryApplication
import com.capiter.delivery.di.modules.DeliveryActivityBuildersModule
import com.capiter.delivery.di.modules.DeliveryViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        DeliveryActivityBuildersModule::class,
        DeliveryAppModule::class,
        DeliveryViewModelModule::class
    ]
)
interface DeliveryAppComponent : AndroidInjector<DeliveryApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): DeliveryAppComponent
    }
}
