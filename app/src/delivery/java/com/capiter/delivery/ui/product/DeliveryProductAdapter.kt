package com.capiter.delivery.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capiter.delivery.model.ListItem
import com.capiter.delivery.model.ListItem.Companion.TYPE_BODY
import com.capiter.delivery.model.ListItem.Companion.TYPE_HEADER
import com.capiter.main.BR
import com.capiter.main.R

class DeliveryProductAdapter :
    ListAdapter<ListItem, BaseViewHolder>(BaseItemCallback()) {

    var mCurrentList = arrayListOf<ListItem?>()

    var mPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_header_delivery_product_view,
                    parent,
                    false
                )
                BaseViewHolder(binding)
            }
            TYPE_BODY -> {
                val binding = DataBindingUtil.inflate<ViewDataBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_delivery_product_view,
                    parent,
                    false
                )
                BaseViewHolder(binding)
            }
            else -> throw IllegalStateException("no type supported")
        }


    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setList(newList: List<ListItem?>) {
        mCurrentList.clear()
        mCurrentList.addAll(newList)
        submitList(mCurrentList.toMutableList())
    }

    fun clearCurrentList() {
        mCurrentList.clear()
        submitList(mCurrentList.toMutableList())
    }

}

class BaseViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ListItem) {
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}

class BaseItemCallback : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem) =
        oldItem.unique().toString() == newItem.unique().toString()

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}
