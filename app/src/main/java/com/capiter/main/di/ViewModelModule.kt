package com.capiter.main.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capiter.main.viewmodel.ViewModelProviderFactory
import com.capiter.user.ui.cart.CartViewModel
import com.capiter.user.ui.product.ProductViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule{

    @Binds
    @IntoMap //to know that it will be map this using a specific key annotation
    @ViewModelKey(CartViewModel::class)
    abstract fun bindCartViewModel(cartViewModel: CartViewModel): ViewModel

    @Binds
    @IntoMap //to know that it will be map this using a specific key annotation
    @ViewModelKey(ProductViewModel::class)
    abstract fun bindProductViewModel(cartViewModel: ProductViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}
