package com.capiter.user.di.modules


import com.capiter.user.ui.activity.UserMainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class UserActivityBuildersModule {
    @ContributesAndroidInjector(modules = [UserFragmentBuildersModule::class])
    abstract fun contributeMainActivity(): UserMainActivity
}