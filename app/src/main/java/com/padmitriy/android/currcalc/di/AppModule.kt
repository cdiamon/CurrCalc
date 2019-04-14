package com.padmitriy.android.currcalc.di

import android.content.Context
import com.padmitriy.android.currcalc.CurrCalcApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: CurrCalcApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return app.applicationContext
    }
}
