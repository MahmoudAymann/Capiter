package com.capiter.delivery.app

import android.app.Activity
import com.capiter.delivery.di.DaggerDeliveryAppComponent
import com.capiter.main.app.BaseApplication
import com.capiter.main.di.helper.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class DeliveryApplication : BaseApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerDeliveryAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector
}