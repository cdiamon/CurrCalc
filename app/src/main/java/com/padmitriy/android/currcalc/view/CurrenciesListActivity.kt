package com.padmitriy.android.currcalc.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.padmitriy.android.currcalc.CurrCalcApplication
import com.padmitriy.android.currcalc.model.RateModel
import com.padmitriy.android.currcalc.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_currencies_list.*
import javax.inject.Inject


class CurrenciesListActivity : BaseActivity(), CurrenciesListView, CurrenciesListAdapter.CurrencyValueListener {

    @Inject
    lateinit var currenciesListPresenter: CurrenciesListPresenter

    private val currenciesAdapter: CurrenciesListAdapter by lazy { CurrenciesListAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CurrCalcApplication.appComponent.inject(this)
        setContentView(com.padmitriy.android.currcalc.R.layout.activity_currencies_list)
        currenciesListPresenter.attachView(this)

        initViews()

        currenciesListPresenter.startFetchingCurrencies()
    }

    private fun initViews() {
        currRecycler.layoutManager = LinearLayoutManager(this)
        currRecycler.adapter = currenciesAdapter
    }

    override fun showCurrencies(list: List<RateModel>) {
        currenciesAdapter.setCurrencies(list)
    }

    override fun onValueChanged(name: String, value: Double) {
        currenciesListPresenter.getCurrenciesOnValueChanged(name, value)
    }

    override fun onFocusChanged(name: String) {
        currenciesListPresenter.changeBaseCurrency(name)
    }

    override fun onDestroy() {
        super.onDestroy()
        currenciesListPresenter.stopFetching()
        currenciesListPresenter.dropView()
    }
}
