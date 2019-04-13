package com.padmitriy.android.currcalc.di;

import com.google.gson.Gson;
import com.padmitriy.android.currcalc.BuildConfig;
import com.padmitriy.android.currcalc.network.RevoApi;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Module
public class NetworkModule {

    @Provides
    public OkHttpClient provideOkhttpClient() {
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);
            httpBuilder.addInterceptor(interceptor);
        }

        return httpBuilder
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();
    }

    @Provides
    public Retrofit provideRetrofitRxApi(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    public RevoApi provideBsnApi(Retrofit retrofit) {
        return retrofit.create(RevoApi.class);
    }
}
