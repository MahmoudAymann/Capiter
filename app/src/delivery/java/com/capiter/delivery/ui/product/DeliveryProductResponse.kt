package com.capiter.delivery.ui.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeliveryProductResponseItem(

	@field:SerializedName("product-quantity")
	val productQuantity: Int? = null,

	@field:SerializedName("product-price")
	val productPrice: Int? = null,

	@field:SerializedName("product-id")
	val productId: String? = null,

	@field:SerializedName("order-name")
	val orderName: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("product-name")
	val productName: String? = null,

	@field:SerializedName("product-image-url")
	val productImageUrl: String? = null
) : Parcelable
