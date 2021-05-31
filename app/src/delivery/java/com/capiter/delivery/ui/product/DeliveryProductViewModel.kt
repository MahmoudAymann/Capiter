package com.capiter.delivery.ui.product

import android.app.Application
import com.capiter.main.R
import com.capiter.main.base.network.Resource
import com.capiter.main.base.viewmodel.AndroidBaseViewModel
import com.capiter.main.util.AppUtil
import timber.log.Timber
import javax.inject.Inject

class DeliveryProductViewModel @Inject constructor(
    app: Application, private val productRepository: ProductRepository
) : AndroidBaseViewModel(app) {

    val adapter = ProductAdapter(::onItemClick)

    private fun onItemClick(item: ProductItem?) {
        Timber.e("${item?.price}")
    }

    private fun loadDataOnAdapter(results: List<ProductsResponseItem?>?) {
        results?.let {
            productRepository.mapListTo(it) {list->
                adapter.setList(list)
            }
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

}