package com.capiter.delivery.ui.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.capiter.main.base.network.Status.*
import com.capiter.main.base.view.BaseFragment
import com.capiter.main.databinding.FragmentProductsBinding
import com.capiter.main.di.Injectable
import com.capiter.main.util.DialogsExtensions.showErrorDialog
import com.capiter.main.util.observe
import javax.inject.Inject

class DeliveryProductFragment : BaseFragment<FragmentProductsBinding, ProductViewModel>(), Injectable {
    override fun pageTitle(): String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val mViewModel: ProductViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.apply {
            observe(mutableLiveData) {
                when (it) {

                }
            }

            observe(resultLiveData) {
                when (it?.status) {
                    SUCCESS -> {
                        showProgress(false)
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    MESSAGE -> {
                        showProgress(false)
                        activity?.showErrorDialog(it.message)
                    }
                    LOADING -> showProgress(true)
                }
            }
        }
    }

}