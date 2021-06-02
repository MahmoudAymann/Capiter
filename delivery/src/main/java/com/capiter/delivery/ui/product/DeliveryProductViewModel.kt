package com.capiter.delivery.ui.product

import android.app.Application
import com.capiter.delivery.model.ListItem
import com.capiter.main.R
import com.capiter.main.base.viewmodel.AndroidBaseViewModel
import com.capiter.main.util.AppUtil
import com.capiter.main.util.Resource
import javax.inject.Inject

class DeliveryProductViewModel @Inject constructor(
    app: Application, private val productRepository: DeliveryProductRepository
) : AndroidBaseViewModel(app) {

    val adapter = DeliveryProductAdapter()

    private fun loadDataOnAdapter(results: List<ListItem?>?) {
        results?.let {
            adapter.setList(results)
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
        productRepository.getProductsApiObs(1,{
            loadDataOnAdapter(it)
            postResult(Resource.success())
        }) {
            postResult(Resource.error(it))
        }
    }

}