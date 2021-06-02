package com.capiter.delivery.ui.activity

import android.app.Application
import androidx.databinding.ObservableField
import com.capiter.main.base.viewmodel.AndroidBaseViewModel

class DeliveryMainViewModel(app: Application) : AndroidBaseViewModel(app) {

    val obsTitle = ObservableField<String>()


}