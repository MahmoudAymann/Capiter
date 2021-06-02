package com.capiter.delivery.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import com.capiter.delivery.R
import com.capiter.delivery.databinding.DeliveryActivityMainBinding
import com.capiter.main.base.view.BaseActivity
import com.capiter.main.util.AppUtil.setNavHost
import com.capiter.main.util.LocalUtil
import com.capiter.main.util.showExitDialog
import timber.log.Timber
import javax.inject.Inject

class DeliveryMainActivity : BaseActivity<DeliveryActivityMainBinding, DeliveryMainViewModel>() {

    override val mViewModel: DeliveryMainViewModel by viewModels()
    private var navController: NavController? = null

    @Inject
    lateinit var localUtil: LocalUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localUtil.changeLanguage(this)
        setupNavController()
        mViewModel.apply {
        }
        binding.obsProgressBar = showProgress
    }

    private fun setupNavController() {
        setNavHost(
            R.id.deliveryProductFragment,
            R.navigation.delivery_nav_graph
        ) {
            this.navController = it
        }
    }




    fun changeTitle(title: String?) {
        title?.let {
            mViewModel.obsTitle.set(title)
        } ?: Timber.e("title is null")
    }

    override fun onBackPressed() {
        when (navController?.currentDestination?.id) {
            R.id.deliveryProductFragment -> showExitDialog()
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun fragmentContainerId(): Int = R.id.fragment_container_view

}