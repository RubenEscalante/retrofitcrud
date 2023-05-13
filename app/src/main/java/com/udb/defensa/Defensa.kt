package com.udb.defensa

import android.app.Application
import com.udb.defensa.data.SettingsDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Defensa: Application()
//{
//    lateinit var settingsDataStore: SettingsDataStore
//
//    override fun onCreate() {
//        super.onCreate()
//        settingsDataStore = SettingsDataStore(applicationContext)
//    }
//}