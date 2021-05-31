package com.capiter.main.data.remote

import com.capiter.user.ui.product.ProductsResponseItem
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by MahmoudAyman on 6/24/2020.
 **/

interface DeliveryApiService {

  @GET("products")
  fun getProductsObs(@Query("page") page: Int): Observable<List<ProductsResponseItem>>
}