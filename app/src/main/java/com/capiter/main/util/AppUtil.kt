package com.capiter.main.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.capiter.main.base.view.BaseActivity
import com.capiter.main.constants.ConstString

/**
 * Created by MahmoudAyman on 8/25/2020.
 **/

object AppUtil {

    fun BaseActivity<*, *>.setNavHost(
        startingFragId: Int,
        @NavigationRes navHostId: Int,
        callBack: (NavController?) -> Unit = {}
    ) {
        val navController: NavController?
        val navHostFragment =
            supportFragmentManager.findFragmentById(fragmentContainerId()) as NavHostFragment
        navController = navHostFragment.navController
        val destination = intent.getIntExtra(ConstString.DESTINATION_NAME, startingFragId)
        val navGraph = navController.navInflater.inflate(navHostId)
        val bundle: Bundle? =
            intent.getBundleExtra(ConstString.BUNDLE_FRAGMENT) ?: intent.extras
        navGraph.startDestination = destination
        navController.setGraph(navGraph, bundle)
        callBack(navController)
    }

    fun Fragment.setResultToFragment(key: String, it: Any?) {
        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(key, it)
    }

    @SuppressLint("ObsoleteSdkInt")
    fun isOldDevice(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
    }

    @SuppressLint("MissingPermission")
    @Suppress("DEPRECATION")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

}