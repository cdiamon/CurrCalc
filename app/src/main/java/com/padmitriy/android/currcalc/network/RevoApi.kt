package com.padmitriy.android.currcalc.network

import com.padmitriy.android.currcalc.model.RatesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RevoApi {

    @GET("latest")
    fun getRates(@Query("base") baseCurrency: String): Single<RatesResponse>
}
