package com.padmitriy.android.currcalc.view

import com.padmitriy.android.currcalc.R
import com.padmitriy.android.currcalc.db.dao.RatesDao
import com.padmitriy.android.currcalc.model.RateModel
import com.padmitriy.android.currcalc.mvp.BasePresenter
import com.padmitriy.android.currcalc.network.RevoApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CurrenciesListPresenter @Inject constructor(
    private val ratesDao: RatesDao,
    private val revoApi: RevoApi
) : BasePresenter<CurrenciesListView>() {

    private var currenciesFetchingDisposable: Disposable? = null

    /**
     * subscribing to changes in DB and rewriting view on any update
     */
    fun startFetchingCurrencies() {

        unsubscribeOnDrop(ratesDao.getAllRates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { list ->
                if (list.isNotEmpty()) {
                    view.showCurrencies(list)
                }
            }
            .doOnError { throwable ->
                throwable.printStackTrace()
                view.showMessage(R.string.error_message)
            }
            .subscribe())

        /**
         * starting default fetching
         */
        changeBaseCurrency("eur", 1.0)
    }

    /**
     * changing comparing value of base currency
     */
    fun getCurrenciesOnValueChanged(baseCurrency: String, value: Double) {
        subscribeToCurrencies(baseCurrency, value)
    }

    /**
     * changing base currency, setting base value to default
     */
    fun changeBaseCurrency(baseCurrency: String, value: Double) {
        subscribeToCurrencies(baseCurrency, value)
    }

    /**
     * @param baseCurrency currency that compares to others
     * @param baseValue value of base currency to calculate exchange rate
     */
    private fun subscribeToCurrencies(baseCurrency: String, baseValue: Double) {

        currenciesFetchingDisposable?.dispose()

        currenciesFetchingDisposable = (
                revoApi.getRates(baseCurrency.toUpperCase())
                    .repeatWhen { t -> t.delay(1, TimeUnit.SECONDS) }
                    .subscribeOn(Schedulers.io())
                    .subscribe({ t ->
                        val ratesList: ArrayList<RateModel> =
                            t.rates.entries.map { entry ->
                                var value = BigDecimal(entry.value.toString())
                                value = value.multiply(BigDecimal(baseValue.toString()))
                                    .setScale(4, BigDecimal.ROUND_HALF_DOWN)
                                RateModel(
                                    entry.key,
                                    value.toDouble()
                                )
                            }.toList() as ArrayList<RateModel>
                        ratesList.add(0, RateModel(t.base, baseValue))
                        ratesDao.updateRatesList(ratesList)
                    }, { t ->
                        t.printStackTrace()
                        view.showMessage(R.string.error_message)
                    })
                )
    }

    /**
     * unsubscribing from further updates
     */
    fun stopFetching() {
        currenciesFetchingDisposable?.dispose()
    }
}
