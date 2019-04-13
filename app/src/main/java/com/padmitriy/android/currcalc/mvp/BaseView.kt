package com.padmitriy.android.currcalc.mvp

import androidx.annotation.StringRes


interface BaseView {

    fun showMessage(message: String)

    fun showMessage(@StringRes message: Int)
}