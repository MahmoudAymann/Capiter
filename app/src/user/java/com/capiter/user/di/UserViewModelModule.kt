package com.capiter.user.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capiter.main.di.ViewModelKey
import com.capiter.main.di.ViewModelModule
import com.capiter.main.viewmodel.ViewModelProviderFactory
import com.capiter.user.ui.cart.CartViewModel
import com.capiter.user.ui.product.ProductViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

//@Suppress("unused")
//abstract class UserViewModelModule : ViewModelModule(){
//
//    @Binds
//    @IntoMap //to know that it will be map this using a specific key annotation
//    @ViewModelKey(ProductViewModel::class)
//    abstract fun bindHomeViewModel(productViewModel: ProductViewModel): ViewModel
//
//    @Binds
//    @IntoMap //to know that it will be map this using a specific key annotation
//    @ViewModelKey(CartViewModel::class)
//    abstract fun bindCartViewModel(cartViewModel: CartViewModel): ViewModel
//
//}
