package com.padmitriy.android.currcalc

import android.app.Application
import com.padmitriy.android.currcalc.di.AppComponent

class CurrCalcApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        appComponent = AppComponent.Initializer.init(this)
        appComponent.inject(this)
    }

    companion object {
        lateinit var appComponent: AppComponent
            private set
    }
}