package com.capiter.delivery.model

import com.capiter.main.base.view.BaseParcelable

data class HeaderItem(var id:Int = 0, var name: String? = null): ListItem(), BaseParcelable{
    override fun getType() = TYPE_HEADER
    override fun unique() = id

    override fun <T> areContentsTheSame(newItem: T): Boolean {
        return true
    }
}
