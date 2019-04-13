package com.padmitriy.android.currcalc.mvp


import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<T : BaseView> {

    private var disposables: CompositeDisposable = CompositeDisposable()
    lateinit var view: T

    fun attachView(view: T) {
        this.view = view
    }

    fun unsubscribeOnDrop(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun dropView() {
        disposables.clear()
    }
}
