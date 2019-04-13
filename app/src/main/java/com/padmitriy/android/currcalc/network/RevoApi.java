package com.padmitriy.android.currcalc.network;

import com.padmitriy.android.currcalc.model.RatesResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RevoApi {

    @GET("latest")
    Observable<RatesResponse> getRates(@Query("base") String baseCurrency);
}
