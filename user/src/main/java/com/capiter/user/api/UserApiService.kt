package com.capiter.user.api

import com.capiter.user.model.ProductsResponseItem
import com.capiter.user.ui.cart.CreateOrderResponse
import com.capiter.user.ui.cart.OrderList
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by MahmoudAyman on 6/24/2020.
 **/

interface UserApiService {

    @GET("products")
    fun getProductsObs(@Query("page") page: Int): Observable<List<ProductsResponseItem>>

    @POST("orders")
    fun createOrder(@Body orderList: List<OrderList>): Observable<List<CreateOrderResponse>?>

}