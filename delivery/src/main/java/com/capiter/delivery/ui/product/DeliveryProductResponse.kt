package com.capiter.delivery.ui.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeliveryProductResponseItem(

    @field:SerializedName("product-quantity")
    val productQuantity: String = "0",

    @field:SerializedName("product-price")
    val productPrice: String = "0",

    @field:SerializedName("product-id")
    val productId: String = "0",

    @field:SerializedName("order-name")
    val orderName: String? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("product-name")
    val productName: String? = null,

	@field:SerializedName("product-image-url")
	val productImageUrl: String? = null
) : Parcelable
