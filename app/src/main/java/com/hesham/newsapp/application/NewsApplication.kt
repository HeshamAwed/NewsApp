package com.hesham.newsapp.application

import android.app.Application
import android.content.Context
import com.hesham.newsapp.di.applicationModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication : Application() {
    companion object {
        lateinit var context: Context
        fun getAppContext(): Context {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initDi()
    }

    private fun initDi() {

        startKoin {
            androidContext(this@NewsApplication)
            modules(applicationModules)
        }
    }

}
