package com.capiter.delivery.ui.product

import com.capiter.delivery.api.DeliveryApiService
import com.capiter.delivery.model.DeliveryProductItem
import com.capiter.delivery.model.HeaderItem
import com.capiter.delivery.model.ListItem
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
            .flatMapIterable { list -> list }
            .toSortedList { l1, l2 ->
                (l1.orderName ?: "").compareTo(l2.orderName ?: "")
            }
            .subscribe({
                mapToAdapterList(it) {
                    onSuccessCallBack(it)
                }
            }, {
                onErrorCallBack(it.message)
            })
    }


    private fun mapToAdapterList(
        serverList: List<DeliveryProductResponseItem>,
        callback: (List<ListItem>) -> Unit
    ) {
        val resultItems = arrayListOf<ListItem>()

        Observable.fromIterable(serverList).map { item ->
            item.orderName?.let { orderName ->
                if (resultItems.contains(HeaderItem(name = orderName))) {// header exist-> add item
                    val bodyItem = DeliveryProductItem.convertFromSource(item)
                    resultItems.add(bodyItem)
                } else { //it's header
                    val headerItem = HeaderItem(name = orderName)
                    resultItems.add(headerItem)
                }
            }
        }.subscribe({
            callback(resultItems)
        }, {
            Timber.e("error in parsing")
        })
    }


}

