package com.capiter.delivery.ui.product

import androidx.databinding.ViewDataBinding
import com.capiter.main.R
import com.capiter.main.base.view.BaseAdapter
import com.capiter.main.databinding.ItemProductViewBinding

class DeliveryProductAdapter(val addToCartClick: (ProductItem?) -> Unit) : BaseAdapter<ProductItem>() {

    override fun layoutRes(): Int = R.layout.item_product_view
    override fun onViewHolderCreated(binding: ViewDataBinding) {
        (binding as ItemProductViewBinding).btnAddToCart.setOnClickListener {
            addToCartClick(binding.item)
        }
    }
}