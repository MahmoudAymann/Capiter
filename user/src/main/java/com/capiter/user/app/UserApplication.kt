package com.capiter.user.app

import android.app.Activity
import com.capiter.main.app.BaseApplication
import com.capiter.main.di.helper.AppInjector
import com.capiter.user.di.DaggerUserAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class UserApplication : BaseApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerUserAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector
}