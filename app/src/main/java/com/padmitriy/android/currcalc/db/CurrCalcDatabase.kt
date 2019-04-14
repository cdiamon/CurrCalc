package com.padmitriy.android.currcalc.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.padmitriy.android.currcalc.db.dao.RatesDao
import com.padmitriy.android.currcalc.model.RateModel

@Database(entities = [RateModel::class], version = 2, exportSchema = false)
//@TypeConverters(DateTypeConverter::class)
abstract class CurrCalcDatabase : RoomDatabase() {

    abstract fun getCurrCalcDao(): RatesDao

}
