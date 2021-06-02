package com.capiter.user.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.capiter.main.base.view.BaseParcelable

@Entity
data class ProductItem(
    @PrimaryKey
    val id: String,
    val name: String? = null,
    val price: String? = null,
    val image: String? = null,
    var quantity: String? = null
) : BaseParcelable {
    override fun unique() = id
    override fun <T> areContentsTheSame(obj: T): Boolean {
        (obj as ProductItem).let {
            return price == it.price
                    && name == it.name
                    && image == it.image
                    && quantity == it.quantity
        }
    }

    companion object {
        fun convertFromSource(item: ProductsResponseItem?): ProductItem =
            ProductItem(
                item?.id ?: "0",
                item?.name,
                item?.price.toString(),
                item?.imageUrl,
                item?.qty.toString()
            )
    }

}