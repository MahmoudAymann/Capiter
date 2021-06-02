package com.capiter.user.ui.activity

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.capiter.main.base.viewmodel.AndroidBaseViewModel
import com.capiter.main.constants.Codes
import com.capiter.user.R

class UserMainViewModel(app: Application) : AndroidBaseViewModel(app) {

    val obsTitle = ObservableField<String>()
    val obsShowCartButton = ObservableBoolean()
    val obsShowBackButton = ObservableBoolean()

    fun onCartClick() {
        setValue(Codes.CART_SCREEN)
    }

    fun onBackClick() {
        setValue(Codes.BACK_BUTTON_PRESSED)
    }

    fun setScreenPermissions(id: Int) {
        when (id) {
            R.id.productFragment -> mainScreenPermissions()
            else -> detailsScreenPermission()
        }
    }


    private fun mainScreenPermissions() {
        obsShowBackButton.set(false)
        obsShowCartButton.set(true)
    }

    private fun detailsScreenPermission() {
        obsShowBackButton.set(true)
        obsShowCartButton.set(false)
    }

}