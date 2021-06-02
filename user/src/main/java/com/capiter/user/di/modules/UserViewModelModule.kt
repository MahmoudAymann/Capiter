package com.capiter.user.di.modules

import androidx.lifecycle.ViewModel
import com.capiter.main.di.helper.ViewModelKey
import com.capiter.user.ui.cart.CartViewModel
import com.capiter.user.ui.product.ProductViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class UserViewModelModule {

    @Binds
    @IntoMap //to know that it will be map this using a specific key annotation
    @ViewModelKey(CartViewModel::class)
    abstract fun bindCartViewModel(cartViewModel: CartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    abstract fun bindProductViewModel(cartViewModel: ProductViewModel): ViewModel

}
