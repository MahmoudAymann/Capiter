package com.capiter.main.data.remote

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by MahmoudAyman on 6/24/2020.
 **/

interface DeliveryApiService {

  @GET("orders")
  fun getProductsObs(): Observable<List<DeliveryProductResponseItem>>
}