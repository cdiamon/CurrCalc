package com.padmitriy.android.currcalc.db.dao

import androidx.room.*
import com.padmitriy.android.currcalc.model.RateModel
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
interface RatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRate(rateModel: RateModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRatesList(rateModel: List<RateModel>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateRate(vararg rateModel: RateModel)

    @Delete
    fun deleteRate(vararg rateModel: RateModel)

    //    @Query("SELECT * FROM RateModel ORDER BY value ASC")
    @Query("SELECT * FROM RateModel")
    fun getAllRates(): Flowable<List<RateModel>>

    @Query("SELECT * FROM RateModel WHERE name = :name")
    fun getRateByName(name: String): Single<RateModel>

    @Query("DELETE FROM RateModel")
    fun clearAllRates()

    @Transaction
    fun updateRatesList(rates: List<RateModel>) {
        clearAllRates()
        insertRatesList(rates)
    }
}