package com.capiter.user.ui.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.capiter.main.base.view.BaseFragment
import com.capiter.main.di.helper.Injectable
import com.capiter.main.util.DialogsExtensions.showErrorDialog
import com.capiter.main.util.Status.*
import com.capiter.main.util.castToActivity
import com.capiter.main.util.observe
import com.capiter.user.R
import com.capiter.user.databinding.FragmentProductsBinding
import com.capiter.user.ui.activity.UserMainActivity
import javax.inject.Inject

class ProductFragment : BaseFragment<FragmentProductsBinding, ProductViewModel>(), Injectable {
    override fun pageTitle(): String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val mViewModel: ProductViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.apply {
            observe(resultLiveData) {
                when (it?.status) {
                    SUCCESS -> {
                        showProgress(false)
                    }
                    MESSAGE -> {
                        showProgress(false)
                        activity?.showErrorDialog(it.message)
                    }
                    LOADING -> showProgress(true)
                }
            }
        }
        castToActivity<UserMainActivity> {
            it?.changeTitle(getString(R.string.app_name))
        }
    }


    override fun onResume() {
        super.onResume()
        mViewModel.refreshData()
    }



}