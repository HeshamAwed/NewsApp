package com.hesham.newsapp.application

import android.app.Application
import com.hesham.newsapp.di.applicationModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initDi()
    }

    private fun initDi() {
        startKoin {
            androidContext(this@NewsApplication)
            modules(applicationModules)
        }
    }

}