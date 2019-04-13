package com.padmitriy.android.currcalc.mvp

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), BaseView {

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(@StringRes message: Int) {
        showMessage(getString(message))
    }
}