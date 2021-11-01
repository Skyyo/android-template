package com.skyyo.template.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
// import com.github.venom.Venom
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        Venom.createInstance(this).apply {
//            initialize()
//            start()
//        }
    }
}
