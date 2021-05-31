package com.capiter.user.ui.cart

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateOrderResponse(

	@field:SerializedName("CreateOrderResponse")
	val createOrderResponse: List<CreateOrderResponseItem?>? = null
) : Parcelable

@Parcelize
data class CreateOrderResponseItem(

	@field:SerializedName("product-id")
	val productId: String? = null,

	@field:SerializedName("_tags")
	val tags: String? = null,

	@field:SerializedName("_createdby")
	val createdby: String? = null,

	@field:SerializedName("_changed")
	val changed: String? = null,

	@field:SerializedName("product-quantity")
	val productQuantity: Int? = null,

	@field:SerializedName("_changedby")
	val changedby: String? = null,

	@field:SerializedName("product-price")
	val productPrice: Int? = null,

	@field:SerializedName("_created")
	val created: String? = null,

	@field:SerializedName("order-name")
	val orderName: String? = null,

	@field:SerializedName("_keywords")
	val keywords: List<String?>? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("product-name")
	val productName: String? = null,

	@field:SerializedName("_version")
	val version: Int? = null,

	@field:SerializedName("product-image-url")
	val productImageUrl: String? = null
) : Parcelable
