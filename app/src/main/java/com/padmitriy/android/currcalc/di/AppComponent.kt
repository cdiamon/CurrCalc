package com.padmitriy.android.currcalc.di

import com.padmitriy.android.currcalc.CurrCalcApplication
import com.padmitriy.android.currcalc.view.CurrenciesListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, NetworkModule::class, SerializerModule::class])
interface AppComponent {

    fun inject(currCalcApplication: CurrCalcApplication)

    fun inject(currenciesListActivity: CurrenciesListActivity)

    object Initializer {
        fun init(application: CurrCalcApplication): AppComponent {
            return DaggerAppComponent.builder()
                .appModule(AppModule(application))
                .build()
        }
    }
}
