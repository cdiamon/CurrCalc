package com.padmitriy.android.currcalc.di

import android.content.Context
import androidx.room.Room
import com.padmitriy.android.currcalc.db.CurrCalcDatabase
import com.padmitriy.android.currcalc.db.dao.RatesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    internal fun provideCurrenciesDatabase(context: Context): CurrCalcDatabase {
        return Room
            .databaseBuilder(context, CurrCalcDatabase::class.java, "currcalc")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    internal fun provideCurrenciesDao(currCalcDatabase: CurrCalcDatabase): RatesDao {
        return currCalcDatabase.getCurrCalcDao()
    }

}