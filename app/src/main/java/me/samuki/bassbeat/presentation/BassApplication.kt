package me.samuki.bassbeat.presentation

import android.app.Application
import me.samuki.bassbeat.presentation.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BassApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BassApplication)
            modules(mainModule)
        }
    }
}
