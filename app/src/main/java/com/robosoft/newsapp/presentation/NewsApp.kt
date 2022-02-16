package com.robosoft.newsapp.presentation

import android.app.Application
import com.robosoft.newsapp.presentation.di.mainModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin



import org.koin.dsl.module

/*@HiltAndroidApp*/
class NewsApp :Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Koin Android logger
           // androidLogger()
            //inject Android context
            androidContext(this@NewsApp)
            // use modules
            val moduleList = listOf(mainModule)
            modules(moduleList)
        }

    }
}