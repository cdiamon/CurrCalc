package com.padmitriy.android.currcalc.view

import com.padmitriy.android.currcalc.ImmediateSchedulerRule
import com.padmitriy.android.currcalc.R
import com.padmitriy.android.currcalc.db.dao.RatesDao
import com.padmitriy.android.currcalc.model.RateModel
import com.padmitriy.android.currcalc.network.RevoApi
import io.reactivex.Flowable
import io.reactivex.Observable
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class CurrenciesListPresenterTest {

    @get:Rule
    val schedulerRule = ImmediateSchedulerRule()

    @Mock
    lateinit var ratesDao: RatesDao
    @Mock
    lateinit var revoApi: RevoApi
    @Mock
    lateinit var view: CurrenciesListView
    @InjectMocks
    lateinit var presenter: CurrenciesListPresenter

    @Before
    fun setUp() {
        presenter.attachView(view)
    }

    @Test(timeout = 1500)
    fun startFetchingCurrencies_ListIsEmpty() {
        `when`(ratesDao.getAllRates()).thenReturn(Flowable.empty())
        `when`(revoApi.getRates("EUR")).thenReturn(Observable.error(Exception()))

        presenter.startFetchingCurrencies()

        verify(view, atLeastOnce()).showMessage(R.string.error_message)
        verify(view, never()).showCurrencies(ArgumentMatchers.anyList())
    }

    @Test(timeout = 1500)
    fun startFetchingCurrencies_ListIsNotEmpty() {
        `when`(ratesDao.getAllRates()).thenReturn(Flowable.just(TEST_RATES_LIST))
        `when`(revoApi.getRates("EUR")).thenReturn(Observable.error(Exception()))

        presenter.startFetchingCurrencies()

        verify(view, atLeastOnce()).showMessage(R.string.error_message)
        verify(view, atLeastOnce()).showCurrencies(TEST_RATES_LIST)
    }

//    @Test
//    fun fillRateList_methodCalled() {
//        presenter.fillRateList()
//
//        verify(view, never()).showMessage(R.string.error_message)
//        verify(ratesDao, atLeastOnce()).insertRatesList(ArgumentMatchers.anyList())
//    }
//
//    @Test
//    fun changeRateRating_methodCalled() {
//        presenter.changeRateRating(TEST_RATE_DTO)
//
//        verify(view, never()).showMessage(R.string.error_message)
//        verify(ratesDao, atLeastOnce()).updateRate(TEST_RATE_DTO)
//    }
//
//    @Test(timeout = 5000)
//    fun startRandomizing_methodsCalled() {
//        `when`(ratesDao.getRateById(ArgumentMatchers.anyInt())).thenReturn(Single.just(TEST_RATE_DTO))
//        //hack to stop loop
//        `when`(ratesDao.updateRate(TEST_RATE_DTO)).thenThrow(Exception::class.java)
//
//        presenter.startRandomizing()
//
//        verify(view, never()).showMessage(R.string.error_message)
//        verify(ratesDao, atLeastOnce()).updateRate(TEST_RATE_DTO)
//        verify(ratesDao, atLeastOnce()).getRateById(ArgumentMatchers.anyInt())
//    }

    @After
    fun tearDown() {
        presenter.dropView()
    }

    companion object {
        private val TEST_RATES_LIST = arrayListOf(RateModel("", 0.0))
        private val TEST_RATE_DTO = RateModel("", 0.0)
    }
}
