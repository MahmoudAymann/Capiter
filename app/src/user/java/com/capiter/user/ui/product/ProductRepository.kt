package com.capiter.user.ui.product

import com.capiter.main.data.local.UserDao
import com.capiter.main.data.remote.UserApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val userDao: UserDao
) {
    fun addToCart(
        item: ProductItem?,
        onSuccessCallBack: (ProductItem) -> Unit
    ) {
        if (item == null) return
        item.quantity = item.quantity?.toInt()?.plus(1).toString()
        userDao.insert(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessCallBack(item)
            }, {
                Timber.e("$it")
            })
    }


    fun getProductsApiObs(
        page: Int,
        onSuccessCallBack: (List<ProductItem?>) -> Unit,
        onErrorCallBack: (String?) -> Unit
    ) {
        userApiService.getProductsObs(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .take(2)
            .subscribe({
                mapListTo(it) { list ->
                    onSuccessCallBack(list)
                }
            }, {
                onErrorCallBack(it.localizedMessage)
            })
    }

    private fun getAllInCart(callBack: (List<ProductItem>) -> Unit) {
        userDao.getAllProducts().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                callBack(it)
            }, {
                Timber.e("$it")
            })
    }

    private fun mapListTo(
        sourceList: List<ProductsResponseItem?>,
        callBack: (List<ProductItem?>) -> Unit
    ) {
        val originalList = Observable.fromArray(sourceList)
        originalList.flatMap {
            Observable.fromIterable(it)
                .map { item -> ProductItem.convertFromSource(item) }
                .toList()
                .toObservable()
        }.subscribe {
            updateListFromCart(it) { list ->
                callBack(list)
            }
        }
    }

    fun updateListFromCart(
        serverList: List<ProductItem?>,
        callback: (List<ProductItem?>) -> Unit
    ) {
        if (serverList.isEmpty()) return
        getAllInCart { cartList ->
            val obs = Observable.create<List<ProductItem?>> {
                val list = mutableListOf<ProductItem?>()
                serverList.forEach { serverItem ->
                    serverItem?.quantity = "0"
                    if (cartList.isNotEmpty())
                        cartList.forEach { cartItem ->
                            if (serverItem?.id == cartItem.id) {
                                serverItem.quantity = cartItem.quantity
                            }
                        }
                    list.add(serverItem)
                }
                it.onNext(list)
                it.onComplete()
            }
            obs.subscribe {
                callback(it)
            }
        }
    }
}

