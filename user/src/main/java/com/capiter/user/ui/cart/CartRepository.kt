package com.capiter.user.ui.cart

import com.capiter.user.api.UserApiService
import com.capiter.user.db.UserDao
import com.capiter.user.model.ProductItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val userApiService: UserApiService,
    private val userDao: UserDao
) {

    fun getAllInCart(
        onSuccessCallBack: (List<ProductItem>) -> Unit,
        onErrorCallBack: (String?) -> Unit = {}
    ) {
        userDao.getAllProducts().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                onSuccessCallBack(it)
            }, {
                onErrorCallBack(it.message)
            })
    }

    fun deleteItemFromCart(
        item: ProductItem?,
        onSuccessCallBack: (ProductItem) -> Unit,
        onErrorCallBack: (String?) -> Unit = {}
    ) {
        if (item == null) return
        userDao.deleteProduct(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessCallBack(item)
            }, {
                onErrorCallBack(it.message)
            })
    }

    fun submitOrder(
        orderName: String? = null,
        listOfProducts: List<ProductItem?>?,
        onSuccessCallBack: () -> Unit,
        onErrorCallBack: (String?) -> Unit
    ) {
        val listOfOrders = mutableListOf<OrderList>()
        Observable.fromIterable(listOfProducts).map {
            listOfOrders.add(
                OrderList(
                    orderName,
                    it?.name,
                    it?.id,
                    it?.price?.toInt(),
                    it?.image,
                    it?.quantity?.toInt()
                )
            )
        }.subscribe {
            userApiService.createOrder(listOfOrders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it != null) // this to make sure a successful request
                    {
                        deleteAll {
                            onSuccessCallBack()
                        }
                    }else
                        onErrorCallBack("server error please check")

                }, {
                    onErrorCallBack(it.message)
                })
        }
    }

    private fun deleteAll(onSuccessCallBack: () -> Unit = {}) {
        userDao.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccessCallBack()
            }, {
                Timber.e("$it")
            })
    }


}

