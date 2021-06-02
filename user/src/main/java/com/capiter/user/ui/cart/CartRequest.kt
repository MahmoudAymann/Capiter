package com.capiter.user.ui.cart

import com.capiter.user.model.ProductItem
import com.google.gson.annotations.SerializedName


data class CartRequest(var orderName: String? = null, var listOfProducts: List<ProductItem?>?=null) {
    fun isValid(): Boolean {
        return !orderName.isNullOrBlank() && !listOfProducts.isNullOrEmpty()
    }
}

data class OrderList(
    @SerializedName("order-name")
    var orderName: String? = null,
    @SerializedName("product-name")
    var productName: String? = null,
    @SerializedName("product-id")
    var id: String? = null,
    @SerializedName("product-price")
    var price: Int? = null,
    @SerializedName("product-image-url")
    var image: String? = null,
    @SerializedName("product-quantity")
    var quantity: Int? = 0,
)