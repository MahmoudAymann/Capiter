package com.capiter.main.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.capiter.main.BuildConfig
import com.capiter.main.constants.ConstString
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by MahmoudAyman on 8/25/2020.
 **/

object AppUtil {
    fun <T : Any> Fragment.listenForResult(key: String, callback: (T?) -> Unit) {
        //Observe the result to be set by Fragment B in the stateHandle of the currentBackStackEntry
        val currentBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<T>(key)
            ?.observe(currentBackStackEntry, Observer { result ->
                callback(result)
            })
    }
    fun Fragment.setResultToFragment(key: String, it: Any?) {
        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(key, it)
    }
    fun isOldDevice():Boolean{
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
    }

    fun Context.getFont(@FontRes fontRes:Int) : Typeface? {
        return ResourcesCompat.getFont(this, fontRes)
    }

    private fun getMinDate(date: String): Calendar {
        val day = date.split("-").toTypedArray()
        val c: Calendar = getCurrentCalInstance()
        c.set(day[0].toInt(), day[1].toInt()-1, day[2].toInt())
        return c
    }

    fun getDaysInMonth(c: Calendar, year: Int, month: Int): Int {
        c[Calendar.YEAR] = year
        c[Calendar.MONTH] = month
        return c.getActualMaximum(Calendar.DATE)
    }

    private fun getCurrentCalInstance(): Calendar {
        val mCalendar = Calendar.getInstance()
        return mCalendar
    }

    fun <T> setSpinner(spinner:Spinner, list: ArrayList<T> = ArrayList(), liveData: MutableLiveData<T?>) {
        val adapter = ArrayAdapter(
            spinner.context,
            android.R.layout.simple_spinner_dropdown_item,
            list
        )
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                liveData.value = parent.selectedItem as T
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    fun openAppInStore(context: FragmentActivity) {
        val uri = Uri.parse("market://details?id=" + context.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            context.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)
                )
            )
        }
    }

    @Suppress("DEPRECATION")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
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

    fun isUserApp() : Boolean = BuildConfig.FLAVOR == ConstString.USER_APP


}