package com.capiter.user.ui.product

import android.app.Application
import com.capiter.main.base.viewmodel.AndroidBaseViewModel
import com.capiter.main.util.AppUtil
import com.capiter.main.util.Resource
import com.capiter.user.R
import com.capiter.user.model.ProductItem
import javax.inject.Inject

class ProductViewModel @Inject constructor(
    app: Application, private val productRepository: ProductRepository
) : AndroidBaseViewModel(app) {

    val adapter = ProductAdapter(::onAddToCartClick)

    private fun onAddToCartClick(item: ProductItem?) {
        productRepository.addToCart(item) {
            adapter.updateItem(it)
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
        if (!AppUtil.isNetworkAvailable(app)) {
            postResult(Resource.error(msg = app.getString(R.string.network_error)))
            return
        }
        postResult(Resource.loading())
        productRepository.getProductsApiObs(1, {
            loadDataOnAdapter(it)
            postResult(Resource.success())
        }) {
            postResult(Resource.error(it))
        }
    }

    fun refreshData() {
        productRepository.updateListFromCart(adapter.currentList){
            adapter.setList(it)
        }
    }

}