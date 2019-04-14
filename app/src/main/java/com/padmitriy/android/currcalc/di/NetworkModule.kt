package com.padmitriy.android.currcalc.di

import com.google.gson.Gson
import com.padmitriy.android.currcalc.BuildConfig
import com.padmitriy.android.currcalc.network.RevoApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        val httpBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
            httpBuilder.addInterceptor(interceptor)
        }

        return httpBuilder
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    fun provideRetrofitRxApi(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideBsnApi(retrofit: Retrofit): RevoApi {
        return retrofit.create(RevoApi::class.java)
    }
}
