package com.capiter.delivery.ui.product

import com.capiter.delivery.model.ListItem
import com.capiter.main.data.remote.DeliveryApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class DeliveryProductRepository @Inject constructor(
    private val deliveryApiService: DeliveryApiService
) {
    fun getProductsApiObs(
        page: Int,
        onSuccessCallBack: (List<ListItem>) -> Unit,
        onErrorCallBack: (String?) -> Unit
    ) {
        deliveryApiService.getProductsObs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mapToAdapterList(it) {
                }
            }, {
                onErrorCallBack(it.localizedMessage)
            })
    }


    private fun mapToAdapterList(
        serverList: List<DeliveryProductResponseItem>,
        callback: (List<ListItem>) -> Unit
    ) {
        val resultItems = arrayListOf<ListItem>()
        val listOfHeaders = arrayListOf<String?>()
        Observable.fromIterable(serverList).map {
            listOfHeaders.add(it.orderName)
        }
        Timber.e("${listOfHeaders.size}")

    }

}

