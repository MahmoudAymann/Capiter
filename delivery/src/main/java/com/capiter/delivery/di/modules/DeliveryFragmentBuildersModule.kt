package com.capiter.delivery.di.modules

import com.capiter.delivery.ui.product.DeliveryProductFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class DeliveryFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeDeliveryProductFragment(): DeliveryProductFragment
}
