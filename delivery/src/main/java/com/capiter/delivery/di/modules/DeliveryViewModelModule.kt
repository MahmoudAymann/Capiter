package com.capiter.delivery.di.modules

import androidx.lifecycle.ViewModel
import com.capiter.delivery.ui.product.DeliveryProductViewModel
import com.capiter.main.di.helper.ViewModelKey

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class DeliveryViewModelModule {

    @Binds
    @IntoMap //to know that it will be map this using a specific key annotation
    @ViewModelKey(DeliveryProductViewModel::class)
    abstract fun bindCartViewModel(cartViewModel: DeliveryProductViewModel): ViewModel
}
