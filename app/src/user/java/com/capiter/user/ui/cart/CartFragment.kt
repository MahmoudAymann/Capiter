package com.capiter.user.ui.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.capiter.main.R
import com.capiter.main.base.network.Status.*
import com.capiter.main.base.view.BaseFragment
import com.capiter.main.databinding.FragmentCartBinding
import com.capiter.main.di.Injectable
import com.capiter.main.util.DialogsExtensions.showErrorDialog
import com.capiter.main.util.DialogsExtensions.showSuccessfulDialog
import com.capiter.main.util.observe
import javax.inject.Inject

class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(), Injectable {

    override fun pageTitle(): String = getString(R.string.cart)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val mViewModel: CartViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.apply {
            observe(resultLiveData) {
                when (it?.status) {
                    SUCCESS -> {
                        showProgress(false)
                        it.data?.let {
                            activity?.showSuccessfulDialog(getString(R.string.order_done)){
                                closeFragment()
                            }
                        }
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