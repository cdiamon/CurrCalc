package com.padmitriy.android.currcalc.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides

@Module
class SerializerModule {

    @Provides
    fun provideApiGson(): Gson {
        return GsonBuilder()
            .create()
    }
}
