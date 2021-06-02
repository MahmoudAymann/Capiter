package com.capiter.main.util

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.capiter.main.R

object DialogsExtensions {
    fun Context.showErrorDialog(message: String?, onClick: () -> Unit = {}) {
        SweetAlertDialog(
            this,
            SweetAlertDialog.ERROR_TYPE
        )
            .setContentText(message)
            .setConfirmButton(getString(R.string.continue_)) { sDialog ->
                sDialog.closeDialog()
                onClick()
            }
            .show()
    }

    fun Context.showSuccessfulDialog(message: String?, onClick: () -> Unit = {}) {
        val dialog = SweetAlertDialog(
            this,
            SweetAlertDialog.SUCCESS_TYPE
        )
            .setConfirmButton(getString(R.string.continue_)) { sDialog ->
                sDialog.closeDialog()
                onClick()
            }
            .setConfirmButtonBackgroundColor(getColorFromRes(R.color.colorPrimary))
        message?.let {
            dialog.setContentText(message)
        }
        dialog.show()
    }

}