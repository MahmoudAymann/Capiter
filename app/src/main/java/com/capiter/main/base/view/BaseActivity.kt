package com.capiter.main.base.view

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.capiter.main.BR
import com.capiter.main.util.bindView
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * Created by MahmoudAyman on 7/17/2020.
 **/

abstract class BaseActivity<B : ViewDataBinding, VM : ViewModel> : DaggerAppCompatActivity() {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    val showProgress = ObservableBoolean()
    protected abstract val mViewModel: VM

    @IdRes
    abstract fun fragmentContainerId(): Int

    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindView()
        binding.setVariable(BR.viewModel, mViewModel)
    }


    override fun supportFragmentInjector() = dispatchingAndroidInjector

}
