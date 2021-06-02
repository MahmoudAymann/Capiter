package com.capiter.delivery.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.capiter.delivery.ui.product.DeliveryProductResponseItem

@Entity
data class DeliveryProductItem(
    @PrimaryKey
    val id: String,
    val name: String? = null,
    val price: String? = null,
    val image: String? = null,
    val quantity: String? = null
) : ListItem() {
    override fun unique() = id
    override fun <T> areContentsTheSame(obj: T): Boolean {
        (obj as DeliveryProductItem).let {
            return price == it.price
                    && name == it.name
                    && image == it.image
                    && quantity == it.quantity
        }
    }

    companion object {
        fun convertFromSource(item: DeliveryProductResponseItem?): DeliveryProductItem =
            DeliveryProductItem(
                item?.id ?: "0",
                item?.productName,
                item?.productPrice.toString(),
                item?.productImageUrl,
                item?.productQuantity.toString()
            )
    }

    override fun getType() = TYPE_BODY

    override fun toString(): String {
        return "$quantity"
    }

}