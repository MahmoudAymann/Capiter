package com.capiter.main.app

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.capiter.main.BuildConfig
import com.capiter.main.databinding.AppDataBindingComponent
import com.capiter.main.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

class BaseApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            initTimber()
        }
        DataBindingUtil.setDefaultComponent(AppDataBindingComponent())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        AppInjector.init(this)
    }

    private fun initTimber() {
        Timber.plant(object : DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return super.createStackElementTag(element) + "line: " + element.lineNumber
            }
        })
    }

    override fun activityInjector() = dispatchingAndroidInjector
}