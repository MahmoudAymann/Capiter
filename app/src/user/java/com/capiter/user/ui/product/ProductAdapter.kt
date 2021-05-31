package com.capiter.user.ui.product

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.capiter.main.R
import com.capiter.main.base.view.BaseAdapter
import com.capiter.main.databinding.ItemProductViewBinding

class ProductAdapter(val addToCartClick: (ProductItem) -> Unit) : BaseAdapter<ProductItem>() {

    override fun layoutRes(): Int = R.layout.item_product_view
    override fun onViewHolderCreated(
        binding: ViewDataBinding,
        viewHolder: RecyclerView.ViewHolder
    ) {
        (binding as ItemProductViewBinding).btnAddToCart.setOnClickListener {
            mPosition = viewHolder.adapterPosition
            addToCartClick(getItem(mPosition))
        }
    }
}