package com.padmitriy.android.currcalc.view

import com.padmitriy.android.currcalc.model.RateModel
import com.padmitriy.android.currcalc.mvp.BaseView


interface CurrenciesListView : BaseView {
    fun showCurrencies(list: List<RateModel>)
}