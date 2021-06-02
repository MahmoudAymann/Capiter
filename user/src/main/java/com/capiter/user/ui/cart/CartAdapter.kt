package com.capiter.user.ui.cart

import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.capiter.main.base.view.BaseAdapter
import com.capiter.user.R
import com.capiter.user.databinding.ItemCartViewBinding
import com.capiter.user.model.ProductItem

class CartAdapter(private val deleteItem: (ProductItem) -> Unit) : BaseAdapter<ProductItem>(), Filterable {

    override fun layoutRes(): Int = R.layout.item_cart_view
    override fun onViewHolderCreated(binding: ViewDataBinding, viewHolder: RecyclerView.ViewHolder) {
        (binding as ItemCartViewBinding).btnDeleteItem.setOnClickListener {
            mPosition = viewHolder.adapterPosition
            deleteItem(getItem(mPosition))
        }
    }

    var filteredItemsList = arrayListOf<ProductItem?>()
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                filteredItemsList = filterResults.values as ArrayList<ProductItem?>
                submitList(filteredItemsList.toMutableList())
            }

            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val queryString = charSequence?.toString()?.lowercase()
                val filterResults = FilterResults()
                filterResults.values = if (queryString.isNullOrEmpty()) {
                    mCurrentList
                } else
                    mCurrentList.filter {
                        it?.name?.lowercase()?.contains(queryString) ?: false
                    }
                return filterResults
            }
        }
    }






}