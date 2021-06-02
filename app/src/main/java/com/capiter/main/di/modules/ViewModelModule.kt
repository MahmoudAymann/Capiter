package com.capiter.main.di.modules

import androidx.lifecycle.ViewModelProvider
import com.capiter.main.di.helper.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory


}
