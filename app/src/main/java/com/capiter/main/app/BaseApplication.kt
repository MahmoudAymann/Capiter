package com.capiter.main.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.capiter.main.BuildConfig
import com.capiter.main.databinding.AppDataBindingComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            initTimber()
        }
        DataBindingUtil.setDefaultComponent(AppDataBindingComponent())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun initTimber() {
        Timber.plant(object : DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                return super.createStackElementTag(element) + "line: " + element.lineNumber
            }
        })
    }

}