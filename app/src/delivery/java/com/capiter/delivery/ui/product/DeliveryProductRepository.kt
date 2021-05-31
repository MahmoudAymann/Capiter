package com.capiter.delivery.ui.product

import com.capiter.main.data.remote.UserApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class DeliveryProductRepository @Inject constructor(
    private val userApiService: UserApiService
) {
    fun getProductsApiObs(
        page: Int,
        onSuccessCallBack: (List<ProductsResponseItem>) -> Unit,
        onErrorCallBack: (String?) -> Unit
    ) {
        userApiService.getProductsObs(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessCallBack(it)
            }, {
                onErrorCallBack(it.localizedMessage)
            })
    }

    fun addToCart(item: ProductItem) {
        userDao.insert(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.e("")
                getAllInCart {}
            }, {
                Timber.e("$it")
            })
    }


    fun getAllInCart(callBack: (List<ProductItem>) -> Unit) {
        userDao.getAllProducts().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                Timber.e("$it")
                callBack(it)
            }, {
                Timber.e("$it")
            })
    }

    fun mapListTo(
        sourceList: List<ProductsResponseItem?>,
        callBack: (List<ProductItem>) -> Unit
    ) {
        val originalList = Observable.fromArray(sourceList)
        originalList.flatMap {
            Observable.fromIterable(it)
                .map { item -> ProductItem.convertFromSource(item) }
                .toList()
                .toObservable()
        }.subscribe {
            callBack(it)
        }
    }

}

