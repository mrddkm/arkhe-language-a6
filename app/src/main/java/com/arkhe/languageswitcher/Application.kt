package com.arkhe.languageswitcher

import android.app.Application
import com.arkhe.languageswitcher.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LanguageSwitcherApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@LanguageSwitcherApplication)
            modules(appModule)
        }
    }
}