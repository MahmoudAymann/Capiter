package com.capiter.delivery.model

import com.capiter.main.base.view.BaseParcelable

abstract class ListItem : BaseParcelable {
    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_BODY = 1
    }

    abstract fun getType(): Int
}