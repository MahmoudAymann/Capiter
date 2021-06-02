package com.capiter.delivery.ui.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.capiter.delivery.databinding.FragmentDeliveryProductsBinding
import com.capiter.delivery.ui.activity.DeliveryMainActivity
import com.capiter.main.R
import com.capiter.main.base.view.BaseFragment
import com.capiter.main.di.helper.Injectable
import com.capiter.main.util.DialogsExtensions.showErrorDialog
import com.capiter.main.util.Status
import com.capiter.main.util.castToActivity
import com.capiter.main.util.observe
import javax.inject.Inject

class DeliveryProductFragment :
    BaseFragment<FragmentDeliveryProductsBinding, DeliveryProductViewModel>(),
    Injectable {
    override fun pageTitle(): String = getString(R.string.my_orders)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val mViewModel: DeliveryProductViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.apply {

            observe(resultLiveData) {
                when (it?.status) {
                    Status.SUCCESS -> {
                        showProgress(false)
                    }
                    Status.MESSAGE -> {
                        showProgress(false)
                        activity?.showErrorDialog(it.message)
                    }
                    Status.LOADING -> showProgress(true)
                }
            }
        }

        castToActivity<DeliveryMainActivity> {
            it?.changeTitle(getString(R.string.my_orders))
        }
    }

}