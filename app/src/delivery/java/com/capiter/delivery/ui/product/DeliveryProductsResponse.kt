package com.capiter.delivery.ui.product

import android.os.Parcelable
import com.capiter.main.base.view.BaseParcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductsResponseItem(

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String = "0",

	@field:SerializedName("image-url")
	val imageUrl: String? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@Expose
	val qty: Int? = 0
) : Parcelable, BaseParcelable {
	override fun unique() = id
}
