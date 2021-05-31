package com.capiter.delivery.ui.product

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.capiter.main.base.view.BaseParcelable

@Entity
data class DeliveryProductItem(
    @PrimaryKey
    val id: String,
    val name: String? = null,
    val price: String? = null,
    val image: String? = null,
    val quantity: String? = null
) : BaseParcelable {
    override fun unique() = id

    companion object {
        fun convertFromSource(item: ProductsResponseItem?): DeliveryProductItem =
            DeliveryProductItem(
                item?.id ?: "0",
                item?.name,
                item?.price.toString(),
                item?.imageUrl,
                item?.qty.toString()
            )
    }

}