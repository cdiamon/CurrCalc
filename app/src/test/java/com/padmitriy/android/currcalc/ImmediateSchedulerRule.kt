package com.padmitriy.android.currcalc

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ImmediateSchedulerRule : TestRule {

    private val immediate = Schedulers.trampoline()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { scheduler -> immediate }
                RxJavaPlugins.setComputationSchedulerHandler { scheduler -> immediate }
                RxJavaPlugins.setNewThreadSchedulerHandler { scheduler -> immediate }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
                RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler -> immediate }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}