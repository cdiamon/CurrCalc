package com.padmitriy.android.currcalc.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.padmitriy.android.currcalc.CurrCalcApplication
import com.padmitriy.android.currcalc.R
import com.padmitriy.android.currcalc.model.RateModel
import com.padmitriy.android.currcalc.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_currencies_list.*
import javax.inject.Inject

class CurrenciesListActivity : BaseActivity(), CurrenciesListView, CurrenciesListAdapter.CurrencyValueListener {

    @Inject
    lateinit var currenciesListPresenter: CurrenciesListPresenter

    private val adapter: CurrenciesListAdapter by lazy { CurrenciesListAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CurrCalcApplication.appComponent.inject(this)
        setContentView(R.layout.activity_currencies_list)

        currRecycler.layoutManager = LinearLayoutManager(this)
        currRecycler.adapter = adapter

        currenciesListPresenter.attachView(this)

        currenciesListPresenter.getCurrenciesList("eur")

        initViews()
    }

    private fun initViews() {
    }

    override fun showCurrencies(list: List<RateModel>) {
        adapter.setCurrencies(list)
    }

    override fun onFocusChanged(name: String) {
        currenciesListPresenter.getCurrenciesList(name)
    }

    override fun onValueChanged(value: Double) {
        currenciesListPresenter.baseValueChanged(value)
    }

    override fun onDestroy() {
        super.onDestroy()
        currenciesListPresenter.dropView()
    }
}
