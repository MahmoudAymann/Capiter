package com.capiter.main.databinding

import androidx.databinding.DataBindingComponent

/**
 * Created by MahmoudAyman on 6/18/2020.
 **/
class AppDataBindingComponent : DataBindingComponent{

    override fun getOtherViewsBinding(): OtherViewsBinding {
        return OtherViewsBinding()
    }
}