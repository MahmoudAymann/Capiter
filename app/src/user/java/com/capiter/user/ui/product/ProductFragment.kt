package com.capiter.user.ui.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.capiter.main.R
import com.capiter.main.base.network.Status.*
import com.capiter.main.base.view.BaseFragment
import com.capiter.main.constants.ConstString
import com.capiter.main.databinding.FragmentProductsBinding
import com.capiter.main.di.Injectable
import com.capiter.main.util.AppUtil.listenForResult
import com.capiter.main.util.DialogsExtensions.showErrorDialog
import com.capiter.main.util.observe
import timber.log.Timber
import javax.inject.Inject

class ProductFragment : BaseFragment<FragmentProductsBinding, ProductViewModel>(), Injectable{
    override fun pageTitle(): String = getString(R.string.app_name)

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


    override fun onResume() {
        super.onResume()
        mViewModel.refreshData()
    }



}