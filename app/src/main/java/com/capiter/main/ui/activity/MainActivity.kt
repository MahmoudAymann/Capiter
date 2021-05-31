package com.capiter.main.ui.activity


import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.NavigationRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.capiter.main.R
import com.capiter.main.base.view.BaseActivity
import com.capiter.main.constants.Codes
import com.capiter.main.constants.ConstString
import com.capiter.main.databinding.ActivityMainBinding
import com.capiter.main.util.AppUtil
import com.capiter.main.util.navigateSafe
import com.capiter.main.util.observe
import com.capiter.main.util.showExitDialog
import com.capiter.user.ui.product.ProductFragmentDirections
import timber.log.Timber
import java.util.*

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),
    NavController.OnDestinationChangedListener {

    override val mViewModel: MainViewModel by viewModels()
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNavController()
        mViewModel.apply {
            observe(mutableLiveData) {
                when (it) {
                    Codes.BACK_BUTTON_PRESSED -> onBackPressed()
                    Codes.CART_SCREEN -> navigateSafe(ProductFragmentDirections.actionHomeFragmentToCartFragment())
                }
            }
        }
        binding.obsProgressBar = showProgress
    }

    private fun setupNavController() {
        if (AppUtil.isUserApp()) {
            setNavHost(R.id.productFragment, R.navigation.user_nav_graph)
        }else{
            setNavHost(R.id.productFragment, R.navigation.delivery_nav_graph)
        }
    }


    private fun setNavHost(startingFragId:Int,@NavigationRes navHostId:Int){
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        val destination = intent.getIntExtra(ConstString.DESTINATION_NAME, startingFragId)
        val navGraph = navController?.navInflater?.inflate(navHostId)
        val bundle: Bundle? =
            intent.getBundleExtra(ConstString.BUNDLE_FRAGMENT) ?: intent.extras
        navGraph?.startDestination = destination
        navController?.setGraph(navGraph!!, bundle)
        navController?.addOnDestinationChangedListener(this)
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

}
