package com.padmitriy.android.currcalc.view

import com.padmitriy.android.currcalc.R
import com.padmitriy.android.currcalc.db.dao.RatesDao
import com.padmitriy.android.currcalc.model.RateModel
import com.padmitriy.android.currcalc.mvp.BasePresenter
import com.padmitriy.android.currcalc.network.RevoApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random


class CurrenciesListPresenter @Inject constructor(
    private val ratesDao: RatesDao,
    private val revoApi: RevoApi
) : BasePresenter<CurrenciesListView>() {

    private var currenciesFetchingDisposable: Disposable? = null

    //    fun changeSummitRating(ratesResponse: RatesResponse) {
//        unsubscribeOnDrop(
//            Observable.fromCallable { ratesDao.updateRate(ratesResponse) }
//                .subscribeOn(Schedulers.io())
//                .subscribe())
    fun baseValueChanged(baseCurrency: String, value: Double) {
        getCurrenciesList(baseCurrency, value)
    }

    fun getCurrenciesList(baseCurrency: String) {
        getCurrenciesList(baseCurrency, 1.0)
    }

    /**
     * subscribing to changes in DB and rewriting view on any update
     */
    fun getCurrenciesList(baseCurrency: String, baseValue: Double) {

        currenciesFetchingDisposable?.dispose()

        currenciesFetchingDisposable = (
                revoApi.getRates(baseCurrency.toUpperCase())
                    .repeatWhen { t -> t.delay(1, TimeUnit.SECONDS) }
                    .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map { t -> t.rates }
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
                    })
                )
    }

    fun getCurrencies() {

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

        getCurrenciesList("eur")
    }


    /**
     * in this method we take random item from database, and with random time interval changing
     * item's rating with random value
     */
//    fun startRandomizing() {
//        randomSummitDisposable = Observable.just(Any()).flatMap {
//            Observable.zip(
//                ratesDao.getRateByName(getRandomId()).toObservable(),
//                Observable.interval(getRandomDelay(), TimeUnit.MILLISECONDS),
//                BiFunction<RatesResponse, Long, RatesResponse> { summit, _ -> return@BiFunction summit })
//        }
//            .subscribeOn(Schedulers.io())
//            .map { return@map it.apply { rating = getRandomRating() } }
//            .doOnNext { summit ->
//                ratesDao.updateRate(summit)
//            }
//            .repeat()
//            .subscribe({}, { throwable ->
//                throwable.printStackTrace()
//            })
//
//        unsubscribeOnDrop(randomSummitDisposable)
//    }

    fun stopFetching() {
        currenciesFetchingDisposable?.dispose()
    }

    fun getRandomId(): Int {
        return (Random.nextInt(10) + 1).also { Timber.i("RND id: $it") }
    }

    fun getRandomRating(): Float {
        return (Random.nextFloat() * 5).also { Timber.i("RND rating: $it") }
    }

    fun getRandomDelay(): Long {
        return Random.nextLong(100, 1000).also { Timber.i("RND delay: $it") }
    }
}
