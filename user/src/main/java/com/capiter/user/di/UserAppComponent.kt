package com.capiter.user.di

import android.app.Application
import com.capiter.user.app.UserApplication
import com.capiter.user.di.modules.UserActivityBuildersModule
import com.capiter.user.di.modules.UserViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        UserActivityBuildersModule::class,
        UserAppModule::class,
        UserViewModelModule::class
    ]
)
interface UserAppComponent : AndroidInjector<UserApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): UserAppComponent
    }
}
