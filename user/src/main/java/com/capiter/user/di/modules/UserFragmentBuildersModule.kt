package com.capiter.user.di.modules

import com.capiter.user.ui.cart.CartFragment
import com.capiter.user.ui.product.ProductFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class UserFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeProductFragment(): ProductFragment

    @ContributesAndroidInjector
    abstract fun contributeCartFragment(): CartFragment
}
