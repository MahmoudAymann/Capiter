package com.capiter.delivery.di.modules


import com.capiter.delivery.ui.activity.DeliveryMainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class DeliveryActivityBuildersModule {
    @ContributesAndroidInjector(modules = [DeliveryFragmentBuildersModule::class])
    abstract fun contributeMainActivity(): DeliveryMainActivity
}