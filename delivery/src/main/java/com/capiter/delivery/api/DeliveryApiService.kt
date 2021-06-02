package com.capiter.delivery.api

import com.capiter.delivery.ui.product.DeliveryProductResponseItem
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

/**
 * Created by MahmoudAyman on 6/24/2020.
 **/

interface DeliveryApiService {

  @GET("orders")
  fun getProductsObs(): Observable<List<DeliveryProductResponseItem>>
}