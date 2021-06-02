package com.capiter.user.ui.cart

import android.app.Application
import androidx.databinding.ObservableBoolean
import com.capiter.main.base.viewmodel.AndroidBaseViewModel
import com.capiter.main.util.AppUtil
import com.capiter.main.util.Resource
import com.capiter.user.R
import com.capiter.user.model.ProductItem
import javax.inject.Inject

class CartViewModel @Inject constructor(
    app: Application, private val cartRepository: CartRepository
) : AndroidBaseViewModel(app) {

    val adapter = CartAdapter(::onItemDeleteClick)
    val deletedIds = arrayListOf<String>()
    val request = CartRequest()
    val obsEnableButton = ObservableBoolean(true)
    val obsShowEmptyView = ObservableBoolean()
    private fun onItemDeleteClick(item: ProductItem) {
        postResult(Resource.loading())
        cartRepository.deleteItemFromCart(item, {
            postResult(Resource.success())
            deletedIds.add(item.id)
            adapter.removeItem(item) {
                if (it)
                    obsShowEmptyView.set(true)
            }
        },{
            postResult(Resource.success())
        })
    }

    fun onSubmitClick() {
        if (!AppUtil.isNetworkAvailable(app)) {
            postResult(Resource.error(msg = app.getString(R.string.network_error)))
            return
        }
        request.listOfProducts = adapter.currentList
        if(!request.isValid()){
            postResult(Resource.error(msg = app.getString(R.string.all_data_required)))
            return
        }
        obsEnableButton.set(false)
        postResult(Resource.loading())
        cartRepository.submitOrder(request.orderName, request.listOfProducts, {
            postResult(Resource.success("done"))
        }){
            obsEnableButton.set(true)
            postResult(Resource.error(it))
        }
    }

    private fun loadDataOnAdapter(results: List<ProductItem?>?) {
        results?.let {
            adapter.setList(it)
        }
    }

    init {
        getData()
    }

    private fun getData() {
        postResult(Resource.loading())
        cartRepository.getAllInCart ({
            loadDataOnAdapter(it)
            postResult(Resource.success())
        }){
            postResult(Resource.error(it))
        }
    }

}

