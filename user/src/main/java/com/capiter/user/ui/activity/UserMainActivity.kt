package com.capiter.user.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.capiter.main.base.view.BaseActivity
import com.capiter.main.constants.Codes
import com.capiter.main.util.AppUtil.setNavHost
import com.capiter.main.util.LocalUtil
import com.capiter.main.util.navigateSafe
import com.capiter.main.util.observe
import com.capiter.main.util.showExitDialog
import com.capiter.user.R
import com.capiter.user.databinding.ActivityMainBinding
import com.capiter.user.ui.product.ProductFragmentDirections
import timber.log.Timber
import javax.inject.Inject

class UserMainActivity : BaseActivity<ActivityMainBinding, UserMainViewModel>(),
    NavController.OnDestinationChangedListener {

    @Inject
    lateinit var localUtil: LocalUtil

    override val mViewModel: UserMainViewModel by viewModels()
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localUtil.changeLanguage(this)
        setupNavController()
        mViewModel.apply {
            observe(mutableLiveData) {
                when (it) {
                    Codes.BACK_BUTTON_PRESSED -> onBackPressed()
                    Codes.CART_SCREEN -> navigateSafe(ProductFragmentDirections.actionProductFragmentToCartFragment())
                }
            }
        }
        binding.obsProgressBar = showProgress
    }

    private fun setupNavController() {
        setNavHost(R.id.productFragment, R.navigation.user_nav_graph) {
            this.navController = it
            it?.addOnDestinationChangedListener(this)
        }

    }


    fun changeTitle(title: String?) {
        title?.let {
            mViewModel.obsTitle.set(title)
        } ?: Timber.e("title is null")
    }


    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        destination.id.let { id ->
            mViewModel.setScreenPermissions(id)
        }
    }

    override fun onBackPressed() {
        when (navController?.currentDestination?.id) {
            R.id.productFragment -> showExitDialog()
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun fragmentContainerId(): Int = R.id.fragment_container_view

}